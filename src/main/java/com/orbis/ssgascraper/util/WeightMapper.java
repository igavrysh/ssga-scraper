package com.orbis.ssgascraper.util;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

public class WeightMapper {

  static public void updateWeightFromDto(WeightDto weightDto, Weight weight) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.map(weightDto, weight);
  }

  static public Weight weightFromDto(WeightDto weightDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(weightDto, Weight.class);
  }
}
