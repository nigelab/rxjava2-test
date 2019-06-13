package com.github.nigelab.rxjava2test;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;

import java.util.Arrays;
import java.util.List;

public class FooActivity extends Observable<String> {

  private List<String> values = Arrays.asList("foo started", FooActivity.class.getCanonicalName(), FooActivity.class.getName(), "foo end");

  @Override
  protected void subscribeActual(Observer<? super String> observer) {
    observer.onSubscribe(Disposables.disposed());
    values.forEach(observer::onNext);
    observer.onComplete();
  }

}
