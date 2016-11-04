package com.grepx.forecast5.domain.util;

import rx.Scheduler;

public class SubscriptionConfig {
  private final Scheduler subscribeOn;
  private final Scheduler observeOn;

  public SubscriptionConfig(Scheduler subscribeOn, Scheduler observeOn) {
    this.subscribeOn = subscribeOn;
    this.observeOn = observeOn;
  }

  public Scheduler getSubscribeOn() {
    return subscribeOn;
  }

  public Scheduler getObserveOn() {
    return observeOn;
  }
}
