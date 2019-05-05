package com.phoenix.base

import android.os.Bundle

/**
 * @date 2019/2/26
 */
abstract class BaseMVPActivity<P : BasePresenter<*>> : BaseActivity() {
    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = initPresenter()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}
