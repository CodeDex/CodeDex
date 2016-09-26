package com.github.codedex.codeview.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewConfiguration
import android.widget.HorizontalScrollView
import android.widget.Scroller

/**
 * @class BidirectionalScrollView
 *
 * Combines vertical & horizontal scroll to implement bidirectional
 * scrolling behavior (like a map view, for example).
 *
 */
class BidirectionalScrollView : HorizontalScrollView {

    private var currentX = 0
    private var currentY = 0

    private var isBeingDragged = false

    private var scroller: Scroller = Scroller(context)

    private var mLastMotionY: Float = 0F
    private var mLastMotionX: Float = 0F

    private var mTouchSlop: Int = 0

    private var mVelocityTracker: VelocityTracker? = null

    private var mMinimumVelocity: Int = 0
    private var mMaximumVelocity: Int = 0

    private var mTwoDScrollViewMovedFocus: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where mScrollX/Y is different from what the app
            //         thinks it is.
            //
            val oldX = scrollX
            val oldY = scrollY
            val x = scroller.currX
            val y = scroller.currY
            if (childCount > 0) {
                val child = getChildAt(0)
                scrollTo(clamp(x, width - paddingRight - paddingLeft, child.width),
                        clamp(y, height - paddingBottom - paddingTop, child.height))
            } else {
                scrollTo(x, y)
            }
            if (oldX != scrollX || oldY != scrollY) {
                onScrollChanged(scrollX, scrollY, oldX, oldY)
            }

            // Keep on drawing until the animation has finished.
            postInvalidate()
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {

        if (ev.action == MotionEvent.ACTION_DOWN && ev.edgeFlags != 0) {
            // Don't handle edge touches immediately -- they may actually belong to one of our
            // descendants.
            return false
        }

        if (!canScroll()) {
            return false
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(ev)

        val action = ev.action
        val y = ev.y
        val x = ev.x

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                /*
       * If being flinged and user touches, stop the fling. isFinished
       * will be false if being flinged.
       */
                if (!scroller.isFinished()) {
                    scroller.abortAnimation()
                }

                // Remember where the motion event started
                mLastMotionY = y
                mLastMotionX = x
            }
            MotionEvent.ACTION_MOVE -> {
                // Scroll to follow the motion event
                var deltaX = (mLastMotionX - x).toInt()
                var deltaY = (mLastMotionY - y).toInt()
                mLastMotionX = x
                mLastMotionY = y

                if (deltaX < 0) {
                    if (scrollX < 0) {
                        deltaX = 0
                    }
                } else if (deltaX > 0) {
                    val rightEdge = width - paddingRight
                    val availableToScroll = getChildAt(0).right - scrollX - rightEdge
                    if (availableToScroll > 0) {
                        deltaX = Math.min(availableToScroll, deltaX)
                    } else {
                        deltaX = 0
                    }
                }
                if (deltaY < 0) {
                    if (scrollY < 0) {
                        deltaY = 0
                    }
                } else if (deltaY > 0) {
                    val bottomEdge = height - paddingBottom
                    val availableToScroll = getChildAt(0).bottom - scrollY - bottomEdge
                    if (availableToScroll > 0) {
                        deltaY = Math.min(availableToScroll, deltaY)
                    } else {
                        deltaY = 0
                    }
                }
                if (deltaY != 0 || deltaX != 0)
                    scrollBy(deltaX, deltaY)
            }
            MotionEvent.ACTION_UP -> {
                val velocityTracker = mVelocityTracker
                velocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialXVelocity = velocityTracker.xVelocity.toInt()
                val initialYVelocity = velocityTracker.yVelocity.toInt()
                if (Math.abs(initialXVelocity) + Math.abs(initialYVelocity) > mMinimumVelocity && childCount > 0) {
                    fling(-initialXVelocity, -initialYVelocity)
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                }
            }
        }
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        /*
   * This method JUST determines whether we want to intercept the motion.
   * If we return true, onMotionEvent will be called and we do the actual
   * scrolling there.
   *
   * Shortcut the most recurring case: the user is in the dragging
   * state and he is moving his finger.  We want to intercept this
   * motion.
   */
        val action = ev.action
        if (action == MotionEvent.ACTION_MOVE && isBeingDragged) {
            return true
        }

