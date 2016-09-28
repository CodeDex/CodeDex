package com.github.codedex.codeview.views

import android.content.Context
import android.support.v4.widget.ScrollerCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.*
import java.lang.ref.WeakReference

/**
 * Created by fabianterhorst on 27.09.16.
 */

class GestureRecyclerView(context: Context, attributes: AttributeSet) : RecyclerView(context, attributes), GestureDetector.OnGestureListener {

    var window: WeakReference<Window>? = null

    private var mDragging = false

    private var mTouchSlop: Int = 0

    private val mScroller: ScrollerCompat = ScrollerCompat.create(context)

    private var gestureDetector = GestureDetector(context, this)

    private var mVelocityTracker = VelocityTracker.obtain();

    private var mMaximumVelocity: Int = 0
    private var mMinimumVelocity: Int = 0

    init {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        mVelocityTracker.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_CANCEL -> {
                mDragging = false
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
        var dif = canScroll()
        val movingRight = velocityX > 0
        val movingLeft = velocityX < 0
        var currX = translationX.toInt()
        /*if(movingLeft) {
            currX = Math.abs(currX)
            dif = Math.abs(dif)
        } else if (movingRight) {
            currX *= (-1)
            dif *= (-1)
        }*/
        var maxX = -dif
        Log.d("fling", "with translationX:" +currX.toString() + " and with vel " + velocityX.toString() + " until " + (-dif).toString())
        mScroller.fling(currX, 0, velocityX, 0,
                -dif, 0,
                0, 0);
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            var x = mScroller.currX
            Log.d("x cc", x.toString())
            val dif = canScroll()
            if (x < -dif) {
                translationX = -dif.toFloat()
                return
            }
            if (x > 0) {
                return
            }
            //x = clamp(Math.abs(x), window!!.get().decorView.width, width) * (-1)
            Log.d("fling", "x " + x.toString())
            translationX = x.toFloat()
        }
    }

    private fun clamp(n: Int, my: Int, child: Int): Int {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0//he should get a difference
        }
        if (my + n > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my
        }
        return n
    }

    private fun clampNeg(n: Int, my: Int, child: Int): Int {
        if (my <= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0
        }
        if (my - n < child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child + my
        }
        return n
    }

    private fun canScroll(): Int {
        val dif = width - window!!.get().decorView.width
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

    //private var ignoreFling = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        //if(!ignoreFling) {
        Log.d("fling", "fling")
            fling(velocityX.toInt())
        /*    ignoreFling = true
        } else {
            ignoreFling = false
        }*/
        return true;
    }

    private var ignore = false

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, x: Float, y: Float): Boolean {
        val dif = canScroll()
        if (dif != 0 && x < SWIPE_MAX_OFF_PATH) {
            if (!ignore) {
                Log.d("scroll", x.toString() + " " + y.toString())
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