package ru.skillbox.diplom.group42.social.service.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "kafka-enable", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> adminConfig = new HashMap<>();
        adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(adminConfig);
    }

    @Bean
    NewTopic sendMessage(){
        return new NewTopic("send-message",1, (short) 1);
    }

    @Bean
    NewTopic notificationsHandler(){
        return new NewTopic("notifications-handler", 1,(short) 1);
    }

    @Bean
    NewTopic sendingNotifications(){
        return new NewTopic("sending-notifications", 1,(short) 1);
    }


}
