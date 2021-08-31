package com.orbis.ssgascraper.dto;

import java.util.ArrayList;
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

}
