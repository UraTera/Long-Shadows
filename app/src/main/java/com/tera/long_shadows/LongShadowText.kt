package com.tera.long_shadows

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.math.max


class LongShadowText(
    context: Context,
    attrs: AttributeSet?,
    defStyleRes: Int
) : View(context, attrs, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    companion object {
        const val TEXT_SIZE = 84 // 32sp
        const val STROKE_WIDTH = 2f
    }

    private var u = Utils()

    private var mPaintText = Paint()
    private var mPaintShadow = Paint()
    private var mPaintAxis = Paint()

    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mXc = 0f
    private var mYc = 0f
    private var mXt = 0f
    private var mYt = 0f
    private var mRect = Rect()
    private var mBlurStyle: Blur? = Blur.NORMAL
    private var mArray = ArrayList<String>()
    private var mLastIndex = 0
    private var mRatioAlpha = 0f

    // Attributes
    private var mArrayColor: IntArray? = null
    private var mBlurWidth = 0f
    private var mEnabledAlpha = false
    private var mFontFamily: Typeface? = null
    private var mMultiColor = false
    private var mShadowAngle = 0f
    private var mShadowColor = 0
    private var mShadowLength = 0
    private var mText: String? = null
    private var mTextColor = 0
    private var mTextSize = 0f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LongShadowText)

        val colorsId = a.getResourceId(R.styleable.LongShadowText_ls_arrayColor, 0)
        if (colorsId != 0)
            mArrayColor = a.resources.getIntArray(colorsId)

        mBlurWidth = a.getDimensionPixelSize(R.styleable.LongShadowText_ls_blurWidth, 0).toFloat()

        mEnabledAlpha = a.getBoolean(R.styleable.LongShadowText_ls_enabledAlpha, true)
        mFontFamily = a.getFont(R.styleable.LongShadowText_ls_fontFamily)
        mMultiColor = a.getBoolean(R.styleable.LongShadowText_ls_multiColor, false)

        mShadowAngle =
            (a.getInt(R.styleable.LongShadowText_ls_shadow_angle, ConstSh.SHADOW_ANGLE).toFloat())
        mShadowColor =
            a.getColor(R.styleable.LongShadowText_ls_shadowColor, ConstSh.SHADOW_COLOR)
        mShadowLength =
            a.getDimensionPixelSize(
                R.styleable.LongShadowText_ls_shadowLength,
                ConstSh.SHADOW_LENGTH
            )

        mText = a.getString(R.styleable.LongShadowText_ls_text)
        mTextColor = a.getColor(R.styleable.LongShadowText_ls_textColor, ConstSh.VIEW_COLOR)

        mTextSize =
            a.getDimensionPixelSize(R.styleable.LongShadowText_ls_textSize, TEXT_SIZE).toFloat()
        a.recycle()

        initPaint()
        setParams()
    }

    private fun initPaint() {
        mPaintText.color = mTextColor
        mPaintText.textSize = mTextSize
        mPaintText.isAntiAlias = true
        mPaintText.isSubpixelText = true
        mPaintText.typeface = mFontFamily

        mPaintShadow.color = mShadowColor
        mPaintShadow.textSize = mTextSize
        mPaintShadow.isAntiAlias = true
        mPaintShadow.isSubpixelText = true
        mPaintShadow.typeface = mFontFamily
        mPaintShadow.style = Paint.Style.STROKE
        mPaintShadow.strokeWidth = STROKE_WIDTH

        mPaintAxis.color = Color.RED
        mPaintAxis.style = Paint.Style.STROKE
        mPaintAxis.strokeWidth = 2f
    }

    private fun setParams() {
        mRatioAlpha = 255f / mShadowLength

        if (mBlurWidth > 0) {
            mPaintShadow.setMaskFilter(BlurMaskFilter(mBlurWidth, mBlurStyle))
        } else
            mPaintShadow.setMaskFilter(null)

        if (mText != null && mMultiColor) {
            mArray = u.strToArray(mText!!)
            if (mArrayColor != null)
                mLastIndex = u.lastIndex(mText!!, mArrayColor!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mText != null) {
            drawText(canvas)
        }
    }

    private fun drawText(canvas: Canvas) {

        if (mEnabledAlpha) mPaintShadow.style = Paint.Style.STROKE
        else mPaintShadow.style = Paint.Style.FILL

        if (mMultiColor) {
            if (mShadowAngle < 90 || mShadowAngle > 270)
                drawShadowRight(canvas)
            else
                drawShadowLeft(canvas)
        } else
            drawShadow(canvas, mText!!, mXt)

        canvas.drawText(mText!!, mXt, mYt, mPaintText)
    }

    private fun drawShadowRight(canvas: Canvas) {
        val wText = mPaintText.measureText(mText)
        var x = mXc - wText / 2

        var nColor = 0
        var indColor = 0
        var color: Int

        if (mArrayColor != null) {
            nColor = mArrayColor!!.size
        }

        for (i in mArray.indices) {
            val ws = mPaintShadow.measureText(mArray[i])
            if (mArrayColor != null) {
                if (indColor >= nColor) indColor = 0
                color = mArrayColor!![indColor]
                indColor++
            } else
                color = (Math.random() * 16777215).toInt() or (0xFF shl 24)

            mPaintShadow.color = color
            drawShadow(canvas, mArray[i], x)
            x += ws
        }
    }

    private fun drawShadowLeft(canvas: Canvas) {
        val wText = mPaintText.measureText(mText)
        var x = mXc + wText / 2
        val indSym = mArray.size - 1
        var nColor = 0
        var indColor = mLastIndex
        var color: Int

        if (mArrayColor != null) {
            nColor = mArrayColor!!.size - 1
        }

        if (mEnabledAlpha) mPaintShadow.style = Paint.Style.STROKE
        else mPaintShadow.style = Paint.Style.FILL

        for (i in indSym downTo 0) {
            val ws = mPaintShadow.measureText(mArray[i])
            if (mArrayColor != null) {
                if (indColor < 0) indColor = indSym
                if (indColor > nColor) indColor = nColor
                color = mArrayColor!![indColor]
                indColor--
            } else
                color = (Math.random() * 16777215).toInt() or (0xFF shl 24)

            x -= ws
            mPaintShadow.color = color
            drawShadow(canvas, mArray[i], x)
        }
    }

    private fun drawShadow(canvas: Canvas, text: String, x: Float) {
        val dx = u.dX(mShadowAngle)
        val dy = u.dY(mShadowAngle)

        for (i in 0..mShadowLength) {
            if (mEnabledAlpha) {
                var alpha = 255 - (mRatioAlpha * i).toInt()
                if (alpha < 0) alpha = 0
                mPaintShadow.alpha = alpha
            }
            canvas.drawText(text, x + dx * i, mYt + dy * i, mPaintShadow)
        }
    }

    private fun setSize() {
        mPaintText.getTextBounds(mText, 0, mText!!.length, mRect)

        val w = mPaintText.measureText(mText)
        mXt = mXc - w / 2
        mYt = mYc - mRect.exactCenterY()
        val h = mRect.height()

        val dx = u.dX(mShadowAngle)
        val dy = u.dY(mShadowAngle)

        mViewWidth = (w + dx * mShadowLength * 2).toInt()
        mViewHeight = (h + dy * mShadowLength * 2).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mText != null)
            setSize()
        val wC = max(mViewWidth, ConstSh.MIN_SIZE)
        val hC = max(mViewHeight, ConstSh.MIN_SIZE)
        setMeasuredDimension(
            resolveSize(wC, widthMeasureSpec),
            resolveSize(hC, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mXc = w / 2f
        mYc = h / 2f
        if (mText != null)
            setSize()
    }

    var arrayColor: Array<Int>? = null
        set(value) {
            field = value
            if (value != null) {
                mArrayColor = value.toIntArray()
                invalidate()
            }
        }

    var blurWidth: Float = 0f
        set(value) {
            field = value
            mBlurWidth = value
            setParams()
            invalidate()
        }

    var enabledAlpha: Boolean = true
        set(value) {
            field = value
            mEnabledAlpha = value
            mPaintShadow.alpha = 255
            invalidate()
        }

    var fontFamily: Typeface? = Typeface.DEFAULT
        set(value) {
            field = value
            mPaintText.typeface = value
            mPaintShadow.typeface = value
            setSize()
            invalidate()
        }

    var multiColor: Boolean = false
        set(value) {
            field = value
            mMultiColor = value
            mPaintShadow.color = mShadowColor
            setParams()
            invalidate()
        }

    var shadowColor: Int = 0
        set(value) {
            field = value
            mShadowColor = value
            mPaintShadow.color = mShadowColor
            invalidate()
        }

    var shadowLength: Int = 0
        set(value) {
            field = value
            mShadowLength = value
            setParams()
            invalidate()
        }

    var shadowAngle: Float = 0f
        set(value) {
            field = value
            mShadowAngle = value
            setParams()
            invalidate()
        }

    var text: String? = null
        set(value) {
            field = value
            if (value != null) {
                mText = value
                setSize()
                setParams()
                invalidate()
            }
        }

    var textSize: Float = 0f
        set(value) {
            field = value
            if (mText != null) {
                mTextSize = value
                mPaintText.textSize = mTextSize
                mPaintShadow.textSize = mTextSize
                setSize()
                invalidate()
            }
        }

    var textColor: Int = 0
        set(value) {
            field = value
            mTextColor = value
            mPaintText.color = mTextColor
            invalidate()
        }
}