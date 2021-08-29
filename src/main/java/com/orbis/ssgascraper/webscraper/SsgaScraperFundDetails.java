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

  public FundDto scrapFundDetails(String fundLink, FundDto fund) {
    JBrowserDriver driver = BrowserDriver.buildDriver();
    try {
      LOGGER.log(Level.INFO, "Driver creating successful");
      driver.get(fundLink);
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

    fundDto = processDescription(document, fundDto);
    fundDto = processTopHoldings(document, fundDto);
    fundDto = processSectorAllocation(document, fundDto);
    fundDto = processGeographicalBreakdown(document, fundDto);
    LOGGER.log(Level.INFO, "Finished Processing SsgaScraper Fund document");
    return fundDto;
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

  private FundDto processTopHoldings(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return fundDto;
    }
    Element fundTopHoldingsEl = fundContentEl.select("div.fund-top-holdings").first();
    if (fundTopHoldingsEl != null) {
      Element asOfDateEl = fundContentEl
          .select("div.fund-top-holdings h3 span.date")
          .first();
      LocalDate localDate = null;
      if (asOfDateEl != null) {
        String asOfDateString = asOfDateEl.text();
        String dateString = StringUtils.substringAfter(asOfDateString, "as of ");

        DateTimeFormatter f = DateTimeFormatter.ofPattern( "MMM dd yyyy");
        try {
          localDate = LocalDate.parse(dateString, f);
        } catch (DateTimeParseException e) {
          LOGGER.log(Level.SEVERE, String.format("cannot parse date, %s", e));
        }
      }
      if (localDate != null) {
        Elements rows = fundTopHoldingsEl
            .select("div.fund-top-holdings table.data-table tbody tr");
        List<WeightDto> topHoldings = new ArrayList<>();
        for (Element r: rows) {
          Element nameEl = r.selectFirst("tr td.label[data-label=Name:]");
          Element weightEl = r.selectFirst("tr td.weight[data-label=Weight:]");
          Double weight = ParsePercentage.parse(weightEl);
          String name = nameEl.text();
          if (weight != null && name != null) {
            WeightDto weightDto = WeightDto.builder()
                .name(name)
                .weight(weight)
                .type(WeightType.HOLDING)
                .date(localDate)
                .build();
            topHoldings.add(weightDto);
          }
        }
        if (topHoldings.size() != 0) {
          fundDto.setHoldingWeights(topHoldings);
        }
      }
    }
    return fundDto;
  }

  private FundDto processSectorAllocation(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return fundDto;
    }
    Element fundSectorsEl = fundContentEl.select("div.fund-sector-breakdown").first();
    if (fundSectorsEl == null) {
      return fundDto;
    }

    Element asOfDateEl = fundContentEl.select("div.fund-sector-breakdown h4 span.date").first();

    LocalDate localDate = parseDateFromElement(asOfDateEl);
    if (localDate == null) {
      return fundDto;
    }

    Elements rows = fundSectorsEl
        .select("div.fund-sector-breakdown table.data-table tbody tr");
    List<WeightDto> sectors = new ArrayList<>();
    for (Element r: rows) {
      Element nameEl = r.selectFirst("tr td.label");
      Element weightEl = r.selectFirst("tr td.data");
      Double weight = ParsePercentage.parse(weightEl);
      String name = nameEl.text();
      if (weight != null && name != null) {
        WeightDto weightDto = WeightDto.builder()
            .name(name)
            .weight(weight)
            .type(WeightType.SECTOR)
            .date(localDate)
            .build();
        sectors.add(weightDto);
      }
    }
    if (sectors.size() != 0) {
      fundDto.setSectorWeights(sectors);
    }
    return fundDto;
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

  private FundDto processGeographicalBreakdown(Document document, FundDto fundDto) {
    Element fundContentEl = fundContent(document);
    if (fundContentEl == null) {
      return fundDto;
    }
    Element fundCountriesEl = fundContentEl.select("div.geographical-chart").first();
    if (fundCountriesEl == null) {
      return fundDto;
    }

    Element asOfDateEl = fundContentEl.select("div.main-content div.fund-data span.date").first();

    LocalDate localDate = parseDateFromElement(asOfDateEl);
    if (localDate == null) {
      return fundDto;
    }

    Elements rows = fundCountriesEl
        .select("div.main-content table.data-table tbody tr");
    List<WeightDto> countries = new ArrayList<>();
    for (Element r: rows) {
      Element nameEl = r.selectFirst("tr td.label");
      Element weightEl = r.selectFirst("tr td.data");
      Double weight = ParsePercentage.parse(weightEl);
      String name = nameEl.text();
      if (weight != null && name != null) {
        WeightDto weightDto = WeightDto.builder()
            .name(name)
            .weight(weight)
            .type(WeightType.COUNTRY)
            .date(localDate)
            .build();
        countries.add(weightDto);
      }
    }
    if (countries.size() != 0) {
      fundDto.setCountryWeights(countries);
    }
    return fundDto;
  }

}
