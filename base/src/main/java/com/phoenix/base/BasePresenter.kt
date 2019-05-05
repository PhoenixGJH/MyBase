package com.phoenix.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @date 2019/2/26
 */
abstract class BasePresenter<V : BaseView>(v: V) {
    protected lateinit var view: V

    private var compositeDisposable: CompositeDisposable?

    init {
        this.compositeDisposable = CompositeDisposable()
        attachView(v)
    }

    open fun attachView(v: V) {
        this.view = v
    }

    open fun detach() {
        compositeDisposable?.clear()
        compositeDisposable = null
    }

    fun addDisposable(d: Disposable) {
        compositeDisposable?.add(d)
    }
}
