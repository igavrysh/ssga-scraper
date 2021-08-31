package com.orbis.ssgascraper.dto;

import com.orbis.ssgascraper.enums.WeightType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class WeightDto {

  private String name;

  private WeightType type;

  private LocalDate date;

  private Double value;

}
