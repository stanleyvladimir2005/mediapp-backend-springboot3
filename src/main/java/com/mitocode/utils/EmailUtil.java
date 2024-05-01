package com.mitocode.utils;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailUtil {
	
	private final JavaMailSender emailSender;
	
	private final SpringTemplateEngine templateEngine;
	
	public void sendMail(Mail mail) throws MessagingException {
		val message = emailSender.createMimeMessage();
		val helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		val context = new Context(); //Establece variables de la plantilla
		context.setVariables(mail.getModel());
		val html = templateEngine.process("email/email-template", context);
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		helper.addAttachment("MyTestFile.txt", new ByteArrayResource("test".getBytes()));
		emailSender.send(message);
	}
}