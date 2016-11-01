package com.grepx.forecast5.domain;

import java.util.List;
import org.threeten.bp.DayOfWeek;

public class DayForecast {
  private final DayOfWeek dayOfWeek;
  private final List<HourForecast> hourForecasts;

  public DayForecast(DayOfWeek dayOfWeek, List<HourForecast> hourForecasts) {
    this.dayOfWeek = dayOfWeek;
    this.hourForecasts = hourForecasts;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public List<HourForecast> getHourForecasts() {
    return hourForecasts;
  }
}
