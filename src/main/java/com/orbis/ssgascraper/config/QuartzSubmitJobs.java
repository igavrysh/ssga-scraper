package com.orbis.ssgascraper.config;

import com.orbis.ssgascraper.jobs.ScraperMainJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobs {

  private static final String CRON_EVERY_N_MINUTES = "0 0/5 * ? * * *";

  // Runs at minute 0 past hour 7 and 15
  private static final String CRON_TWICE_PER_DAY = "0 0 7,15/12 * * ?";

  // Runs every 6 hours starting from 00:00
  private static final String CRON_FOUR_TIMES_PER_DAY = "0 0 0,6,12,18/12 * * ?";

  @Bean(name = "scraperMainJob123")
  public JobDetailFactoryBean jobScraperMain() {
    return QuartzConfig.createJobDetail(ScraperMainJob.class, "Scrapper Main Job");
  }

  @Bean(name = "scraperMainTrigger")
  public CronTriggerFactoryBean triggerScraperMain(@Qualifier("scraperMainJob123") JobDetail jobDetail) {
    return QuartzConfig.createCronTrigger(jobDetail, CRON_FOUR_TIMES_PER_DAY, "Scrapper Main Trigger");
  }
}
