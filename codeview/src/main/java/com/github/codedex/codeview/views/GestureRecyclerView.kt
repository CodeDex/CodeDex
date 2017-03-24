package com.github.codedex.codeview.views

import android.content.Context
import android.support.v4.widget.ScrollerCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Window
import java.lang.ref.WeakReference

/**
 * Created by fabianterhorst on 27.09.16.
 */

// Line 80, [...]!!.get()!!.decorView[...] instead of [...]!!.get()..decorView[...]

open class GestureRecyclerView(context: Context, attributes: AttributeSet) : RecyclerView(context, attributes), GestureDetector.OnGestureListener {

    var window: WeakReference<Window>? = null

    private val mScroller: ScrollerCompat = ScrollerCompat.create(context)

    private var gestureDetector = GestureDetector(context, this)

    init {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_CANCEL -> {
                //Stop any flinging in progress
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //Stop any flinging in progress
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
        }

        return super.onInterceptTouchEvent(event)
    }

    fun fling(velocityX: Int) {
        val dif = canScroll()
        val currX = translationX.toInt()
        mScroller.fling(currX, 0, velocityX, 0,
                -dif, 0,
                0, 0);
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val x = mScroller.currX
            val dif = canScroll()
            if (x < -dif) {
                translationX = -dif.toFloat()
                return
            }
            if (x > 0) {
                return
            }
            translationX = x.toFloat()
        }
    }

    private fun canScroll(): Int {
        val dif = width - window!!.get()!!.decorView.width
        if (dif <= 0) {
            return 0 //view is smaller then window, no need to scroll
        }
        return dif
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    private val SWIPE_MAX_OFF_PATH = 250

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        fling(velocityX.toInt())
        return true;
    }

    private var ignore = false

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, x: Float, y: Float): Boolean {
        val dif = canScroll()
        if (dif != 0 && x < SWIPE_MAX_OFF_PATH) {
            if (!ignore) {
                if (translationX - x < -dif) {
                    translationX = -dif.toFloat()
                    return true
                }
                if (translationX - x > 0) {
                    return true
                }
                translationX -= x
                ignore = true
            } else {
                ignore = false
            }
        }
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onLongPress(p0: MotionEvent?) {

    }
}