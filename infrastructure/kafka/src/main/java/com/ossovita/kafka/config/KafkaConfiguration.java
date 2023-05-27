package com.ossovita.kafka.config;


import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.kafka.model.SubscriptionPaymentResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaAddress;


    @Value("${spring.kafka.group.id}")
    private String groupId;


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    //producer
    @Bean
    public ProducerFactory producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory(config);
    }


    //consumer
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializerWithJTM.class);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 25000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 35000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        return props;
    }

    //consumer | kafkalistenercontainerfactory | NOTE: we need to create additional KafkaListenerContainerFactory for each data type we want to listen
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReservationPaymentResponse> reservationPaymentResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReservationPaymentResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),//
                new StringDeserializer(),
                new JsonDeserializer<>(ReservationPaymentResponse.class, false)));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubscriptionPaymentResponse> subscriptionPaymentResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SubscriptionPaymentResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),//
                new StringDeserializer(),
                new JsonDeserializer<>(SubscriptionPaymentResponse.class, false)));
        return factory;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RoomStatusUpdateRequest> roomStatusUpdateRequestConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RoomStatusUpdateRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),//
                new StringDeserializer(),
                new JsonDeserializer<>(RoomStatusUpdateRequest.class, false)));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReservationPaymentRefundRequest> reservationPaymentRefundRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReservationPaymentRefundRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),//
                new StringDeserializer(),
                new JsonDeserializer<>(ReservationPaymentRefundRequest.class, false)));
        return factory;
    }


}