package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Weight;

public class WeightDtoTransformer {

  static public Weight toModel(WeightDto dto) {
    return Weight.builder()
        .name(dto.getName())
        .type(dto.getType())
        .date(dto.getDate())
        .value(dto.getValue())
        .build();
  }
}
