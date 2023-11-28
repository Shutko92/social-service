package ru.skillbox.diplom.group42.social.service.config.email.recovery;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
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
    public JavaMailSender getJavaMailSender() throws GeneralSecurityException, IOException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setUsername(USER_NAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.debug", "true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.smtps.ssl.socketFactory", sf);
        return mailSender;

    }


}
