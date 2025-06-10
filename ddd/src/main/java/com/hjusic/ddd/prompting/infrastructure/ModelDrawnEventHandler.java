package com.hjusic.ddd.prompting.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjusic.ddd.prompting.model.DrawModelEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelDrawnEventHandler {

  private final SimpMessagingTemplate messagingTemplate;
  private final ObjectMapper objectMapper;

  @EventListener
  public void handleModelDrawnEvent(DrawModelEvent event) {
    try {
      String json = objectMapper.writeValueAsString(event);
      messagingTemplate.convertAndSend("/topic/ai-results", json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}
