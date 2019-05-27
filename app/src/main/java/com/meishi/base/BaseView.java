package com.meishi.base;

import io.reactivex.subjects.BehaviorSubject;

public interface BaseView extends IView {

    /**
     * 获取Lifecycle对象，用于Activity  onDestroy时取消观察者的订阅。
     */
    <R> BehaviorSubject<R> getLifeCycleSubject();
}
