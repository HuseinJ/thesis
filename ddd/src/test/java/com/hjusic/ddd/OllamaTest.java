package com.hjusic.ddd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjusic.ddd.prompting.model.DrawnBoundedContext;
import com.hjusic.ddd.prompting.model.Promptings;
import com.hjusic.ddd.prompting.model.ProposedBoundedContext;
import com.hjusic.ddd.prompting.model.PromptEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OllamaTest {

  @Autowired
  private OllamaChatModel ollamaChatModel;

  @Autowired
  private Promptings promptings;

  @Test
  void testOllamaChatModel() {
    Prompt prompt = new Prompt(
        "What's a fun fact about AI?",
        OllamaOptions.builder()
            .model(OllamaModel.LLAMA3_2)
            .temperature(0.4)
            .build()
    );

    ChatResponse response = ollamaChatModel.call(prompt);
    System.out.println("Response: " + response.getResult().getOutput().toString());
  }

  @Test
  void testDomainDesignPrompt() throws IOException, InterruptedException {
    PromptEvent ddp = PromptEvent.now(
        "a car has a motor and a color and can drive, also a car has a transmission with gears",
        OllamaModel.LLAMA3_2
    );

    ProposedBoundedContext parsed = (ProposedBoundedContext) promptings.trigger(ddp);

    DrawnBoundedContext drawnBoundedContext = (DrawnBoundedContext) promptings.trigger(parsed.toDrawModelEvent());

    System.out.println("Description: " + drawnBoundedContext.getDescription());
    System.out.println("PlantUML: " + drawnBoundedContext.getPlantuml());
    saveTempImage(drawnBoundedContext.getImage(), "png");
  }

  @Test
  void testStructuredAiOutput() throws JsonProcessingException {
    String jsonSchema = """
        {
            "type": "object",
            "properties": {
                "steps": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "explanation": { "type": "string" },
                            "output": { "type": "string" }
                        },
                        "required": ["explanation", "output"],
                        "additionalProperties": false
                    }
                },
                "final_answer": { "type": "string" }
            },
            "required": ["steps", "final_answer"],
            "additionalProperties": false
        }
        """;

    Prompt prompt = new Prompt("how can I solve 8x + 7 = -23",
        OllamaOptions.builder()
            .model(OllamaModel.LLAMA3_2.getName())
            .format(new ObjectMapper().readValue(jsonSchema, Map.class))
            .build());

    ChatResponse response = this.ollamaChatModel.call(prompt);

    System.out.println(response.getResult().getOutput().toString());
  }

  public static void saveTempImage(byte[] imageData, String fileExtension) throws IOException {
    Path tempFile = Files.createTempFile("plantuml-diagram-", "." + fileExtension);
    Files.write(tempFile, imageData);
    System.out.println("Image written to: " + tempFile.toAbsolutePath());
  }
}
