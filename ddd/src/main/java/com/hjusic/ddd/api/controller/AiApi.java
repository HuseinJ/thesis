package com.hjusic.ddd.api.controller;

import com.hjusic.ddd.api.dto.GenerationRequest;
import com.hjusic.ddd.api.infrastructure.OllamaModelProperties;
import com.hjusic.ddd.prompting.application.GenerateContext;
import com.hjusic.ddd.prompting.infrastructure.ImageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1")
public class AiApi {

  private final GenerateContext generateContext;
  private final ImageRepository imageRepository;
  private final OllamaModelProperties modelProperties;

  @PostMapping(value = "/generate")
  public ResponseEntity<Void> triggerAiGeneration(@RequestBody GenerationRequest request) {
    generateContext.generate(request.getRequirements(), request.getModels());
    return ResponseEntity.accepted().build();
  }

  @GetMapping(value = "/get/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId) {

    var uuid = UUID.fromString(imageId);
    var result = imageRepository.getImage(uuid);

    if (result == null || result.length == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity
        .ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(result);
  }

  @GetMapping("/models")
  public ResponseEntity<List<String>> getAvailableModels() {
    List<String> models = new ArrayList<>();
    models.add(modelProperties.getChat().getModel()); // default model
    models.addAll(modelProperties.getInit().getChat().getAdditionalModels()); // additional models
    return ResponseEntity.ok(models);
  }

}
