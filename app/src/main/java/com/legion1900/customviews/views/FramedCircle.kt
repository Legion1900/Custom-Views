package com.legion1900.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.legion1900.customviews.R
import kotlin.properties.Delegates

class FramedCircle @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
) : View(context, attrSet) {

    var circleColor: Int by Delegates.observable(getColor(R.color.colorDefCircle)) { _, _, _ ->
        invalidate()
    }

    var circleRadius: Int by Delegates.observable(DEF_RADIUS) { _, _, _ ->
        requestLayout()
    }

    var isFramed: Boolean by Delegates.observable(true) { _, _, _ ->
        invalidate()
    }

    var frameColor: Int by Delegates.observable(getColor(R.color.colorDefFrame)) { _, _, _ ->
        invalidate()
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrSet, R.styleable.FramedCircle, 0, 0)
        try {
            circleColor =
                a.getColor(R.styleable.FramedCircle_circleColor, getColor(R.color.colorDefCircle))
            circleRadius =
                a.getDimensionPixelSize(R.styleable.FramedCircle_circleRadius, DEF_RADIUS)
            isFramed = a.getBoolean(R.styleable.FramedCircle_isFramed, true)
            frameColor =
                a.getColor(R.styleable.FramedCircle_frameColor, getColor(R.color.colorDefFrame))
        } finally {
            a.recycle()
        }
    }

    /*
    * Pair(x, y).
    * */
    private var circleCenter = Pair(0, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, widthMode) = unpackSpec(widthMeasureSpec)
        val (height, heightMode) = unpackSpec(heightMeasureSpec)
        val (measuredWidth, measuredHeight) = measureUnspecified()
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    /*
    * Returns Pair<measured width, measured height>.
    * */
    private fun measureUnspecified(): Pair<Int, Int> {
        val width = circleRadius * 2 + paddingLeft + paddingRight
        val height = circleRadius * 2 + paddingTop + paddingBottom
        return Pair(width, height)
    }

    /*
    * Returns values in format Pair<Size, Mode>.
    * */
    private fun unpackSpec(spec: Int): Pair<Int, Int> {
        return Pair(MeasureSpec.getSize(spec), MeasureSpec.getMode(spec))
    }

    /*
    * wrap content does not work like that!
    * */
    private val Int.isUnspecified
        get() = this == MeasureSpec.UNSPECIFIED

    private val Int.isExactly
        get() = this == MeasureSpec.EXACTLY

    private val Int.isAtMost
        get() = this == MeasureSpec.AT_MOST

    private fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(context, colorId)

    private companion object {
        const val DEF_RADIUS = 200
    }
}
