package com.mitocode.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import static java.util.Locale.*;

@RestController
@AllArgsConstructor
@RequestMapping("/languages")
public class LanguageController {
    private final LocaleResolver localeResolver;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @GetMapping("/locale/{loc}")
    public ResponseEntity<Void> changeLocale(@PathVariable("loc") String loc) {
        val userLocale = switch (loc) {
            case "en" -> ENGLISH;
            case "fr" -> FRENCH;
            default -> ROOT;
        };
        localeResolver.setLocale(httpServletRequest, httpServletResponse, userLocale);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}