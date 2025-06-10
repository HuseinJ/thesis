package com.hjusic.ddd.prompting.model;

import com.hjusic.ddd.common.event.model.Event;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class DrawModelEvent implements Event {

  private final UUID eventId;
  private final UUID imageId;
  private final Instant timestamp;
  private final String plantUmlCode;
  private final String description;
  private String model;

  public static DrawModelEvent now(String plantUmlCode, String description, String model) {
    return new DrawModelEvent(UUID.randomUUID(), UUID.randomUUID(), Instant.now(), plantUmlCode, description, model);
  }
}
