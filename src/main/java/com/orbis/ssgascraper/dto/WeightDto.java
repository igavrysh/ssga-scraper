package com.orbis.ssgascraper.dto;

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

  private String type;

  private Date data;

}
