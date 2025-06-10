package com.hjusic.ddd.prompting.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjusic.ddd.common.event.model.Event;
import java.io.IOException;

public interface Promptings {

  public BoundedContext trigger(Event event) throws IOException, InterruptedException;
}
