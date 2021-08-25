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

    public static FundDto fundDtoFromFund(Fund f) {
        return FundDto.builder()
                .ticker(f.getTicker())
                .name(f.getName())
                .domicile(f.getDomicile())
                .description(f.getDescription())
                .link(f.getLink())
                .build();
    }

    public static Fund fundFromFundDto(FundDto f) {
        return Fund.builder()
                .ticker(f.getTicker())
                .name(f.getName())
                .domicile(f.getDomicile())
                .description(f.getDescription())
                .link(f.getLink())
                .build();
    }

}
