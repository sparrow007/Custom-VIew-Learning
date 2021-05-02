package com.example.customviewimple.views.drawable

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class PolyViewDraw(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    val polygonLapsDrawable = PolygonLapsDrawable()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            polygonLapsDrawable.draw(canvas)
        }
    }

}