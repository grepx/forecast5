package com.grepx.forecast5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.grepx.forecast5.domain.DayForecast;
import com.grepx.forecast5.domain.ForecastService;
import com.grepx.forecast5.network.ForecastResponse;
import com.grepx.forecast5.network.ForecastServiceImpl;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  ForecastService forecastService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupDependencies();

    setContentView(R.layout.activity_main);

    forecastService.forecast("London")
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Action1<List<DayForecast>>() {
                     @Override public void call(List<DayForecast> dayForecasts) {
                       Log.d(TAG, "call: " + dayForecasts.get(0).getDayOfWeek());
                     }
                   }, new Action1<Throwable>() {
                     @Override public void call(Throwable throwable) {
                       Log.e(TAG, throwable.getMessage(), throwable);
                     }
                   });
  }

  private void setupDependencies() {
    setupNetwork();
  }

  private void setupNetwork() {
    forecastService = new ForecastServiceImpl();
  }
}
