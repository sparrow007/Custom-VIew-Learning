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
                doReflection(bitmapDrawable.bitmap)
            }
        }

        var bitmap:Bitmap? = null
        bitmap = if (drawable.intrinsicWidth <= 0|| drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        doReflection(bitmap)

    }

    override fun setImageResource(resId: Int) {
        doReflection(BitmapFactory.decodeResource(resources, resId))
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
        //Adding shading to the paint for the gradient
        val shader = LinearGradient(
            0f,
            originalImageHeight.toFloat(),
            0f,
            bitmapWithReflection.height + reflectionGap,
            0x70ffffff,
            0x00000000,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        //Apply porterduff in the rect shape
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        paint.alpha = 250

        //draw rect with paint
        canvas.drawRect(
            0f,
            originalImageHeight.toFloat(),
            originalImageWidth.toFloat(),
            reflectionGap + bitmapWithReflection.height + 20,
            paint
        )

        super.setImageDrawable(BitmapDrawable(resources, bitmapWithReflection))

    }

    private fun doReflection(originalImage: Bitmap?) {
        if (originalImage == null) return
        val reflectionGap = 4
        val width = originalImage.width
        val height = originalImage.height
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        val reflectionImage = Bitmap.createBitmap(
            originalImage, 0,
            height / 2, width, height / 2, matrix, false
        )
        val bitmapWithReflection = Bitmap.createBitmap(
            width,
            height + height / 2, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmapWithReflection)

        val paint = Paint()
        val shader: LinearGradient = LinearGradient(
            0f,
            originalImage.height.toFloat(), 0f, bitmapWithReflection.height.toFloat()
                    + reflectionGap, 0x70ffffff, 0x00000000,
            Shader.TileMode.MIRROR
        )
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        canvas.drawBitmap(originalImage, 0f, 0f, null)
        val defaultPaint = Paint()
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            (bitmapWithReflection.height + reflectionGap).toFloat(),
            defaultPaint
        )
        canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)

        canvas.drawRect(
            0f, height.toFloat(), width.toFloat(), (bitmapWithReflection.height
                    + reflectionGap).toFloat(), paint
        )
        super.setImageDrawable(BitmapDrawable(resources, bitmapWithReflection))
    }
}