package com.phoenix.base

import androidx.annotation.StringRes

/**
 * @date 2019/2/26
 */
interface BaseView {
    fun toast(text: CharSequence)

    fun toast(@StringRes resId: Int)
}
