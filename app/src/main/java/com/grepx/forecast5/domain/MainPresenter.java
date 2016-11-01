package com.grepx.forecast5.domain;

import android.util.Log;
import java.util.List;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainPresenter {
  private static final String TAG = MainPresenter.class.getSimpleName();

  private final ForecastService forecastService;
  private final Scheduler mainThread;
  private final MainView view;

  public MainPresenter(MainView view, ForecastService forecastService, Scheduler mainThread) {
    this.view = view;
    this.forecastService = forecastService;
    this.mainThread = mainThread;
  }

  public void doSearch(String placeName) {
    view.showLoading(true);
    forecastService.forecast(placeName)
                   .subscribeOn(Schedulers.io())
                   .observeOn(mainThread)
                   .subscribe(new Action1<List<DayForecast>>() {
                     @Override public void call(List<DayForecast> dayForecasts) {
                       processResult(dayForecasts);
                     }
                   }, new Action1<Throwable>() {
                     @Override public void call(Throwable throwable) {
                       processError(throwable);
                     }
                   });
  }

  private void processResult(List<DayForecast> dayForecasts) {
    view.showLoading(false);
    view.showResults(dayForecasts);
  }

  private void processError(Throwable throwable) {
    view.showLoading(false);
    view.showError();
    Log.e(TAG, throwable.getMessage(), throwable);
  }
}
