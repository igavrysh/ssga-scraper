package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.SsgaScraperApplication;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Scraping entrypoint
 */

@Component
public class ScraperEngine {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(SsgaScraperApplication.class));


    /**
     * Starts the scrapping engine
     */
    public void start() {

    }

}
