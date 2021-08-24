package com.orbis.ssgascraper.webscraper;

/**
 * Enum to maintain scrapper information and registered for scraping.
 * Scraper Engine directly iterates over the values of this enum.
 */
public enum ScraperInfo {

    SSGA("https://www.ssga.com", "SSGA", 1);

    public final String URL;

    public final Integer ID;

    // website name
    public final String NAME;

    ScraperInfo(String URL, String NAME, Integer ID) {
        this.URL = URL;
        this.NAME = NAME;
        this.ID = ID;
    }
}
