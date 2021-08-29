package com.orbis.ssgascraper.webscraper;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.orbis.ssgascraper.dto.FundDto;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SsgaScraperFundsList {
  private final static Logger LOGGER = Logger.getLogger(String.valueOf(SsgaScraperFundsList.class));

  public List<FundDto> scrapFundsList(String indexLink) {
    JBrowserDriver driver = BrowserDriver.buildDriver();
    try {
      URL indexUrl = new URL(indexLink);
      LOGGER.log(Level.INFO, "Driver creating successful");
      driver.get(indexLink);
      String loadedPage = driver.getPageSource();
      LOGGER.log(Level.INFO, "STARTED JSOUP PARSING");
      final Document document = Jsoup.parse(loadedPage);

      // process document
      List<FundDto> funds = processIndexDocument(document, indexUrl);

      return funds;
    } catch (Exception ex) {
      // release the ScraperDataDispatcher thread if exception occurs.
      ex.printStackTrace();
    } finally{
      driver.quit();
    }
    return new ArrayList<>();
  }

  /**
   * Processes and extracts the document using CSS query selectors
   *
   * @param document: HTML document
   * @return List of FundDTO entites
   */
  private List<FundDto> processIndexDocument(Document document, URL indexURL) {
    LOGGER.log(Level.INFO, "Started Processing SsgaScraper Index document");

    List<FundDto> result = new ArrayList<>();

    try {
      // iterate over the document
      for (Element row : document.select("div.tb-body table tbody")) {

        // query selectors to scrape the data.
        Element nameEl = row.select("tr:nth-of-type(1) td.fundName a").first();
        Element tickerEl = row.select("tr:nth-of-type(1) td.fundTicker a").first();
        Element domicileEl = row.select("tr:nth-of-type(1) td.domicile div").first();

        if (nameEl != null && tickerEl != null && domicileEl != null) {
          FundDto fundDto = FundDto.builder()
              .name(nameEl.text())
              .ticker(tickerEl.text())
              .domicile(domicileEl.text())
              .link(indexURL.getProtocol()
                  + "://" + indexURL.getHost()
                  + nameEl.attr("href"))
              .build();
          result.add(fundDto);
        } else {
          LOGGER.log(Level.SEVERE,
              String.format("SsgaScrapper failed to parse funds, name %s, ticker %s, domicile $s", nameEl, tickerEl, domicileEl));
        }
      }
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Error occurred while processing the document");
    }
    LOGGER.log(Level.INFO, "Finished Processing SsgaScraper document");
    return result;
  }

}
