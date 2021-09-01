package com.orbis.ssgascraper.jobs;

import com.orbis.ssgascraper.config.QuartzConfig;
import com.orbis.ssgascraper.webscraper.ScraperMain;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class ScraperMainJob implements Job {

  private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperMainJob.class));

  @Autowired
  private ScraperMain scraperMain;

  @Override
  public void execute(JobExecutionContext context) {
    LOGGER.log(Level.INFO,
        String.format("Job ** %s ** starting @ %s", context.getJobDetail().getKey().getName(),
            context.getFireTime()));
    scraperMain.start();
    LOGGER.log(Level.INFO,
        String.format("Job ** %s ** completed.  Next job scheduled @ %s",
            context.getJobDetail().getKey().getName(), context.getNextFireTime()));
  }
}
