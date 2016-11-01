package com.grepx.forecast5.domain;

import org.threeten.bp.LocalDateTime;

public class HourForecast {
  private final LocalDateTime time;
  private final int weatherCode;
  private final String iconUrl;
  private final float temp;
  private final float tempMin;
  private final float tempMax;

  public HourForecast(LocalDateTime time, int weatherCode, String iconUrl, float temp, float tempMin,
                     float tempMax) {
    this.time = time;
    this.weatherCode = weatherCode;
    this.iconUrl = iconUrl;
    this.temp = temp;
    this.tempMin = tempMin;
    this.tempMax = tempMax;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public int getWeatherCode() {
    return weatherCode;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public float getTemp() {
    return temp;
  }

  public float getTempMin() {
    return tempMin;
  }

  public float getTempMax() {
    return tempMax;
  }
}
