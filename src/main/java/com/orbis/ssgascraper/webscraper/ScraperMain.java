package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.dto.FundDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ScraperMain {

  private static String INDEX_LINK = "https://www.ssga.com/us/en/individual/etfs/fund-finder";

  public void start() {
    try {
      AtomicReference<List<FundDto>> funds = new AtomicReference<>(new ArrayList<>());
      ParallelRunner
          .executeTasksInParallel(Arrays.asList(() -> {
            funds.set(new SsgaScraperFundsList().scrapFundsList(INDEX_LINK));
          })
      );

      AtomicReference<List<FundDto>> updatedFunds = new AtomicReference<>(new ArrayList<>());
      List<Runnable> fundListTasks = funds
          .get()
          .stream()
          .map(f -> {
            return (Runnable) () -> {
              FundDto fund = new SsgaScraperFundDetails().scrapFundDetails(f.getLink(), f);

              updatedFunds.get().add(fund);
            };
          })
          .collect(Collectors.toList());
      ParallelRunner.executeTasksInParallel(fundListTasks);

      List<FundDto> result = updatedFunds.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
