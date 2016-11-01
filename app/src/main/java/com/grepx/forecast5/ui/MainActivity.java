package com.grepx.forecast5.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.grepx.forecast5.R;
import com.grepx.forecast5.domain.DayForecast;
import com.grepx.forecast5.domain.ForecastService;
import com.grepx.forecast5.network.ForecastServiceImpl;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  private ForecastService forecastService;

  private SwipeRefreshLayout swipeRefreshLayout;
  private EditText searchEditText;
  private ViewGroup dayListLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    injectDependencies();

    setupView();
  }

  private void injectDependencies() {
    // in a production architecture I'd use Dagger to do this
    forecastService = new ForecastServiceImpl();
  }

  private void setupView() {
    setContentView(R.layout.activity_main);

    searchEditText = (EditText) findViewById(R.id.search);

    searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            actionId == EditorInfo.IME_ACTION_DONE ||
            event != null &&
            event.getAction() == KeyEvent.ACTION_DOWN &&
            event.getKeyCode() == KeyEvent.KEYCODE_ENTER
            ) {
          doSearch(searchEditText.getText().toString());
          return true;
        }
        return false;
      }
    });

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        doSearch(searchEditText.getText().toString());
      }
    });

    dayListLayout = (ViewGroup) findViewById(R.id.day_list_layout);
  }

  private void doSearch(String placeName) {
    showLoading(true);
    forecastService.forecast(placeName)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Action1<List<DayForecast>>() {
                     @Override public void call(List<DayForecast> dayForecasts) {
                       showLoading(false);
                       showResults(dayForecasts);
                     }
                   }, new Action1<Throwable>() {
                     @Override public void call(Throwable throwable) {
                       showLoading(false);
                       showError();
                       Log.e(TAG, throwable.getMessage(), throwable);
                     }
                   });
  }

  private void showResults(List<DayForecast> dayForecasts) {
    dayListLayout.removeAllViews();
    for (DayForecast dayForecast : dayForecasts) {
      DayForecastView dayForecastView = new DayForecastView(this);
      dayForecastView.setState(dayForecast);
      dayListLayout.addView(dayForecastView);
    }
  }

  private void showError() {
    Toast.makeText(this, R.string.error_text, Toast.LENGTH_SHORT).show();
  }

  private void showLoading(boolean loading) {
    swipeRefreshLayout.setRefreshing(loading);
  }
}