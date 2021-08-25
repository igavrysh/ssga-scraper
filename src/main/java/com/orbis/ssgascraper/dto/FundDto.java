package com.orbis.ssgascraper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FundDto {

    private String ticker;

    private String name;

    private String domicile;

    private String description;

    private String link;

}
