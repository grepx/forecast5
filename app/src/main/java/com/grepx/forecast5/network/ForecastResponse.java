package com.grepx.forecast5.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
  public City city;

  public static class City {
    public String name;
    public String country;
  }

  public List<Forecast> list;

  public static class Forecast {
    public long dt;

    public Main main;

    public static class Main {
      float temp;

      @SerializedName("temp_min")
      float tempMin;

      @SerializedName("temp_max")
      float tempMax;
    }

    public List<Weather> weather;

    public static class Weather {
      public int id;
      public String main;
      public String description;
      public String icon;
    }
  }
}