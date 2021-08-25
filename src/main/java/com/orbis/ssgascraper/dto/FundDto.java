package com.orbis.ssgascraper.dto;

import com.orbis.ssgascraper.model.Fund;
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

    public static FundDto fundDtoFromFund(Fund fund) {
        return FundDto.builder()
                .ticker(fund.getTicker())
                .name(fund.getName())
                .domicile(fund.getDomicile())
                .description(fund.getDescription())
                .link(fund.getLink())
                .build();
    }

}
