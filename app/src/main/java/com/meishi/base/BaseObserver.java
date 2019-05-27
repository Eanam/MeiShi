package com.meishi.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

;

/**
 * 封装Oberver，使得我们在使用时只需重写两个方法
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        error(e);
    }

    @Override
    public void onComplete() {
    }

    public abstract void success(T t);

    public abstract void error(Throwable e);
}
