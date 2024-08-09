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
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Component
@RequiredArgsConstructor
public class EmailUtil {
	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;
	
	public void sendMail(Mail mail) throws MessagingException {
		val message = emailSender.createMimeMessage();
		val helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
		val context = new Context();
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