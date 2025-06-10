package com.hjusic.ddd.prompting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class BoundedContext {
  private String description;
  private String plantuml;
  private String[] models;
  private String[] aggregates;
  private String[] valueObjects;
  @Setter
  private String generationModel;
}
