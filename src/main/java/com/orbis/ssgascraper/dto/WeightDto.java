package com.orbis.ssgascraper.dto;

import com.orbis.ssgascraper.enums.WeightType;
import java.time.LocalDate;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class WeightDto {

  private Double weight;

  private String name;

  private WeightType type;

  private LocalDate date;
}