        if (!canScroll()) {
            isBeingDragged = false
            return false
        }
        val y = ev.y
        val x = ev.x
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                /*
       * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
       * whether the user has moved far enough from his original down touch.
       */
                /*
       * Locally do absolute value. mLastMotionY is set to the y value
       * of the down event.
       */
                val yDiff = Math.abs(y - mLastMotionY).toInt()
                val xDiff = Math.abs(x - mLastMotionX).toInt()
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    isBeingDragged = true
                }
            }

            MotionEvent.ACTION_DOWN -> {
                /* Remember location of down touch */
                mLastMotionY = y
                mLastMotionX = x

                /*
       * If being flinged and user touches the screen, initiate drag;
       * otherwise don't.  mScroller.isFinished should be false when
       * being flinged.
       */
                isBeingDragged = !scroller.isFinished
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->
                /* Release the drag */
                isBeingDragged = false
        }

        /*
   * The only time we want to intercept motion events is if we are in the
   * drag mode.
   */
        return isBeingDragged
    }

    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        val childWidthMeasureSpec = makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val childHeightMeasureSpec = makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun measureChildWithMargins(child: View,
                                         parentWidthMeasureSpec: Int, widthUsed: Int,
                                         parentHeightMeasureSpec: Int, heightUsed: Int) {
        val params = child.layoutParams as MarginLayoutParams

        val childWidthMeasureSpec = makeMeasureSpec(
                params.leftMargin + params.rightMargin, MeasureSpec.UNSPECIFIED)
        val childHeightMeasureSpec = makeMeasureSpec(
                params.topMargin + params.bottomMargin, MeasureSpec.UNSPECIFIED)

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    /**
     * @return Returns true this ScrollView can be scrolled
     */
    private fun canScroll(): Boolean {
        val child = getChildAt(0)
        if (child != null) {
            val childHeight = child.height
            val childWidth = child.width
            return height < childHeight + paddingTop + paddingBottom || width < childWidth + paddingLeft + paddingRight
        }
        return false
    }

    /**
     * Fling the scroll view

     * @param velocityY The initial velocity in the Y direction. Positive
     * *                  numbers mean that the finger/curor is moving down the screen,
     * *                  which means we want to scroll towards the top.
     */
    fun fling(velocityX: Int, velocityY: Int) {
        if (childCount > 0) {
            val height = height - paddingBottom - paddingTop
            val bottom = getChildAt(0).height
            val width = width - paddingRight - paddingLeft
            val right = getChildAt(0).width

            scroller.fling(scrollX, scrollY, velocityX, velocityY, 0, right - width, 0, bottom - height)

            val movingDown = velocityY > 0
            val movingRight = velocityX > 0

            var newFocused: View? = findFocusableViewInMyBounds(movingRight, scroller.getFinalX(), movingDown, scroller.getFinalY(), findFocus())
            if (newFocused == null) {
                newFocused = this
            }

            if (newFocused !== findFocus() && newFocused.requestFocus(if (movingDown) View.FOCUS_DOWN else View.FOCUS_UP)) {
                mTwoDScrollViewMovedFocus = true
                mTwoDScrollViewMovedFocus = false
            }

            awakenScrollBars(scroller.duration)
            invalidate()
        }
    }

    private fun findFocusableViewInMyBounds(topFocus: Boolean, top: Int, leftFocus: Boolean, left: Int, preferredFocusable: View?): View? {
        /*
   * The fading edge's transparent side should be considered for focus
   * since it's mostly visible, so we divide the actual fading edge length
   * by 2.
   */
        val verticalFadingEdgeLength = verticalFadingEdgeLength / 2
        val topWithoutFadingEdge = top + verticalFadingEdgeLength
        val bottomWithoutFadingEdge = top + height - verticalFadingEdgeLength
        val horizontalFadingEdgeLength = horizontalFadingEdgeLength / 2
        val leftWithoutFadingEdge = left + horizontalFadingEdgeLength
        val rightWithoutFadingEdge = left + width - horizontalFadingEdgeLength

        if (preferredFocusable != null
                && preferredFocusable.top < bottomWithoutFadingEdge
                && preferredFocusable.bottom > topWithoutFadingEdge
                && preferredFocusable.left < rightWithoutFadingEdge
                && preferredFocusable.right > leftWithoutFadingEdge) {
            return preferredFocusable
        }
        return findFocusableViewInBounds(topFocus, topWithoutFadingEdge, bottomWithoutFadingEdge, leftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge)
    }

    private fun findFocusableViewInBounds(topFocus: Boolean, top: Int, bottom: Int, leftFocus: Boolean, left: Int, right: Int): View? {
        val focusables = getFocusables(View.FOCUS_FORWARD)
        var focusCandidate: View? = null

        /*
   * A fully contained focusable is one where its top is below the bound's
   * top, and its bottom is above the bound's bottom. A partially
   * contained focusable is one where some part of it is within the
   * bounds, but it also has some part that is not within bounds.  A fully contained
   * focusable is preferred to a partially contained focusable.
   */
        var foundFullyContainedFocusable = false

        val count = focusables.size
        for (i in 0..count - 1) {
            val view = focusables[i]
            val viewTop = view.top
            val viewBottom = view.bottom
            val viewLeft = view.left
            val viewRight = view.right

            if (top < viewBottom && viewTop < bottom && left < viewRight && viewLeft < right) {
                /*
       * the focusable is in the target area, it is a candidate for
       * focusing
       */
                val viewIsFullyContained = top < viewTop && viewBottom < bottom && left < viewLeft && viewRight < right
                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view
                    foundFullyContainedFocusable = viewIsFullyContained
                } else {
                    val viewIsCloserToVerticalBoundary = topFocus && viewTop < focusCandidate.top || !topFocus && viewBottom > focusCandidate.bottom
                    val viewIsCloserToHorizontalBoundary = leftFocus && viewLeft < focusCandidate.left || !leftFocus && viewRight > focusCandidate.right
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
                            /*
              * We're dealing with only fully contained views, so
              * it has to be closer to the boundary to beat our
              * candidate
              */
                            focusCandidate = view
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view
                            foundFullyContainedFocusable = true
                        } else if (viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
                            /*
              * Partially contained view beats another partially
              * contained view if it's closer
              */
                            focusCandidate = view
                        }
                    }
                }
            }
        }
        return focusCandidate
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
            return 0
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
}
