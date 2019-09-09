package com.phoenix.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @date 2019/2/26
 */
abstract class BasePresenter<V : BaseView>(v: V) {
    protected var view: V = v

    private var compositeDisposable: CompositeDisposable?

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    open fun detach() {
        compositeDisposable?.clear()
        compositeDisposable = null
    }

    fun addDisposable(d: Disposable) {
        compositeDisposable?.add(d)
    }
}
