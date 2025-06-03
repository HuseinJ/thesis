package com.hjusic.idp.core.event.model;

import java.time.Instant;
import java.util.UUID;

public interface Event {
  UUID getEventId();

  Instant getTimestamp();

  String getEventType();
}
