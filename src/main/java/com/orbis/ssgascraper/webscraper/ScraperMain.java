package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.service.FundService;

import com.orbis.ssgascraper.service.WeightService;
import java.net.URL;
import java.util.ArrayList;
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

  private final WeightService weightService;

  public void start() {
    try {
      URL indexUrl = new URL(INDEX_LINK);

      List<FundDto> funds = new SsgaScraperFundsList().scrapFundsList(indexUrl);

      funds.forEach(f -> {
        fundService.upsert(f);
      });

      AtomicReference<List<ScrapFundDetailsResult>> results
          = new AtomicReference<>(new ArrayList<>());
      List<Runnable> fundListTasks = funds
          .stream()
          .map(f -> {
            return (Runnable) () -> {
              ScrapFundDetailsResult result = new SsgaScraperFundDetails()
                  .scrapFundDetails(f.getLink(), f);
              results.get().add(result);
            };
          })
          .collect(Collectors.toList());
      ParallelRunner.executeTasksInParallel(fundListTasks);

      List<ScrapFundDetailsResult> updatedFundsWithWeights = results.get();
      updatedFundsWithWeights.forEach(r -> {
        fundService.upsert(r.getFund());

        r.getWeights().forEach(w -> {
          weightService.upsert(w);
        });
      });

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
