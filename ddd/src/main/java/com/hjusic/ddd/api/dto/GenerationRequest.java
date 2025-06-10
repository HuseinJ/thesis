package com.hjusic.ddd.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerationRequest {
  private String requirements;
  private String[] models;
}
