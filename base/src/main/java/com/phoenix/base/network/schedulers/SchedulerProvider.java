package com.phoenix.base.network.schedulers;

import androidx.annotation.NonNull;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulerProvider {

    private volatile static SchedulerProvider instance;

    private SchedulerProvider() {
    }

    public static SchedulerProvider getInstance() {
        if (instance == null) {
            synchronized (SchedulerProvider.class) {
                if (instance == null) {
                    instance = new SchedulerProvider();
                }
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @NonNull
    @Override
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(io())
                .observeOn(ui());
    }
}
