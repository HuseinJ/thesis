package com.hjusic.ddd.prompting.model;

import static com.hjusic.ddd.prompting.model.ProposedBoundedContext.RESPONSE_SCHEMA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjusic.ddd.common.event.model.Event;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class PromptEvent implements Event {

  private static final String ENGINEERING_PROMPT = """
      You are a Domain-Driven Design (DDD) Software architect.
      Based on the following requirements, generate a short description and a valid PlantUML diagram with all the classes values and properties you can identify in the given requirements.

      Requirements:
      
      %s
      
      Make extra sure that the generated PlantUML is valid and can be rendered. Only use the official PlantUML syntax.
      """;

  private final Prompt requirementsPrompt;
  private final UUID eventId;
  private final Instant timestamp;
  private final String model;

  public static PromptEvent now(String requirements, OllamaModel model)
      throws JsonProcessingException {
    var ollamaOptions = OllamaOptions.builder()
        .model(model.getName())
        .format(new ObjectMapper().readValue(RESPONSE_SCHEMA, Map.class))
        .build();

    String fullPrompt = String.format(ENGINEERING_PROMPT, requirements);

    var prompt = new Prompt(fullPrompt, ollamaOptions);

    return new PromptEvent(prompt, UUID.randomUUID(), Instant.now(), model.getName());
  }

  public static PromptEvent now(String requirements, String model)
      throws JsonProcessingException {
    var ollamaOptions = OllamaOptions.builder()
        .model(model)
        .format(new ObjectMapper().readValue(RESPONSE_SCHEMA, Map.class))
        .build();

    String fullPrompt = String.format(ENGINEERING_PROMPT, requirements);

    var prompt = new Prompt(fullPrompt, ollamaOptions);

    return new PromptEvent(prompt, UUID.randomUUID(), Instant.now(), model);
  }
}
