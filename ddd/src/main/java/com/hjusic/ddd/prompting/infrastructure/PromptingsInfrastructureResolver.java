package com.hjusic.ddd.prompting.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjusic.ddd.common.event.infrastructure.DomainEventPublisher;
import com.hjusic.ddd.common.event.model.Event;
import com.hjusic.ddd.prompting.model.BoundedContext;
import com.hjusic.ddd.prompting.model.DrawModelEvent;
import com.hjusic.ddd.prompting.model.DrawnBoundedContext;
import com.hjusic.ddd.prompting.model.PromptEvent;
import com.hjusic.ddd.prompting.model.Promptings;
import com.hjusic.ddd.prompting.model.ProposedBoundedContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.Deflater;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptingsInfrastructureResolver implements Promptings {

  private final String plantUmlServerUrl = "http://localhost:8085";
  private final String plantUmlFormat = "png";

  private final DomainEventPublisher domainEventPublisher;
  private final OllamaChatModel ollamaChatModel;
  private final ImageRepository imageRepository;

  @Override
  public BoundedContext trigger(Event event) throws IOException, InterruptedException {
    var boundedContext = switch (event) {
      case PromptEvent promptEvent -> handle(promptEvent);
      case DrawModelEvent drawModelEvent -> handle(drawModelEvent);
      default -> throw new IllegalArgumentException("Unsupported event type: " + event.getClass());
    };

    domainEventPublisher.publish(event);

    return boundedContext;
  }

  private BoundedContext handle(PromptEvent promptEvent) throws JsonProcessingException {
    ChatResponse response = ollamaChatModel.call(promptEvent.getRequirementsPrompt());
    String jsonOutput = response.getResult().getOutput().getText();
    ObjectMapper objectMapper = new ObjectMapper();
    ProposedBoundedContext parsed = objectMapper.readValue(jsonOutput,
        ProposedBoundedContext.class);
    parsed.setGenerationModel(promptEvent.getModel());
    return parsed;
  }

  private BoundedContext handle(DrawModelEvent drawModelEvent)
      throws IOException, InterruptedException {
    String encodedDiagram = encodePlantUML(drawModelEvent.getPlantUmlCode());

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(plantUmlServerUrl + "/png/" + encodedDiagram))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

    imageRepository.saveImage(drawModelEvent.getImageId(), response.body());

    return new DrawnBoundedContext(drawModelEvent.getDescription(),
        drawModelEvent.getPlantUmlCode(), null, null, null, drawModelEvent.getModel(),
        response.body());
  }

  private String encodePlantUML(String text) {
    byte[] compressed = compress(text);
    return encodeBase64PlantUML(compressed);
  }

  private byte[] compress(String text) {
    Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
    deflater.setInput(text.getBytes());
    deflater.finish();

    byte[] buffer = new byte[1024];
    int count;
    byte[] output = new byte[0];

    try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
      while (!deflater.finished()) {
        count = deflater.deflate(buffer);
        baos.write(buffer, 0, count);
      }
      output = baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
  }

  private String encodeBase64PlantUML(byte[] data) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < data.length; i += 3) {
      if (i + 2 < data.length) {
        sb.append(encode3bytes(data[i], data[i + 1], data[i + 2]));
      } else if (i + 1 < data.length) {
        sb.append(encode3bytes(data[i], data[i + 1], (byte) 0));
      } else {
        sb.append(encode3bytes(data[i], (byte) 0, (byte) 0));
      }
    }
    return sb.toString();
  }

  private static final char[] PLANTUML_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_".toCharArray();

  private String encode3bytes(byte b1, byte b2, byte b3) {
    int c1 = (b1 & 0xFF) >> 2;
    int c2 = ((b1 & 0x3) << 4) | ((b2 & 0xFF) >> 4);
    int c3 = ((b2 & 0xF) << 2) | ((b3 & 0xFF) >> 6);
    int c4 = b3 & 0x3F;
    return new String(new char[]{
        PLANTUML_ALPHABET[c1],
        PLANTUML_ALPHABET[c2],
        PLANTUML_ALPHABET[c3],
        PLANTUML_ALPHABET[c4]
    });
  }
}
