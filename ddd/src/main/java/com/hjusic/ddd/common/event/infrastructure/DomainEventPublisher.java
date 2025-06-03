package com.hjusic.idp.core.event.infrastructure;

import com.hjusic.idp.core.event.model.Event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Optional;
import org.springframework.stereotype.Component;

public class DomainEventPublisher {
  private final ApplicationEventPublisher applicationPublisher;
  private final Optional<KafkaTemplate<String, Event>> kafkaTemplate;
  private boolean kafkaEnabled = false;

  public DomainEventPublisher(
      ApplicationEventPublisher applicationPublisher,
      Optional<KafkaTemplate<String, Event>> kafkaTemplate) {
    this.applicationPublisher = applicationPublisher;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void enableKafka(boolean enabled) {
    this.kafkaEnabled = enabled && kafkaTemplate.isPresent();
  }

  public void publish(Event event) {
    applicationPublisher.publishEvent(event);

    if (kafkaEnabled) {
      publishToKafka(event);
    }
  }

  private void publishToKafka(Event event) {
    kafkaTemplate.ifPresent(template -> {
      String topic = determineTopicForEvent(event);
      template.send(topic, event.getEventId().toString(), event);
    });
  }

  private String determineTopicForEvent(Event event) {
    return "idp-" + event.getClass().getSimpleName().toLowerCase();
  }
}
