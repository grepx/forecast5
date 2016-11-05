package com.grepx.forecast5.domain;

import com.grepx.forecast5.domain.util.SubscriptionConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainPresenterTest {

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void doSearch_success() throws Exception {
    final String placeName = "London";

    MainView mainView = Mockito.mock(MainView.class);
    ForecastService forecastService = Mockito.mock(ForecastService.class);
    SubscriptionConfig subscriptionConfig = new SubscriptionConfig(Schedulers.immediate(),
                                                                   Schedulers.immediate());

    List<HourForecast> hourForecasts = new ArrayList<>();
    hourForecasts.add(new HourForecast(LocalDateTime.ofEpochSecond(1000, 0, ZoneOffset.UTC),
                                       100,
                                       "http://google.com/logo",
                                       12.0f,
                                       5.5f,
                                       15.5f));

    List<DayForecast> dayForecasts = new ArrayList<>();
    dayForecasts.add(new DayForecast(DayOfWeek.MONDAY, hourForecasts));

    Observable<List<DayForecast>> serviceResult = Observable.just(dayForecasts);

    Mockito.when(forecastService.forecast(placeName)).thenReturn(serviceResult);

    MainPresenter mainPresenter = new MainPresenter(mainView, forecastService, subscriptionConfig);
    mainPresenter.doSearch(placeName);

    Mockito.verify(mainView).showResults(dayForecasts);
    Mockito.verify(mainView).showLoading(false);
  }
}