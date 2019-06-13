package com.github.nigelab.rxjava2test;

import io.reactivex.*;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.nigelab.rxjava2test.FooBooActivity.*;

public class HelloWorld {
  public static void main(String[] args) {
    Flowable.just("Hello Flowable").subscribe(System.out::println);
    Observable.just("Hello Observable").subscribe(System.out::println);
    Single.just("Hello Single").subscribe(System.out::println);
    Observable.just("Hello concat1").concatWith(Observable.just("Hello concat2")).subscribe(System.out::println);

    AtomicInteger count = new AtomicInteger();

    Observable.range(1, 10)
        .doOnNext(ignored -> count.incrementAndGet())
        .ignoreElements()
        .andThen(Single.just(count.get()))
        .subscribe(System.out::println);

    Observable.range(1, 10)
        .doOnNext(ignored -> count.incrementAndGet())
        .ignoreElements()
        .andThen(Single.defer(() -> Single.just(count.get())))
        .subscribe(System.out::println);

    Observable.range(1, 10)
        .doOnNext(ignored -> count.incrementAndGet())
        .ignoreElements()
        .andThen(Single.fromCallable(() -> count.get()))
        .subscribe(System.out::println);

    Observable.just(1, 2, 3)
        .mergeWith(Observable.just(4, 5, 6))
        .subscribe(item -> System.out.println(item));
    Observable.just("f1", "f2", "f3")
        .mergeWith(Observable.just("f4", "f5", "f6"))
        .subscribe(item -> System.out.println(item));

    Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        emitter.setDisposable(Disposables.disposed());
        Arrays.asList("f1", "f2", "f3").forEach(emitter::onNext);
        emitter.onComplete();
      }
    }).mergeWith(Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        emitter.setDisposable(Disposables.disposed());
        Arrays.asList("f4", "f5", "f6").forEach(emitter::onNext);
        emitter.onComplete();
      }
    })).subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.err.println("MERGE::" + s);
      }
    });

    testActivity();
  }

  private static void testActivity() {
    FooActivity foo = new FooActivity();
    foo.subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.out.println(s);
      }
    });
    BooActivity boo = new BooActivity();
    boo.subscribe(System.out::println);

    foo.mergeWith(boo).subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.out.println("merge::" + s);
      }
    });
    foo.concatWith(boo).subscribe(new Consumer<String>() {
      @Override
      public void accept(String s) throws Exception {
        System.out.println("concat::" + s);
      }
    });

    FooBooActivity fooBoo = new FooBooActivity();
    fooBoo.subscribe(System.err::println);
    FooBooActivity concatFooBoo = FooBooActivity.just(CONCAT);
    concatFooBoo.subscribe(System.err::println);
    FooBooActivity mergeFooBoo = FooBooActivity.just(MERGE);
    mergeFooBoo.subscribe(System.err::println);
    FooBooActivity zipFooBoo = FooBooActivity.just(ZIP);
    zipFooBoo.subscribe(System.err::println);
  }
}
