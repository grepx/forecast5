package com.grepx.forecast5.domain;

import android.util.Log;
import com.grepx.forecast5.domain.util.RxUtil;
import com.grepx.forecast5.domain.util.SubscriptionConfig;
import java.util.List;
import rx.functions.Action1;

public class MainPresenter {
  private static final String TAG = MainPresenter.class.getSimpleName();

  private final ForecastService forecastService;
  private final SubscriptionConfig subscriptionConfig;
  private final MainView view;

  public MainPresenter(MainView view, ForecastService forecastService, SubscriptionConfig subscriptionConfig) {
    this.view = view;
    this.forecastService = forecastService;
    this.subscriptionConfig = subscriptionConfig;
  }

  public void doSearch(String placeName) {
    view.showLoading(true);
    forecastService.forecast(placeName)
                   .compose(RxUtil.<List<DayForecast>>applyConfig(subscriptionConfig))
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
