package com.grepx.forecast5.domain;

import com.grepx.forecast5.domain.util.SubscriptionConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;

public class MainPresenterTest {

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void doSearch_success() throws Exception {
    // configure test data
    final String placeName = "London";

    List<HourForecast> hourForecasts = new ArrayList<>();
    hourForecasts.add(new HourForecast(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                                       100,
                                       "http://grepx.com/test",
                                       12.0f,
                                       5.5f,
                                       15.5f));

    List<DayForecast> dayForecasts = new ArrayList<>();
    dayForecasts.add(new DayForecast(DayOfWeek.MONDAY, hourForecasts));

    Observable<List<DayForecast>> serviceResult = Observable.just(dayForecasts);

    // configure mocks
    MainView mainView = Mockito.mock(MainView.class);

    ForecastService forecastService = Mockito.mock(ForecastService.class);
    Mockito.when(forecastService.forecast(placeName)).thenReturn(serviceResult);

    // do test
    SubscriptionConfig subscriptionConfig = new SubscriptionConfig(Schedulers.immediate(),
                                                                   Schedulers.immediate());
    MainPresenter mainPresenter = new MainPresenter(mainView, forecastService, subscriptionConfig);
    mainPresenter.doSearch(placeName);

    // assertions
    Mockito.verify(mainView).showResults(dayForecasts);
    Mockito.verify(mainView).showLoading(false);
    Mockito.verify(mainView, never()).showError();
  }

  @Test
  public void doSearch_error() throws Exception {
    // configure test data
    final String placeName = "London";

    // service throws an exception
    Observable<List<DayForecast>> serviceResult = Observable.error(new RuntimeException());

    // configure mocks
    MainView mainView = Mockito.mock(MainView.class);

    ForecastService forecastService = Mockito.mock(ForecastService.class);
    Mockito.when(forecastService.forecast(placeName)).thenReturn(serviceResult);

    // do test
    SubscriptionConfig subscriptionConfig = new SubscriptionConfig(Schedulers.immediate(),
                                                                   Schedulers.immediate());
    MainPresenter mainPresenter = new MainPresenter(mainView, forecastService, subscriptionConfig);
    mainPresenter.doSearch(placeName);

    // assertions
    Mockito.verify(mainView, never()).showResults(ArgumentMatchers.<DayForecast>anyList());
    Mockito.verify(mainView).showLoading(false);
    Mockito.verify(mainView).showError();
  }
}