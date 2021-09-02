package com.orbis.ssgascraper.webscraper;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.enums.WeightType;
import com.orbis.ssgascraper.util.ParsePercentage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SsgaScraperFundDetails {

  private final static Logger LOGGER = Logger.getLogger(String.valueOf(SsgaScraperFundsList.class));

  public ScrapFundDetailsResult scrapFundDetails(String fundLink, FundDto fund) {
    JBrowserDriver driver = BrowserDriver.buildDriver();
    try {
      LOGGER.log(Level.INFO, "Driver creating successful");
      driver.get(fundLink);
      String loadedPage = driver.getPageSource();
      LOGGER.log(Level.INFO, "STARTED JSOUP PARSING");
      final Document document = Jsoup.parse(loadedPage);

      // process document
      FundDto updatedFund = processFundDocument(document, fund);
      List<WeightDto> updatedWeights = processFundWeights(document, fund);
      return ScrapFundDetailsResult.builder()
          .fund(updatedFund)
          .weights(updatedWeights)
          .build();
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

    fundDto = processDescription(document, fundDto);
    LOGGER.log(Level.INFO, "Finished Processing SsgaScraper Fund document");
    return fundDto;
  }

  private List<WeightDto> processFundWeights(Document document, FundDto fundDto) {
    List<WeightDto> weights = new ArrayList<>();
    weights.addAll(processTopHoldings(document, fundDto));
    weights.addAll(processSectorAllocation(document, fundDto));
    weights.addAll(processGeographicalBreakdown(document, fundDto));
    return weights;
  }

  private Element fundContent(Document document) {
    return document.select("div.fund-content").first();
  }

  private FundDto processDescription(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return fundDto;
    }
    Element fundDesc = fundContentEl.select("div.content ul").first();
    if (fundDesc != null) {
      fundDto.setDescription(fundDesc.outerHtml());
    }
    return fundDto;
  }

  private List<WeightDto> processTopHoldings(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return new ArrayList<>();
    }
    Element fundTopHoldingsEl = fundContentEl.select("div.fund-top-holdings").first();
    if (fundTopHoldingsEl == null) {
      return new ArrayList<>();
    }

    Element asOfDateEl = fundContentEl.select("div.fund-top-holdings h3 span.date").first();
    LocalDate localDate = parseDateFromElement(asOfDateEl);
    if (localDate == null) {
      return new ArrayList<>();
    }

    Elements rows = fundTopHoldingsEl.select("div.fund-top-holdings table.data-table tbody tr");
    List<WeightDto> topHoldings = new ArrayList<>();
    for (Element r: rows) {
      Element nameEl = r.selectFirst("tr td.label[data-label=Name:]");
      Element weightEl = r.selectFirst("tr td.weight[data-label=Weight:]");
      Element isinEl = r.selectFirst("tr td.weight[data-lable=ISIN:]");
      if (nameEl == null || weightEl == null) {
        continue;
      }

      Double value = null;
      value = ParsePercentage.parse(weightEl);
      String name = null;
      name = nameEl.text();
      String isin = null;
      if (isinEl != null) {
        isin = isinEl.text();
      }
      if (value != null && name != null) {
        WeightDto weightDto = WeightDto.builder()
            .name(name + (isin != null ? "ISIN: " + isin : ""))
            .value(value)
            .type(WeightType.HOLDING)
            .date(localDate)
            .fund(fundDto)
            .build();
        topHoldings.add(weightDto);
      }
    }
    return topHoldings;
  }

  private List<WeightDto> processSectorAllocation(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return new ArrayList<>();
    }
    Element fundSectorsEl = fundContentEl.select("div.fund-sector-breakdown").first();
    if (fundSectorsEl == null) {
      return new ArrayList<>();
    }

    Element asOfDateEl = fundContentEl.select("div.fund-sector-breakdown h4 span.date").first();

    LocalDate localDate = parseDateFromElement(asOfDateEl);
    if (localDate == null) {
      return new ArrayList<>();
    }

    Elements rows = fundSectorsEl
        .select("div.fund-sector-breakdown table.data-table tbody tr");
    List<WeightDto> sectors = new ArrayList<>();
    for (Element r: rows) {
      Element nameEl = r.selectFirst("tr td.label");
      Element weightEl = r.selectFirst("tr td.data");
      if (nameEl == null || weightEl == null) {
        continue;
      }
      Double value = ParsePercentage.parse(weightEl);
      String name = nameEl.text();
      if (value != null && name != null) {
        WeightDto weightDto = WeightDto.builder()
            .name(name)
            .value(value)
            .type(WeightType.SECTOR)
            .date(localDate)
            .fund(fundDto)
            .build();
        sectors.add(weightDto);
      }
    }
    return sectors;
  }

  private List<WeightDto> processGeographicalBreakdown(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return new ArrayList<>();
    }
    Element fundCountriesEl = fundContentEl.select("div.geographical-chart").first();
    if (fundCountriesEl == null) {
      return new ArrayList<>();
    }

    Element asOfDateEl = fundContentEl.select("div.main-content div.fund-data span.date").first();

    LocalDate localDate = parseDateFromElement(asOfDateEl);
    if (localDate == null) {
      return new ArrayList<>();
    }

    Elements rows = fundCountriesEl
        .select("div.main-content table.data-table tbody tr");
    List<WeightDto> countries = new ArrayList<>();
    for (Element r: rows) {
      Element nameEl = r.selectFirst("tr td.label");
      Element weightEl = r.selectFirst("tr td.data");
      if (nameEl == null || weightEl == null) {
        continue;
      }
      Double value = ParsePercentage.parse(weightEl);
      String name = nameEl.text();
      if (value != null && name != null) {
        WeightDto weightDto = WeightDto.builder()
            .name(name)
            .value(value)
            .type(WeightType.COUNTRY)
            .date(localDate)
            .fund(fundDto)
            .build();
        countries.add(weightDto);
      }
    }
    return countries;
  }

  private LocalDate parseDateFromElement(Element asOfDateEl) {
    LocalDate localDate = null;
    if (asOfDateEl != null) {
      String asOfDateString = asOfDateEl.text();
      String dateString = StringUtils.substringAfter(asOfDateString, "as of ");

      DateTimeFormatter f = DateTimeFormatter.ofPattern( "MMM dd yyyy");
      try {
        localDate = LocalDate.parse(dateString, f);
      } catch (DateTimeParseException e) {
        LOGGER.log(Level.SEVERE,
            String.format("cannot parse date of element %s, error: %s", asOfDateEl, e));
      }
    }
    return localDate;
  }

}
