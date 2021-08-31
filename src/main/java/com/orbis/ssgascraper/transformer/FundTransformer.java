package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Fund;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FundTransformer {

  public static FundDto toDto(Fund f) {
    List<WeightDto> countryWeights = null;
    if (f.getCountryWeights() != null) {
      f.getCountryWeights()
          .stream()
          .map(w -> WeightTransformer.toDto(w))
          .collect(Collectors.toList());
    }
    return FundDto.builder()
        .name(f.getName())
        .ticker(f.getTicker())
        .domicile(f.getDomicile())
        .description(f.getDescription())
        .link(f.getLink())
        .countryWeights(countryWeights)
        .build();
  }

  public static void updateFundAtomicFieldsWithDto(Fund fund, FundDto fundDto) {
    fund.setName(fundDto.getName());
    fund.setTicker(fundDto.getTicker());
    fund.setDomicile(fundDto.getDomicile());
    fund.setDescription(fundDto.getDescription());
    fund.setLink(fundDto.getLink());
  }

}
