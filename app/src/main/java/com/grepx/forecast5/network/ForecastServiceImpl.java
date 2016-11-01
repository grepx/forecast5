package com.grepx.forecast5.network;

import com.grepx.forecast5.domain.DayForecast;
import com.grepx.forecast5.domain.ForecastService;
import com.grepx.forecast5.domain.HourForecast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

public class ForecastServiceImpl implements ForecastService {

  OpenWeatherApi openWeatherApi;

  public ForecastServiceImpl() {
    // set up retrofit
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
          @Override public Response intercept(Chain chain) throws IOException {
            HttpUrl originalUrl = chain.request().url();
            HttpUrl url = originalUrl.newBuilder()
                                     .addQueryParameter("mode", "json")
                                     .addQueryParameter("appid", OpenWeatherConstants.appId)
                                     .build();

            Request.Builder requestBuilder = chain.request().newBuilder().url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
          }
        })
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(OpenWeatherConstants.apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(httpClient)
        .build();

    openWeatherApi = retrofit.create(OpenWeatherApi.class);
  }

  @Override public Observable<List<DayForecast>> forecast(String placeName) {
    return openWeatherApi
        .forecast(placeName)
        // map the network parsing model to the domain model
        .map(new Func1<ForecastResponse, List<DayForecast>>() {
          @Override public List<DayForecast> call(ForecastResponse forecastResponse) {
            return mapDayForcasts(forecastResponse);
          }
        });
  }

  private List<DayForecast> mapDayForcasts(ForecastResponse forecastResponse) {
    List<DayForecast> dayForecasts = new ArrayList<>();
    DayOfWeek currentDay = null;
    List<HourForecast> hourForecasts = new ArrayList<>();

    for (ForecastResponse.Forecast forecast : forecastResponse.list) {
      LocalDateTime dateTime = mapLocalDateTime(forecast.dt);
      if (currentDay != null && currentDay != dateTime.getDayOfWeek()) {
        // finished records for this day
        dayForecasts.add(new DayForecast(currentDay, hourForecasts));
        hourForecasts = new ArrayList<>();
      }
      currentDay = dateTime.getDayOfWeek();

      hourForecasts.add(mapHourForecast(forecast));
    }
    dayForecasts.add(new DayForecast(currentDay, hourForecasts));

    return dayForecasts;
  }

  private HourForecast mapHourForecast(ForecastResponse.Forecast forecast) {
    ForecastResponse.Forecast.Weather weather = forecast.weather.get(0);
    return new HourForecast(
        mapLocalDateTime(forecast.dt),
        weather.id,
        String.format(OpenWeatherConstants.iconUrlTemplate, weather.icon),
        forecast.main.temp,
        forecast.main.tempMin,
        forecast.main.tempMax
    );
  }

  private LocalDateTime mapLocalDateTime(long dt) {
    // todo: figure out correct zone offset value
    return LocalDateTime.ofEpochSecond(dt, 0, ZoneOffset.UTC);
  }
}
