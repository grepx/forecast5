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
import com.grepx.forecast5.domain.MainPresenter;
import com.grepx.forecast5.domain.MainView;
import com.grepx.forecast5.network.ForecastServiceImpl;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainView {
  private static final String TAG = MainActivity.class.getSimpleName();

  private SwipeRefreshLayout swipeRefreshLayout;
  private EditText searchEditText;
  private ViewGroup dayListLayout;

  private MainPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    injectDependencies();

    setupView();
  }

  private void injectDependencies() {
    // in a production architecture I'd use Dagger to do this
    ForecastServiceImpl forecastService = new ForecastServiceImpl();
    presenter = new MainPresenter(this, forecastService, AndroidSchedulers.mainThread());
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
          presenter.doSearch(searchEditText.getText().toString());
          return true;
        }
        return false;
      }
    });

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        presenter.doSearch(searchEditText.getText().toString());
      }
    });

    dayListLayout = (ViewGroup) findViewById(R.id.day_list_layout);
  }

  public void showResults(List<DayForecast> dayForecasts) {
    dayListLayout.removeAllViews();
    for (DayForecast dayForecast : dayForecasts) {
      DayForecastView dayForecastView = new DayForecastView(this);
      dayForecastView.setState(dayForecast);
      dayListLayout.addView(dayForecastView);
    }
  }

  public void showError() {
    Toast.makeText(this, R.string.error_text, Toast.LENGTH_SHORT).show();
  }

  public void showLoading(boolean loading) {
    swipeRefreshLayout.setRefreshing(loading);
  }
}