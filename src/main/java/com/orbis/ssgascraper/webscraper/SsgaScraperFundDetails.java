package com.orbis.ssgascraper.webscraper;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.orbis.ssgascraper.dto.FundDto;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SsgaScraperFundDetails {

  private final static Logger LOGGER = Logger.getLogger(String.valueOf(SsgaScraperFundsList.class));

  public FundDto scrapFundsList(String fundLink, FundDto fund) {
    JBrowserDriver driver = BrowserDriver.buildDriver();
    try {
      LOGGER.log(Level.INFO, "Driver creating successful");
      driver.get(ScraperInfo.SSGA.URL);
      String loadedPage = driver.getPageSource();
      LOGGER.log(Level.INFO, "STARTED JSOUP PARSING");
      final Document document = Jsoup.parse(loadedPage);

      // process document
      FundDto updatedFund = processFundDocument(document, fund);
      return updatedFund;
    } catch (Exception ex) {
      // release the ScraperDataDispatcher thread if exception occurs.
      ex.printStackTrace();
    } finally{
      driver.quit();
    }
    return null;
  }

  /**
   * Processes and extracts the fund document using CSS query selectors
   *
   * @param document: HTML document
   */
  private FundDto processFundDocument(Document document, FundDto fundDto) {
    LOGGER.log(Level.INFO, "Started Processing SsgaScraper Fund document");

    Element fundContentEl = document.select("div.fund-content").first();

    if (fundContentEl != null) {
      Element fundDesc = fundContentEl.select("div.content ul").first();
      if (fundDesc != null) {
        fundDto.setDescription(fundDesc.outerHtml());
      }
    }

    LOGGER.log(Level.INFO, "Finished Processing SsgaScraper Fund document");
    return fundDto;
  }

}
