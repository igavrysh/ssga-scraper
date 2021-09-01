package com.orbis.ssgascraper;

import com.orbis.ssgascraper.webscraper.ScraperMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SsgaScraperApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =  SpringApplication.run(SsgaScraperApplication.class, args);

		context.getBean(ScraperMain.class).start();
	}

}