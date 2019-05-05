package com.phoenix.base

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @date 2019/3/4
 */
abstract class OnResponseListener<T>(protected var presenter: BasePresenter<*>, protected var view: BaseView) : Observer<T> {

    override fun onSubscribe(d: Disposable) {
        presenter.addDisposable(d)
    }

    override fun onNext(t: T) {
        onResponse(t)
    }

    override fun onError(e: Throwable) {
        onResponseError()
    }

    internal abstract fun onResponse(t: T)

    internal abstract fun onResponseError()
}
