package com.phoenix.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * @author mfyk
 * @date 2019/4/11
 */
abstract class BaseFragment : Fragment(), BaseView {

    /**
     * 设置LayoutId
     *
     * @return LayoutId
     */
    @LayoutRes
    protected abstract fun getContentId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    protected abstract fun init(view: View)

    override fun toast(@StringRes resId: Int) {
        val activity = activity
        if (activity != null) {
            Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
        }
    }

    override fun toast(text: CharSequence) {
        if (text == null) {
            return
        }
        val activity = activity
        if (activity != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }
}
