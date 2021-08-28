package com.orbis.ssgascraper.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class WeightTypeConverter implements AttributeConverter<WeightType, String> {
  @Override
  public String convertToDatabaseColumn(WeightType weightType) {
    if (weightType == null) {
      return null;
    }
    return weightType.getCode();
  }

  @Override
  public WeightType convertToEntityAttribute(final String code) {
    if (code == null) {
      return null;
    }

    return Stream.of(WeightType.values())
        .filter(c -> c.getCode().equals(code))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}