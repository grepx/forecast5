package com.grepx.forecast5.domain.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtil {
  public static <T> Observable.Transformer<T, T> applyConfig(
      final SubscriptionConfig subscriptionConfig) {
    return new Observable.Transformer<T, T>() {
      @Override
      public Observable<T> call(Observable<T> observable) {
        return observable.subscribeOn(subscriptionConfig.getSubscribeOn())
                         .observeOn(subscriptionConfig.getObserveOn());
      }
    };
  }

  public static <T> Observable.Transformer<T, T> applySchedulers() {
    return new Observable.Transformer<T, T>() {
      @Override
      public Observable<T> call(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }
}
