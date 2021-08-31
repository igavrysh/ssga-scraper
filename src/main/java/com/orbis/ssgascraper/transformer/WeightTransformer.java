package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Weight;

public class WeightTransformer {
  public static WeightDto toDto(Weight w) {
    return WeightDto.builder()
        .name(w.getName())
        .type(w.getType())
        .date(w.getDate())
        .value(w.getValue())
        .build();
  }

  public static void updateWeightAtomicFieldsWithDto(Weight weight, WeightDto weightDto) {
    weight.setName(weightDto.getName());
    weight.setType(weightDto.getType());
    weight.setDate(weightDto.getDate());
    weight.setValue(weightDto.getValue());
  }
}
