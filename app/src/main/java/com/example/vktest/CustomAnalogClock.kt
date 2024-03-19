package com.example.vktest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

class CustomAnalogClock : View {

    private var mHeight = 0
    private var mWidth = 0

    private val mClockHours = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private var mPadding = 0
    private val mNumeralSpacing = 0

    private var mHandTruncation = 0
    private var mHourHandTruncation = 0

    private var mRadius = 0
    private var mPaint: Paint? = null
    private val mRect = Rect()
    private var isInit = false

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {

        if (!isInit) {
            mPaint = Paint()
            mHeight = height
            mWidth = width
            mPadding = mNumeralSpacing + 50
            val minAttr = mHeight.coerceAtMost(mWidth)
            mRadius = minAttr / 2 - mPadding

            mHandTruncation = minAttr / 20
            mHourHandTruncation = minAttr / 10
            isInit = true
        }

        canvas.drawColor(Color.WHITE)

        mPaint?.reset()
        mPaint?.color = Color.DKGRAY
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeWidth = 14f
        mPaint?.isAntiAlias = true
        canvas.drawCircle(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mRadius + mPadding - 10).toFloat(),
            mPaint!!
        )

        mPaint?.style = Paint.Style.FILL
        canvas.drawCircle(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            12f,
            mPaint!!
        )

        val fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24f, resources.displayMetrics)
                .toInt()
        mPaint?.textSize = fontSize.toFloat()
        for (hour in mClockHours) {
            val tmp = hour.toString()
            mPaint?.getTextBounds(tmp, 0, tmp.length, mRect)

            val angle = Math.PI / 6 * (hour - 3)
            val x = (mWidth / 2 + cos(angle) * mRadius - mRect.width() / 2).toInt()
            val y = (mHeight / 2 + sin(angle) * mRadius + mRect.height() / 2).toInt()
            canvas.drawText(
                hour.toString(),
                x.toFloat(),
                y.toFloat(),
                mPaint!!
            )
        }

        val calendar = Calendar.getInstance()
        var hour = calendar[Calendar.HOUR_OF_DAY].toFloat()
        hour = if (hour > 12) hour - 12 else hour

        drawHandLine(
            canvas,
            ((hour + calendar[Calendar.MINUTE] / 60) * 5f).toDouble(),
            isHour = true,
            isSecond = false
        )

        drawHandLine(canvas, calendar[Calendar.MINUTE].toDouble(), isHour = false, isSecond = false)

        drawHandLine(canvas, calendar[Calendar.SECOND].toDouble(), isHour = false, isSecond = true)

        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawHandLine(canvas: Canvas, moment: Double, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * moment / 30 - Math.PI / 2
        val handRadius =
            if (isHour) mRadius - mHandTruncation - mHourHandTruncation else mRadius - mHandTruncation
        if (isSecond) mPaint!!.color = Color.RED
        canvas.drawLine(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius).toFloat(),
            mPaint!!
        )
    }

}

