package com.grepx.forecast5.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherApi {
  @GET("forecast") Observable<ForecastResponse> forecast(@Query("q") String placeName);
}
