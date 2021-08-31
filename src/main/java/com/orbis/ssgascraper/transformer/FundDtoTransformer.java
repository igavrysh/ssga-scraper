package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import java.util.List;
import java.util.stream.Collectors;

public class FundDtoTransformer {

  static public Fund toModel(FundDto dto) {
    List<Weight> countryWeights = null;
    if (dto.getCountryWeights() != null) {
      countryWeights = dto.getCountryWeights()
          .stream()
          .map(WeightDtoTransformer::toModel)
          .collect(Collectors.toList());
    }

    return Fund.builder()
        .name(dto.getName())
        .ticker(dto.getTicker())
        .domicile(dto.getDomicile())
        .description(dto.getDescription())
        .link(dto.getLink())
        .countryWeights(countryWeights)
        .build();
  }


}
