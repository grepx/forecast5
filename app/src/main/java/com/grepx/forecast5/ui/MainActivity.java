package com.grepx.forecast5.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
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

  ForecastService forecastService;

  EditText searchEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    injectDependencies();

    setupView();

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
          Log.d(TAG, "onEditorAction: " + searchEditText.getText());
          return true;
        }
        return false;
      }
    });
  }
}