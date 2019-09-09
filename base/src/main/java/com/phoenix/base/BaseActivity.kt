package com.phoenix.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast

/**
 * @date 2019/4/11
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    /**
     * 设置LayoutId
     *
     * @return LayoutId
     */
    @LayoutRes
    protected abstract fun getContentId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentId())
        init()
    }

    protected abstract fun init()

    override fun toast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    override fun toast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
