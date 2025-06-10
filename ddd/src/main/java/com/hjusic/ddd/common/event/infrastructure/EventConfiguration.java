package com.hjusic.ddd.common.event.infrastructure;

import com.hjusic.ddd.common.event.model.Event;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class EventConfiguration {

  @Value("${kafka.bootstrap-servers:localhost:9092}")
  private String bootstrapServers;

  @Value("${kafka.enabled:false}")
  private boolean kafkaEnabled;

  @Bean
  public ProducerFactory<String, Event> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Event> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public DomainEventPublisher configureEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    var eventPublisher = new DomainEventPublisher(applicationEventPublisher, Optional.of(kafkaTemplate()));
    eventPublisher.enableKafka(kafkaEnabled);
    return eventPublisher;
  }

}
