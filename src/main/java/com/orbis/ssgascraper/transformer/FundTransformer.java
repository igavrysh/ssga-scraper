package com.orbis.ssgascraper.transformer;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Fund;
import java.util.List;

public class FundTransformer {

  public static FundDto toDto(Fund f) {
    List<WeightDto> countryWeights = null;
    return FundDto.builder()
        .name(f.getName())
        .ticker(f.getTicker())
        .domicile(f.getDomicile())
        .description(f.getDescription())
        .link(f.getLink())
        .build();
  }

  public static void updateFundAtomicFieldsWithDto(Fund fund, FundDto fundDto) {
    if (fund.getName() != null) {
      fund.setName(fundDto.getName());
    }
    if (fund.getTicker() != null) {
      fund.setTicker(fundDto.getTicker());
    }
    if (fund.getDomicile() != null) {
      fund.setDomicile(fundDto.getDomicile());
    }
    if (fundDto.getDescription() != null) {
      fund.setDescription(fundDto.getDescription());
    }
    if (fundDto.getLink() != null) {
      fund.setLink(fundDto.getLink());
    }
  }
}
