package com.hjusic.ddd.prompting.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ProposedBoundedContext extends BoundedContext {

  public static final String RESPONSE_SCHEMA = """
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

  public DrawModelEvent toDrawModelEvent() {
    return DrawModelEvent.now(getPlantuml(), getDescription(), getGenerationModel());
  }
}
