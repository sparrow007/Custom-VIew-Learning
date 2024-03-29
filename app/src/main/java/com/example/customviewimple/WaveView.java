package com.example.customviewimple;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class WaveView extends View{

    private Path mAbovePath,mBelowWavePath;
    private Paint mAboveWavePaint,mBelowWavePaint;
    private DrawFilter mDrawFilter;
    private float φ;
    private OnWaveAnimationListener mWaveAnimationListener;
    private int progress = 0;
    private double ω;


    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化路径
        mAbovePath = new Path();
        mBelowWavePath = new Path();

        //初始化画笔
        mAboveWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAboveWavePaint.setAntiAlias(true);
        mAboveWavePaint.setStyle(Paint.Style.FILL);
        mAboveWavePaint.setColor(Color.BLACK);

        mBelowWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBelowWavePaint.setAntiAlias(true);
        mBelowWavePaint.setStyle(Paint.Style.STROKE);
        mBelowWavePaint.setColor(Color.BLACK);
        mBelowWavePaint.setAlpha(80);

        //画布抗锯齿
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progress = h - 100;
        ω = 2*Math.PI / getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

      //  canvas.setDrawFilter(mDrawFilter);

        mAbovePath.reset();
        mBelowWavePath.reset();

        φ-=0.2f;
        float y,y2;

        mAbovePath.moveTo(getLeft() + 250,getBottom());
        //mBelowWavePath.moveTo(getLeft(),getBottom());

        for (float x = 100; x <= getWidth()-100; x += 20) {
            /**
             *  y=Asin(ωx+φ)+k
             *  A—振幅越大，波形在y轴上最大与最小值的差值越大
             *  ω—角速度， 控制正弦周期(单位角度内震动的次数)
             *  φ—初相，反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
             *  k—偏距，反映在坐标系上则为图像的上移或下移。
             */
            y = (float) (15 * Math.cos(ω * x + φ)) + progress;
            y2 = (float) (10 * Math.sin(ω * x + φ));
            mAbovePath.lineTo(getLeft()+50 + x, y);
           // mBelowWavePath.lineTo(x, y2);
            //回调 把y坐标的值传出去(在activity里面接收让小机器人随波浪一起摇摆)
//            mWaveAnimationListener.OnWaveAnimation(y);
        }

        mAbovePath.lineTo(getRight() - 150,getBottom());
        //mBelowWavePath.lineTo(getRight(),getBottom());

        canvas.drawPath(mAbovePath,mAboveWavePaint);
       // canvas.drawPath(mBelowWavePath,mBelowWavePaint);

        if (progress == 0)
        postInvalidateDelayed(20);
    }

    public void setOnWaveAnimationListener(OnWaveAnimationListener l){
        this.mWaveAnimationListener = l;
    }

    public interface OnWaveAnimationListener{
        void OnWaveAnimation(float y);
    }

    void setProgress(int progress) {
        this.progress = getHeight() - progress;
        invalidate();
    }


    void animation() {
        ObjectAnimator objectAnimate = ObjectAnimator.ofInt(this, "progress", 0, getHeight());
        objectAnimate.setDuration(4000);
        objectAnimate.setInterpolator(new AccelerateInterpolator());
        objectAnimate.start();
    }
}