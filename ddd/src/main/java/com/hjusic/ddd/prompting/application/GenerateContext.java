package com.hjusic.ddd.prompting.application;

import com.hjusic.ddd.prompting.model.PromptEvent;
import com.hjusic.ddd.prompting.model.Promptings;
import com.hjusic.ddd.prompting.model.ProposedBoundedContext;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateContext {

  private final Promptings promptings;

  public void generate(String requirements, String[] models) {
    if (StringUtils.isBlank(requirements)) {
      throw new IllegalArgumentException("Description cannot be null or empty");
    }
    if (models == null || models.length == 0) {
      throw new IllegalArgumentException("At least one model must be specified");
    }

    Arrays.stream(models).forEach(model -> {
      if (StringUtils.isBlank(model)) {
        throw new IllegalArgumentException("Model names cannot be null or empty");
      }

      // Launch each model trigger asynchronously
      CompletableFuture.runAsync(() -> {
        try {
          var promptingEvent = PromptEvent.now(requirements, model);
          var proposedDesign = promptings.trigger(promptingEvent);
          if (proposedDesign instanceof ProposedBoundedContext proposedBoundedContext) {
            promptings.trigger(proposedBoundedContext.toDrawModelEvent());
          }
        } catch (IOException | InterruptedException e) {
          e.printStackTrace(); // Log it; avoid crashing the main thread
        }
      });
    });
  }
}
