package com.github.nigelab.rxjava2test;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;

public class FooBooActivity extends Observable<String> {
  public static final String CONCAT = "concat";
  public static final String ZIP = "zip";
  public static final String MERGE = "merge";
  private String operator;

  public FooBooActivity() {
  }

  private FooBooActivity(String operator) {
    this.operator = operator;
  }

  public static FooBooActivity just(String operator) {
    return new FooBooActivity(operator);
  }

  @Override
  protected void subscribeActual(Observer<? super String> observer) {
    observer.onSubscribe(Disposables.disposed());
    FooActivity foo = new FooActivity();
    BooActivity boo = new BooActivity();
//    foo.concatWith(boo).subscribe(observer);
    getStringObservable(foo, boo).startWith("----- " + operator + " foo and boo -----")
        .subscribe(new Consumer<String>() {
          @Override
          public void accept(String s) throws Exception {
            observer.onNext("FooBoo::" + operator + "::" + s);
          }
        });
    observer.onComplete();
  }

  private Observable<String> getStringObservable(FooActivity foo, BooActivity boo) {
    if (CONCAT.equals(operator)) {
      return foo.concatWith(boo);
    }
    if (MERGE.equals(operator)) {
      return foo.mergeWith(boo);
    }
    if (ZIP.equals(operator)) {
      return foo.zipWith(boo, (f, b) -> f + "/" + b);
    }
    return Observable.empty();
  }
}
