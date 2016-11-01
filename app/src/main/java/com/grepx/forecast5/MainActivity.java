package com.grepx.forecast5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.grepx.forecast5.network.ForecastResponse;
import com.grepx.forecast5.network.OpenWeatherApi;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  OpenWeatherApi openWeatherApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupDependencies();

    setContentView(R.layout.activity_main);

    openWeatherApi.forecast("London")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ForecastResponse>() {
          @Override public void call(ForecastResponse forecastResponse) {
            Log.d(TAG, "call: " + forecastResponse.city.name);
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
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
          @Override public Response intercept(Chain chain) throws IOException {
            HttpUrl originalUrl = chain.request().url();
            HttpUrl url = originalUrl.newBuilder()
                                     .addQueryParameter("mode", "json")
                                     .addQueryParameter("appid", "65756037449a32d4c723e7b9e6bb40dd")
                                     .build();

            Request.Builder requestBuilder = chain.request().newBuilder().url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
          }
        })
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(httpClient)
        .build();

    openWeatherApi = retrofit.create(OpenWeatherApi.class);
  }
}
