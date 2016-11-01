package com.grepx.forecast5.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.grepx.forecast5.R;
import com.grepx.forecast5.domain.DayForecast;
import com.grepx.forecast5.domain.HourForecast;
import org.threeten.bp.DayOfWeek;

public class DayForecastView extends FrameLayout {
  private static final String TAG = DayForecastView.class.getSimpleName();

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
    dayName.setText(getDayOfWeekString(dayForecast.getDayOfWeek()));

    hourListLayout.removeAllViews();
    for (HourForecast hourForecast : dayForecast.getHourForecasts()) {
      HourForecastView hourForecastView = new HourForecastView(getContext());
      hourForecastView.setState(hourForecast);
      hourListLayout.addView(hourForecastView);
    }
  }

  private String getDayOfWeekString(DayOfWeek dayOfWeek) {
    int stringResourceId;
    switch (dayOfWeek) {
      case MONDAY:
        stringResourceId = R.string.monday;
        break;
      case TUESDAY:
        stringResourceId = R.string.tuesday;
        break;
      case WEDNESDAY:
        stringResourceId = R.string.wednesday;
        break;
      case THURSDAY:
        stringResourceId = R.string.thursday;
        break;
      case FRIDAY:
        stringResourceId = R.string.friday;
        break;
      case SATURDAY:
        stringResourceId = R.string.saturday;
        break;
      case SUNDAY:
        stringResourceId = R.string.sunday;
        break;
      default:
        Log.e(TAG, "Unknown day of week");
        stringResourceId = R.string.sunday;
    }
    return getContext().getString(stringResourceId);
  }
}
