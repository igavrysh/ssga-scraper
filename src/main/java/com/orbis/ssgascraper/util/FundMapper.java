package com.orbis.ssgascraper.util;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.model.Fund;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

public class FundMapper {

  static public void updateFundFromDto(FundDto fundDto, Fund fund) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(fundDto, fund);
  }

  static public Fund fundFromDto(FundDto fundDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(fundDto, Fund.class);
  }

}
