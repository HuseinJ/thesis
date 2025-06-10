package com.hjusic.ddd.prompting.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageRepository {

  private final Map<UUID, byte[]> imageStore = new HashMap<>();

  public void saveImage(UUID imageId, byte[] image) {
    imageStore.put(imageId, image);
  }

  public byte[] getImage(UUID imageId) {
    return imageStore.get(imageId);
  }

}
