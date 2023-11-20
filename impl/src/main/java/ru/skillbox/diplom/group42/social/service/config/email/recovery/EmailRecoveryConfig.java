package ru.skillbox.diplom.group42.social.service.config.email.recovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailRecoveryConfig {
    @Value("${spring.mail.host}")
    String HOST;
    @Value("${spring.mail.port}")
    Integer PORT;
    @Value("${spring.mail.username}")
    String USER_NAME;
    @Value("${spring.mail.password}")
    String PASSWORD;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setUsername(USER_NAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
