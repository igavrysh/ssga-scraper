package com.orbis.ssgascraper.util;

import org.jsoup.nodes.Element;

public class ParsePercentage {

  public static Double parse(String percentageString) {
    return Double.parseDouble(percentageString.replace("%", "")) / 100;
  }

  public static Double parse(Element element) {
    return ParsePercentage.parse(element.text());
  }

}
