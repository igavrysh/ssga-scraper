package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.service.FundService;
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
      AtomicReference<List<FundDto>> funds = new AtomicReference<>(new ArrayList<>());
      ParallelRunner
          .executeTasksInParallel(Arrays.asList(() -> {
            funds.set(new SsgaScraperFundsList().scrapFundsList(INDEX_LINK));
          })
      );

      ParallelRunner.executeTasksInParallel(Arrays.asList(() -> {
        funds.get().forEach(f -> {
          fundService.upsert(f);
        });
      }));

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

      int t = 1;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
