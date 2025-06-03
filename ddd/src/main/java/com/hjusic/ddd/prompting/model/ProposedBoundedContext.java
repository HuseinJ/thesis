package com.hjusic.ddd.prompting.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainDesignResponse {
  private String description;
  private String plantuml;
}
