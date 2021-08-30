package com.orbis.ssgascraper.util;

import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Weight;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;

public class WeightListConverter extends AbstractConverter<List<WeightDto>, List<Weight>> {

  @Override
  protected List<Weight> convert(List<WeightDto> weightDtos) {

    return weightDtos
        .stream()
        .map(weightDto -> WeightMapper.weightFromDto(weightDto))
        .collect(Collectors.toList());
  }

}
