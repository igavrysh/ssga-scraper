package com.orbis.ssgascraper.jobs;

import com.orbis.ssgascraper.webscraper.ScraperMain;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
public class ScraperMainJob implements Job {
  @Autowired
  private ScraperMain scraperMain;

  @Override
  public void execute(JobExecutionContext context) {
    log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
    scraperMain.start();
    log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
  }
}
