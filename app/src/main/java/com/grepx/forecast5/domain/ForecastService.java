package com.grepx.forecast5.domain;

import java.util.List;
import rx.Observable;

public interface ForecastService {
  Observable<List<DayForecast>> forecast(String placeName);
}
