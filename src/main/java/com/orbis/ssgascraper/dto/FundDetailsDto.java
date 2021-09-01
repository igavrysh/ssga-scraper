package com.orbis.ssgascraper.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class FundDetailsDto {

    private FundDto fundDto;

    private List<WeightDto> countryWeights;

    private List<WeightDto> holdingWeights;

    private List<WeightDto> sectorWeights;
}
