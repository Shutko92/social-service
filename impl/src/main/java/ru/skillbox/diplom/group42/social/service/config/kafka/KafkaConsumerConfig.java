package ru.skillbox.diplom.group42.social.service.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamingMessageDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ConditionalOnProperty(value = "kafka-enable", havingValue = "true", matchIfMissing = true)
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, MessageDto> consumerMessageFactory() {
        Map<String, Object> consumerFactoryConfig = new HashMap<>();
        consumerFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerFactoryConfig, new StringDeserializer(), new JsonDeserializer<>(MessageDto.class));
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDto> kafkaListenerMessageContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerMessageFactory());
        return factory;
    }


    @Bean
    public ConsumerFactory<String, EventNotificationDto> eventNotificationDtoConsumerFactory() {
        Map<String, Object> consumerFactoryConfig = new HashMap<>();
        consumerFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerFactoryConfig, new StringDeserializer(), new JsonDeserializer<>(EventNotificationDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventNotificationDto> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventNotificationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventNotificationDtoConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StreamingMessageDto<NotificationDto>> sendNotificationDtoConsumerFactory() {
        Map<String, Object> consumerFactoryConfig = new HashMap<>();
        consumerFactoryConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerFactoryConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactoryConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerFactoryConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerFactoryConfig, new StringDeserializer(), new JsonDeserializer<>(StreamingMessageDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StreamingMessageDto<NotificationDto>> sendKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StreamingMessageDto<NotificationDto>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sendNotificationDtoConsumerFactory());
        return factory;
    }

}
