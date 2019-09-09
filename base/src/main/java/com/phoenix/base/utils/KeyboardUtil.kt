package com.phoenix.base.utils

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * 键盘工具类
 */
object KeyboardUtil {

    /**
     * 监听Keyboard弹出，并将View移动到键盘上方
     * @param baseView 需要监听的，键盘弹出的View
     * @param upperView 需要显示在键盘上方的View
     * @param listener baseView上已经设置的监听器，避免重复设置
     */
    fun addOnGlobalLayoutListener(baseView: View, upperView: View, listener: ViewTreeObserver.OnGlobalLayoutListener?)
            : ViewTreeObserver.OnGlobalLayoutListener {
        var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

        if (listener != null) {
            globalLayoutListener = listener
        } else {
            globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                private var sc: IntArray? = null
                private var scrollHeight: Int = 0

                override fun onGlobalLayout() {
                    val r = Rect()
                    baseView.getWindowVisibleDisplayFrame(r) //这样获得的r就是base没有被挡住可见的部分
                    if (sc == null) {
                        sc = IntArray(2)
                        upperView.getLocationOnScreen(sc) //数组里面两个值sc[0],sc[1]分别是对应控件在xy两轴的距离
                    }
                    //r.top 是状态栏高度
                    val screenHeight = baseView.rootView.height
                    val softHeight = screenHeight - r.bottom

                    if (softHeight > 140) {
                        //当输入法高度大于140判定为输入法打开了  设置大点，有虚拟键的会超过100
                        scrollHeight = sc!![1] + upperView.height - (screenHeight - softHeight) + 10//可以加个5dp的距离这样不想被挡住的最后一个布局不会挨着输入法
                        if (baseView.scrollY != scrollHeight && scrollHeight > 0) {
                            scrollToPos(0, scrollHeight, baseView)
                        }
                    } else {//否则判断为输入法隐藏了
                        if (baseView.scrollY != 0) {
                            scrollToPos(scrollHeight, 0, baseView)
                        }
                    }
                }

            }
        }

        baseView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        return globalLayoutListener
    }

    /**
     * 取消监听布局变化
     */
    fun removeOnGlobalLayoutListener(baseView: View, globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) {
        baseView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     * 移动布局
     */
    private fun scrollToPos(start: Int, end: Int, view: View) {
        val animator = ValueAnimator.ofInt(start, end)
        animator.duration = 250
        //延迟一段时间再执行动画是为了避免显示下面的无关View，具体效果可以注释该代码查看
        animator.startDelay = 50
        animator.addUpdateListener { animation -> view.scrollTo(0, animation.animatedValue as Int) }
        animator.start()
    }
}