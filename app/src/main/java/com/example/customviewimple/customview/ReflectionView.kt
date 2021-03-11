package com.example.customviewimple.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ReflectionView(context: Context, attrs: AttributeSet): AppCompatImageView(context, attrs) {

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable == null) return
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable as BitmapDrawable
            if (bitmapDrawable.bitmap != null) {
                getReflection(bitmapDrawable.bitmap)
            }
        }

        var bitmap:Bitmap? = null
        bitmap = if (drawable.intrinsicWidth <= 0|| drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
        }else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width, canvas.height)
        drawable.draw(canvas)
        getReflection(bitmap)

    }

    override fun setImageResource(resId: Int) {
        getReflection(BitmapFactory.decodeResource(resources, resId))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun getReflection(image: Bitmap?) {

        if (image == null ) return

        val reflectionGap = 4f
        val matrix = Matrix()
        matrix.preScale(1f, -1f)

        val originalImageWidth = image.width
        val originalImageHeight = image.height

        val reflectionImage = Bitmap.createBitmap(
            image, 0, originalImageHeight / 2, originalImageWidth,
            originalImageHeight / 2, matrix, false
        )

        val bitmapWithReflection = Bitmap.createBitmap(
            originalImageWidth, originalImageHeight + originalImageHeight / 2,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmapWithReflection)

        //draw original image
        canvas.drawBitmap(image, 0f, 0f, null)

        //draw reflection image
        canvas.drawBitmap(reflectionImage, 0f, originalImageHeight + reflectionGap, null)

        val paint = Paint()


        super.setImageDrawable(BitmapDrawable(resources, bitmapWithReflection))

    }
}