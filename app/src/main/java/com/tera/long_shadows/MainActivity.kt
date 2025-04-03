package com.tera.long_shadows

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tera.long_shadows.databinding.ActivityMainBinding
import com.tera.palettedialog.PaletteDialog


class MainActivity : AppCompatActivity() {

    companion object {
        const val TEXT = "text"
        const val VIEW_COLOR = "text_color"
        const val VIEW_SIZE = "text_size"
        const val SHADOW_COLOR = "shadow_color"
        const val SHADOW_LENGTH = "shadow_length"
        const val SHADOW_ANGLE = "shadow_angle"
        const val BLUR_WIDTH = "blur_width"
        const val KEY_ALPHA = "key_alpha"
        const val KEY_FONT = "key_font"
        const val KEY_MULTI = "key_multi"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences

    private var mText = ""
    private var mViewColor = 0
    private var mViewSize = 0f
    private var mShadowLength = 0
    private var mShadowAngle = 0f
    private var mShadowColor = 0
    private var mBlurWidth = 0f
    private var mKeyAlpha = true
    private var mKeyMulti = false
    private var mKeyFont = false

    private val mArrayColor = arrayOf(
        Color.MAGENTA, Color.BLUE, Color.LTGRAY,
        Color.CYAN, Color.RED, Color.GREEN, -15614083, -3732859, -6206783, -936674
    )

//    private val mArrayColor = arrayOf(Color.GREEN, Color.BLUE, Color.RED)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("settings", Context.MODE_PRIVATE)
        mText = sp.getString(TEXT, "Text").toString()
        mViewSize = sp.getFloat(VIEW_SIZE, 80f)
        mShadowLength = sp.getInt(SHADOW_LENGTH, 40)
        mShadowAngle = sp.getFloat(SHADOW_ANGLE, 45f)
        mViewColor = sp.getInt(VIEW_COLOR, Color.BLUE)
        mShadowColor = sp.getInt(SHADOW_COLOR, -7538441)
        mBlurWidth = sp.getFloat(BLUR_WIDTH, 0f)
        mKeyAlpha = sp.getBoolean(KEY_ALPHA, true)
        mKeyMulti = sp.getBoolean(KEY_MULTI, false)
        mKeyFont = sp.getBoolean(KEY_FONT, false)

        setStart()
        setText()
        setFont()
        setColor()
        initControls()

    }

    private fun setStart() = with(binding) {
        lsText.arrayColor = mArrayColor

        slSize.value = mViewSize
        lsText.textSize = mViewSize
        lsIcon.iconSize = mViewSize

        slLength.value = mShadowLength.toFloat()
        lsText.shadowLength = mShadowLength
        lsIcon.shadowLength = mShadowLength

        slAngle.value = mShadowAngle
        lsText.shadowAngle = mShadowAngle
        lsIcon.shadowAngle = mShadowAngle

        slBlur.value = mBlurWidth
        lsText.blurWidth = mBlurWidth

        chAlpha.isChecked = mKeyAlpha
        lsText.enabledAlpha = mKeyAlpha
        lsIcon.enabledAlpha = mKeyAlpha

        chMulti.isChecked = mKeyMulti
        lsText.multiColor = mKeyMulti

        chFont.isChecked = mKeyFont
    }

    private fun setText() = with(binding) {
        val str = mShadowAngle.toInt().toString() + "\u00B0"
        tvAngle.text = str
    }

    private fun setFont() = with(binding) {
        val font1 = resources.getFont(R.font.comic)
        val font2 = resources.getFont(R.font.candice)
        val font3 = resources.getFont(R.font.cyrillichover)
        val font4 = resources.getFont(R.font.casanova)
        val icon1 = R.drawable.ic_circle
        val icon2 = R.drawable.ic_star_color

        if (mKeyFont) {
            lsText.fontFamily = font1
//            lsIcon.icon = icon1
//            lsText.text = "Hello"
        }
        else {
            lsText.fontFamily = font4
//            lsIcon.icon = icon2
//            lsText.text = "World!"
        }
    }

    private fun setColor() = with(binding) {
        lsText.textColor = mViewColor
//        lsIcon.iconColor = mViewColor

        lsText.shadowColor = mShadowColor
        lsIcon.shadowColor = mShadowColor
    }

    private fun initControls() = with(binding) {

        bnTextColor.setOnClickListener {
            openDialog(mViewColor, 1)
        }

        bnShadowColor.setOnClickListener {
            openDialog(mShadowColor, 2)
        }

        chAlpha.setOnCheckedChangeListener { _, isChecked ->
            mKeyAlpha = isChecked
            lsText.enabledAlpha = mKeyAlpha
            lsIcon.enabledAlpha = mKeyAlpha
        }

        chMulti.setOnCheckedChangeListener { _, isChecked ->
            mKeyMulti = isChecked
            lsText.multiColor = mKeyMulti
        }

        chFont.setOnCheckedChangeListener { _, isChecked ->
            mKeyFont = isChecked
            setFont()
        }

        slSize.setOnChangeListener {
            mViewSize = it
            lsText.textSize = it
            lsIcon.iconSize = it
        }

        slLength.setOnChangeListener {
            mShadowLength = it.toInt()
            lsText.shadowLength = mShadowLength
            lsIcon.shadowLength = mShadowLength
        }

        slAngle.setOnChangeListener {
            mShadowAngle = it
            lsText.shadowAngle = it
            lsIcon.shadowAngle = it
            setText()
        }

        slBlur.setOnChangeListener {
            mBlurWidth = it
            lsText.blurWidth = mBlurWidth
        }
    }

    private fun openDialog(color: Int, numColor: Int) {
        val dialog = PaletteDialog(this, color)
        dialog.setOnClickListener {
            when (numColor) {
                1 -> {
                    mViewColor = it
                    setColor()
                }

                2 -> {
                    mShadowColor = it
                    setColor()
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        val editor = sp.edit()
        editor.putString(TEXT, mText)
        editor.putFloat(VIEW_SIZE, mViewSize)
        editor.putInt(SHADOW_LENGTH, mShadowLength)
        editor.putFloat(SHADOW_ANGLE, mShadowAngle)
        editor.putInt(VIEW_COLOR, mViewColor)
        editor.putInt(SHADOW_COLOR, mShadowColor)
        editor.putFloat(BLUR_WIDTH, mBlurWidth)
        editor.putBoolean(KEY_ALPHA, mKeyAlpha)
        editor.putBoolean(KEY_MULTI, mKeyMulti)
        editor.putBoolean(KEY_FONT, mKeyFont)
        editor.apply()
    }

}