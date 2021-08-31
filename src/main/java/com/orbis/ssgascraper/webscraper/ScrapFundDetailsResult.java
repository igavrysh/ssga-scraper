package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ScrapFundDetailsResult {

  private FundDto fund;

  private List<WeightDto> weights;

}
