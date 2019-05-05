package com.phoenix.base

import android.os.Bundle

/**
 * @author mfyk
 * @date 2019/4/11
 */
abstract class BaseMVPFragment<P : BasePresenter<*>> : BaseFragment() {
    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenter()
    }

    protected abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}
