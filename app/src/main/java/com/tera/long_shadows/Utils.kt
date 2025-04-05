package com.tera.long_shadows

import android.graphics.Bitmap
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Utils {

    companion object{
        const val DIFF = 4
    }

    fun moveBitmap(bm: Bitmap, angle: Float): Bitmap {
        val w = bm.width + DIFF * 3
        val dx = (dX(angle) * DIFF).roundToInt()
        val dy = (dY(angle) * DIFF).roundToInt()

        val bmRes = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888)

        for (x in 0 until bm.width)
            for (y in 0 until bm.height) {
                val pxColor = bm.getPixel(x, y)
                val xN = x + dx
                val yN = y + dy

                if (xN >= 0 && yN >= 0)
                    bmRes.setPixel(xN, yN, pxColor)
            }
        return bmRes
    }

    fun diffBitmap(bm1: Bitmap, bm2: Bitmap): Bitmap {
        // bm1 - вычитаемое
        for (x in 0 until bm1.width)
            for (y in 0 until bm1.height) {
                val pxColor = bm1.getPixel(x, y)
                if (pxColor != 0)
                    bm2.setPixel(x, y, 0)
            }
        return bm2
    }

    fun colorBitmap(bm: Bitmap, color: Int): Bitmap {

        val bmRes = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)

        for (x in 0 until bm.width)
            for (y in 0 until bm.height) {
                val pxColor = bm.getPixel(x, y)
                if (pxColor != 0)
                    bmRes.setPixel(x, y, color)
            }
        return bmRes
    }

    fun dX(angle: Float): Float {
        val a = Math.toRadians(angle.toDouble())
        return cos(a).toFloat()
    }

    fun dY(angle: Float): Float {
        val a = Math.toRadians(angle.toDouble())
        return sin(a).toFloat()
    }


    fun strToArray(str: String): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in str.indices)
            list.add(str[i].toString())
        return list
    }

    fun lastIndex(str: String?, arrayColor: IntArray?): Int {
        if (str == null || arrayColor == null) return 0
        val arraySym = strToArray(str)
        val nColor = arrayColor.size
        var ind = 0

        for (i in arraySym.indices) {
            if (ind > nColor - 1) ind = 0
            ind++
        }
        return ind - 1
    }


}