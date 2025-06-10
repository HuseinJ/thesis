package com.hjusic.ddd.prompting.model;

import lombok.Getter;

@Getter
public class DrawnBoundedContext extends BoundedContext {

  private final byte[] image;

  public DrawnBoundedContext(String description, String plantuml, String[] models,
      String[] aggregates, String[] valueObjects, String generationModel, byte[] image) {
    super(description, plantuml, models, aggregates, valueObjects, generationModel);
    this.image = image;
  }
}
