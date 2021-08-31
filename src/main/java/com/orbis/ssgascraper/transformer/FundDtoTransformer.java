package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import java.util.List;
import java.util.stream.Collectors;

public class FundDtoTransformer {

  static private List<Weight> transformToModel(List<WeightDto> weights) {
    if (weights != null) {
      return weights.stream()
          .map(WeightDtoTransformer::toModel)
          .collect(Collectors.toList());
    }
    return null;
  }

  static public Fund toModel(FundDto dto) {
    return Fund.builder()
        .name(dto.getName())
        .ticker(dto.getTicker())
        .domicile(dto.getDomicile())
        .description(dto.getDescription())
        .link(dto.getLink())
        .build();
  }
}
