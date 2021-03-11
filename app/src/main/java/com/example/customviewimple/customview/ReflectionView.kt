package com.example.customviewimple.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ReflectionView(context: Context, attrs: AttributeSet): AppCompatImageView(context, attrs) {

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}