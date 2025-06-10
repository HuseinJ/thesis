package com.hjusic.ddd.api.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.ai.ollama")
public class OllamaModelProperties {
  private Chat chat = new Chat();
  private Init init = new Init();

  @Data
  public static class Chat {
    private String model;
  }

  @Data
  public static class Init {
    private ChatInit chat = new ChatInit();

    @Data
    public static class ChatInit {
      private List<String> additionalModels;
    }
  }
}
