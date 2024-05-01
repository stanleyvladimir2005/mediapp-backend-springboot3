package com.mitocode.config;

import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;

@Configuration
public class MessageConfig {

    // Carga los properties
    @Bean
    public MessageSource messageSource(){
        val messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }

    // Establece un default locale
    @Bean
    public LocaleResolver localeResolver() {
        val slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ROOT);
        return slr;
    }

    // Para resolver las variables en messages
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        val bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}