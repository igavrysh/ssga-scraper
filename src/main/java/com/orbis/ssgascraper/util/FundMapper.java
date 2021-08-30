package com.orbis.ssgascraper.util;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class FundMapper {

  static public void updateFundFromDto(FundDto fundDto, Fund fund) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(fundDto, fund);
  }

  public Fund fundFromDto(FundDto fundDto) {

    ModelMapper modelMapper = new ModelMapper();
    TypeMap<FundDto, Fund> typeMap = modelMapper.createTypeMap(FundDto.class, Fund.class);
    typeMap.addMappings(mapper -> mapper.using(new WeightListConverter())
        .map(FundDto::getCountryWeights, Fund::setCountryWeights));
    typeMap.addMappings(mapper -> mapper.using(new WeightListConverter())
        .map(FundDto::getHoldingWeights, Fund::setHoldingWeights));
    typeMap.addMappings(mapper -> mapper.using(new WeightListConverter())
        .map(FundDto::getSectorWeights, Fund::setSectorWeights));
    return modelMapper.map(fundDto, Fund.class);
  }

}
