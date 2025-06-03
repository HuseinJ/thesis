package com.hjusic.ddd.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class DomainDesigningPrompt {

  private static final String responseSchema = """
{
  "type": "object",
  "properties": {
    "description": { "type": "string" },
    "plantuml": { "type": "string" }
  },
  "required": ["description", "plantuml"],
  "additionalProperties": false
}
""";

  private final Prompt requirementsPrompt;

  public static DomainDesigningPrompt from(String requirements, OllamaModel model)
      throws JsonProcessingException {
    var ollamaOptions = OllamaOptions.builder()
        .model(model.getName())
        .format(new ObjectMapper().readValue(responseSchema, Map.class))
        .build();

    String fullPrompt = String.format("""
      You are a Domain-Driven Design (DDD) Software architect.
      Based on the following requirements, generate a short description and a PlantUML with all the bounded contexts, Aggregates, Valueobjects etc..
      Your response must follow this JSON format with a 'description' and 'plantuml' field.

      Requirements:
      %s
      """, requirements);

    var prompt = new Prompt(fullPrompt, ollamaOptions);

    return new DomainDesigningPrompt(prompt);
  }
}
