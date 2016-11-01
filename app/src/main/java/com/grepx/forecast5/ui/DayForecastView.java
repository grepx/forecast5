package com.grepx.forecast5.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.grepx.forecast5.R;
import com.grepx.forecast5.domain.DayForecast;
import com.grepx.forecast5.domain.HourForecast;

public class DayForecastView extends FrameLayout {

  private TextView dayName;
  private ViewGroup hourListLayout;

  public DayForecastView(Context context) {
    super(context);
    init();
  }

  public DayForecastView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DayForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.view_day_forecast, this);
    dayName = (TextView) findViewById(R.id.day_name);
    hourListLayout = (ViewGroup) findViewById(R.id.hour_list_layout);
  }

  public void setState(DayForecast dayForecast) {
    // todo: do this properly with i18n etc.
    dayName.setText(dayForecast.getDayOfWeek().toString());

    hourListLayout.removeAllViews();
    for (HourForecast hourForecast : dayForecast.getHourForecasts()) {
      HourForecastView hourForecastView = new HourForecastView(getContext());
      hourForecastView.setState(hourForecast);
      hourListLayout.addView(hourForecastView);
    }
  }
}
