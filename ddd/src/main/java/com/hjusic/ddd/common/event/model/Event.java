package com.hjusic.ddd.common.event.model;

import java.time.Instant;
import java.util.UUID;

public interface Event {
  UUID getEventId();

  Instant getTimestamp();

  default String getEventType() {
    return this.getClass().getSimpleName();
  };
}
