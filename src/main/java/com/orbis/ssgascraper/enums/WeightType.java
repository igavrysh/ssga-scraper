package com.orbis.ssgascraper.enums;

public enum WeightType {
  COUNTRY("COUNTRY_WEIGHT"),
  SECTOR("SECTOR_WEIGHT"),
  HOLDING("HOLDING_WEIGHT");

  private String code;

  WeightType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}