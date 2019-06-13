package com.github.nigelab.rxjava2test;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;

import java.util.Arrays;
import java.util.List;

public class BooActivity extends Observable<String> {
  private List<String> values = Arrays.asList("boo started", BooActivity.class.getCanonicalName(), BooActivity.class.getSimpleName(), "boo end");

  @Override
  protected void subscribeActual(Observer<? super String> observer) {
    observer.onSubscribe(Disposables.disposed());
    values.forEach(observer::onNext);
    observer.onComplete();
  }
}
