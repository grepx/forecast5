package com.grepx.forecast5.domain;

import java.util.List;

public interface MainView {
  void showResults(List<DayForecast> dayForecasts);

  void showError();

  void showLoading(boolean loading);
}
