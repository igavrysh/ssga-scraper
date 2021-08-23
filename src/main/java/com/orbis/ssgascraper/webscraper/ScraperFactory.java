package com.orbis.ssgascraper.webscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScraperFactory {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperFactory.class));

    private final SsgaScraper ssgaScraper;

    @Autowired
    public ScraperFactory(SsgaScraper ssgaScraper) {
        this.ssgaScraper = ssgaScraper;
    }

    public Scraper createScraper(ScraperInfo scraperInfo) {
        switch (scraperInfo) {
            case SSGA:
                return ssgaScraper;

            default:
                LOGGER.log(Level.SEVERE, "Unsupported scraper " + scraperInfo);
        }
        return null;
    }
}
