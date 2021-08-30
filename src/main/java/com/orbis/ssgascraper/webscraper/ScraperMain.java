package com.orbis.ssgascraper.webscraper;

import com.google.common.collect.Lists;
import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.model.Weight;
import com.orbis.ssgascraper.service.FundService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScraperMain {

  private static String INDEX_LINK = "https://www.ssga.com/us/en/individual/etfs/fund-finder";

  private final FundService fundService;

  public void start() {
    try {
      URL indexUrl = new URL(INDEX_LINK);

      List<FundDto> funds = new SsgaScraperFundsList().scrapFundsList(indexUrl);

      funds.forEach(f -> {
        fundService.upsert(f);
      });

      WeightDto[] cws = {
          WeightDto.builder().name("Ukraine").date(LocalDate.now()).value(0.99).build(),
          WeightDto.builder().name("US").date(LocalDate.now()).value(0.1).build()
      };

      funds.get(0).setCountryWeights(Arrays.asList(cws));

      WeightDto[] sws = {
          WeightDto.builder().name("IT").date(LocalDate.now()).value(0.99).build(),
          WeightDto.builder().name("Manufacturing").date(LocalDate.now()).value(0.1).build()
      };

      funds.get(0).setSectorWeights(Arrays.asList(sws));

      funds.forEach(f -> {
        fundService.upsert(f);
      });


      /*

      AtomicReference<List<FundDto>> updatedFunds = new AtomicReference<>(new ArrayList<>());
      List<Runnable> fundListTasks = funds
          .stream()
          .map(f -> {
            return (Runnable) () -> {
              FundDto fund = new SsgaScraperFundDetails().scrapFundDetails(f.getLink(), f);
              updatedFunds.get().add(fund);
            };
          })
          .collect(Collectors.toList());
      ParallelRunner.executeTasksInParallel(fundListTasks);
      */

      /*
      List<FundDto> updatedFundsWithWeights = updatedFunds.get();
      updatedFundsWithWeights.forEach(f -> {
        fundService.upsert(f);
      });*/


    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
