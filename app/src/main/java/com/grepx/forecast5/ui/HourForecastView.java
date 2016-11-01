package com.grepx.forecast5.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.grepx.forecast5.R;
import com.grepx.forecast5.domain.HourForecast;

public class HourForecastView extends FrameLayout {

  private TextView temp;
  private TextView tempMin;
  private TextView tempMax;
  private String temperatureFormat;
  private String temperatureFormatMin;
  private String temperatureFormatMax;

  public HourForecastView(Context context) {
    super(context);
    init();
  }

  public HourForecastView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public HourForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.view_hour_forecast, this);
    temp = (TextView) findViewById(R.id.temp);
    tempMin = (TextView) findViewById(R.id.temp_min);
    tempMax = (TextView) findViewById(R.id.temp_max);

    temperatureFormat = getContext().getString(R.string.temperature_format);
    temperatureFormatMin = getContext().getString(R.string.temperature_format_min);
    temperatureFormatMax = getContext().getString(R.string.temperature_format_max);
  }

  public void setState(HourForecast hourForecast) {
    String temp = formatTemp(hourForecast.getTemp());
    String tempMin = formatTemp(hourForecast.getTempMin());
    String tempMax = formatTemp(hourForecast.getTempMax());

    tempMin = String.format(temperatureFormatMin, tempMin);
    tempMax = String.format(temperatureFormatMax, tempMax);

    this.temp.setText(temp);
    this.tempMin.setText(tempMin);
    this.tempMax.setText(tempMax);
  }

  private String formatTemp(float temp) {
    return String.format(temperatureFormat, String.valueOf(temp));
  }
}
