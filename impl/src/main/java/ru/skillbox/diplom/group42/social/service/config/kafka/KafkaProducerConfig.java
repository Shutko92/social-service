package ru.skillbox.diplom.group42.social.service.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamingMessageDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "kafka-enable", havingValue = "true", matchIfMissing = true)
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, MessageDto> producerMessageFactory() {
        Map<String, Object> producerFactoryConfig = new HashMap<>();
        producerFactoryConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerFactoryConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerFactoryConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerFactoryConfig, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, MessageDto> kafkaMessageTemplate() {
        return new KafkaTemplate<>(producerMessageFactory());
    }

    @Bean
    public ProducerFactory<String, EventNotificationDto> eventNotificationProducerFactory() {
        Map<String, Object> producerFactoryConfig = new HashMap<>();
        producerFactoryConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerFactoryConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerFactoryConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerFactoryConfig, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, EventNotificationDto> eventNotificationKafkaTemplate() {
        return new KafkaTemplate<>(eventNotificationProducerFactory());
    }

    @Bean
    public ProducerFactory<String, StreamingMessageDto<NotificationDto>> sendNotificationProducerFactory() {
        Map<String, Object> producerFactoryConfig = new HashMap<>();
        producerFactoryConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerFactoryConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerFactoryConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerFactoryConfig, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, StreamingMessageDto<NotificationDto>> sendNotificationKafkaTemplate() {
        return new KafkaTemplate<>(sendNotificationProducerFactory());
    }


}
