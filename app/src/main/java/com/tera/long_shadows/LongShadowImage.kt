package com.tera.long_shadows

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.max

class LongShadowImage(
    context: Context,
    attrs: AttributeSet?,
    defStyleRes: Int
) : View(context, attrs, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    companion object {
        const val ICON_SIZE = 70
    }

    private var u = Utils()
    private var mPaint = Paint()

    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mXc = 0f
    private var mYc = 0f
    private var mBitmapIcon: Bitmap? = null
    private var mBitmapShadow: Bitmap? = null
    private var mK = 1.5f

    // Attributes
    private var mEnabledAlpha = false
    private var mIcon = 0
    private var mIconColor = 0
    private var mIconSize = 0f
    private var mShadowAngle = 0f
    private var mShadowColor = 0
    private var mShadowLength = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LongShadowImage)
        mEnabledAlpha = a.getBoolean(R.styleable.LongShadowImage_lsi_enabledAlpha, true)

        mIcon = a.getResourceId(R.styleable.LongShadowImage_lsi_icon, 0)
        mIconColor = a.getColor(R.styleable.LongShadowImage_lsi_iconColor, 0)
        mIconSize =
            a.getDimensionPixelSize(R.styleable.LongShadowImage_lsi_iconSize, ICON_SIZE)
                .toFloat() * mK

        mShadowAngle =
            (a.getInt(R.styleable.LongShadowImage_lsi_shadow_angle, ConstSh.SHADOW_ANGLE).toFloat())
        mShadowColor =
            a.getColor(R.styleable.LongShadowImage_lsi_shadowColor, ConstSh.SHADOW_COLOR)
        mShadowLength =
            a.getDimensionPixelSize(R.styleable.LongShadowImage_lsi_shadowLength, ConstSh.SHADOW_LENGTH)

        a.recycle()

        if (mIcon != 0 && mIconSize > 0)
            setBitmaps()
    }

    private fun setBitmaps() {
        val w = mIconSize.toInt()
        val drawable = ContextCompat.getDrawable(context, mIcon) as Drawable

        if (mIconColor != 0)
            drawable.setTint(mIconColor)

        mBitmapIcon = drawable.toBitmap(w, w, null)

        val bitmap1 = drawable.toBitmap(w, w, null)

        if (mEnabledAlpha) {
            // Move
            mBitmapShadow = u.moveBitmap(bitmap1, mShadowAngle)
            // Subtract
            mBitmapShadow = u.diffBitmap(bitmap1, mBitmapShadow!!)
            // Change color
            mBitmapShadow = u.colorBitmap(mBitmapShadow!!, mShadowColor)
        } else
            mBitmapShadow = u.colorBitmap(bitmap1, mShadowColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mIcon != 0 && mIconSize > 0)
            drawIcon(canvas)
    }

    private fun drawIcon(canvas: Canvas) {
        val w = mIconSize.toInt()
        val x = (mXc - w / 2)
        val y = (mYc - w / 2)
        val dx = u.dX(mShadowAngle)
        val dy = u.dY(mShadowAngle)
        val dAlpha = 255f / mShadowLength
        mPaint.alpha = 255

        for (i in 0..mShadowLength step 2) {
            if (mEnabledAlpha) {
                var alpha = 255 - (dAlpha * i).toInt()
                if (alpha < 0) alpha = 0
                mPaint.alpha = alpha
            }
            canvas.drawBitmap(mBitmapShadow!!, x + dx * i, y + dy * i, mPaint)
        }
        canvas.drawBitmap(mBitmapIcon!!, x, y, null)
    }

    private fun setSize() {
        val w = mBitmapIcon!!.width
        val h = mBitmapIcon!!.height
        val dx = u.dX(mShadowAngle)
        val dy = u.dY(mShadowAngle)
        mViewWidth = (w + dx * mShadowLength).toInt()
        mViewHeight = (h + dy * mShadowLength).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mIcon != 0 && mIconSize > 0)
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
    }

    var icon: Int? = 0
        set(value) {
            field = value
            if (value != null && mIconSize > 0) {
                mIcon = value
                setBitmaps()
                invalidate()
            }
        }

    var iconColor: Int = 0
        set(value) {
            field = value
            if (mBitmapIcon != null){
                mIconColor = value
                mBitmapIcon = u.colorBitmap(mBitmapIcon!!, mIconColor)
            }
            invalidate()
        }

    var iconSize: Float = 0f
        set(value) {
            field = value
            mIconSize = value * mK
            if (mBitmapIcon != null && mBitmapShadow != null)
                setBitmaps()
            invalidate()
        }

    var enabledAlpha: Boolean = true
        set(value) {
            field = value
            mEnabledAlpha = value
            invalidate()
        }

    var shadowColor: Int = 0
        set(value) {
            field = value
            mShadowColor = value
            if (mBitmapShadow != null) {
                mShadowColor = value
                mBitmapShadow = u.colorBitmap(mBitmapShadow!!, mShadowColor)
            }
            invalidate()
        }

    var shadowLength: Int = 0
        set(value) {
            field = value
            mShadowLength = value
            invalidate()
        }

    var shadowAngle: Float = 0f
        set(value) {
            field = value
            mShadowAngle = value
            setBitmaps()
            invalidate()
        }


}