package com.orbis.ssgascraper.dto;

import com.orbis.ssgascraper.model.Fund;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class FundDto {

    private String ticker;

    private String name;

    private String domicile;

    private String description;

    private String link;

    private List<WeightDto> countryWeights;

    private List<WeightDto> sectorWeights;

    private List<WeightDto> holdingWeights;

}
