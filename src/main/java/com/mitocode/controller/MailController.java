package com.mitocode.controller;

import com.mitocode.model.ResetMail;
import com.mitocode.service.ILoginService;
import com.mitocode.service.IResetMailService;
import com.mitocode.utils.EmailUtil;
import com.mitocode.utils.Mail;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final ILoginService loginService;
    private final IResetMailService resetMailService;
    private final EmailUtil emailUtil;

    @PostMapping(value = "/sendMail")
    public ResponseEntity<Integer> sendMail(@RequestBody String username) throws Exception {
        var rpta = 0;
        val us = loginService.checkUsername(username);
        if(us != null && us.getIdUser() > 0) {
            val resetMail = new ResetMail();
            resetMail.setRandom(UUID.randomUUID().toString());
            resetMail.setUser(us);
            resetMail.setExpiration(10);
            resetMailService.save(resetMail);

            val mail = new Mail();
            mail.setFrom("email.prueba.demo@gmail.com");
            mail.setTo(us.getUsername());
            mail.setSubject("RESTABLECER CONTRASEÑA  MEDIAPP");

            val model = new HashMap<String, Object>();
            val url = "http://localhost:4200/#/forgot/" + resetMail.getRandom();
            model.put("user", resetMail.getUser().getUsername());
            model.put("resetUrl", url);
            mail.setModel(model);

            emailUtil.sendMail(mail);
            rpta = 1;
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @GetMapping(value = "/reset/check/{random}")
    public ResponseEntity<Integer> checkRandom(@PathVariable("random") String random) {
        var rpta = 0;
        try {
            if (random != null && !random.isEmpty()) {
                val rm = resetMailService.findByRandom(random);
                if (rm != null && rm.getId() > 0 && !rm.isExpired())
                    rpta = 1;
            }
        } catch (Exception e) {
            return new ResponseEntity<>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @PostMapping(value = "/reset/{random}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@PathVariable("random") String random, @RequestBody String password) {
        try {
            val rm = resetMailService.findByRandom(random);
            if (rm != null && rm.getId() > 0 && !rm.isExpired()) {
                loginService.changePassword(password, rm.getUser().getUsername());
                resetMailService.delete(rm);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}