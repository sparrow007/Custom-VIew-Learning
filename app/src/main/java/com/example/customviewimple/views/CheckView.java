package com.example.customviewimple.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customviewimple.R;

public class CheckView extends View {
    private  static  final  int  ANIM_NULL  =  0 ;          // Animation status-no
    private  static  final  int  ANIM_CHECK  =  1 ;         // Animation status-open
    private  static  final  int  ANIM_UNCHECK  =  2 ;       // Animation status-end

    private  Context mContext;            // Context
    private  int mWidth, mHeight;         // Width and height
    private  Handler mHandler;            // handler

    private Paint mPaint;
    private Bitmap okBitmap;

    private   int animCurrentPage =  -1 ;        // current page
    private  int animMaxPage =  13  ;            // Pages
    private  int animDuration =  500 ;          // length animation
    private  int animState =  ANIM_NULL ;       // animation states

    private  boolean isCheck =  false ;         // whether only the selected state

    public CheckView(Context context) {
        super(context, null);

    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Initialization
     * @param context
     */ private void init ( Context context ) {

        mContext = context;

        mPaint = new Paint();
        mPaint.setColor(0xffFF5317);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        okBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.checkmark);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (animCurrentPage < animMaxPage && animCurrentPage >= 0) {
                    if (animState == ANIM_NULL)
                        return;
                    if (animState == ANIM_CHECK) {
                        animCurrentPage++;
                    } else if (animState == ANIM_UNCHECK) {
                        animCurrentPage--;
                    }
                    //animCurrentPage为animMaxPage不需要更新，否则下面的else会导致绘制前一张图闪一下
                    if (animCurrentPage != animMaxPage) {
                        invalidate();
                    }
                    this.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
                } else {
                    if (isCheck) {
                        animCurrentPage = animMaxPage - 1;
                    } else {
                        animCurrentPage = -1;
                    }
                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };
    }


    /**
     * View大小确定
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * Draw content
     * @param canvas
     */ @Override protected void onDraw ( Canvas canvas ) {



        // Move the coordinate system to the center of the
        canvas . translate(mWidth /  2 , mHeight /  2 );

        // Draw the background circle
        canvas . drawCircle( 0 , 0 , 240 , mPaint);

        //
       // Get the image side length

        int sideLength = okBitmap.getHeight();

        // obtain an image drawing position and the actual selection
        Rect src =  new Rect (sideLength * animCurrentPage, 0 , sideLength * (animCurrentPage + 1 ), sideLength);
        Rect DST =  new Rect ( - 200, - 200  , 200  , 200  );

        // Draw the
        canvas . drawBitmap(okBitmap, src, DST, null );
    }


    /**
     * 选择
     */
    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState =  ANIM_CHECK ;
        animCurrentPage =  0 ;
        mHandler . sendEmptyMessageDelayed ( 0 , animDuration / animMaxPage);
        isCheck = true;
    }

    /**
     * Cancel selection
     */ public void unCheck () {
        if (animState != ANIM_NULL || ( ! isCheck))
            return ;

        animState =  ANIM_UNCHECK ;
        animCurrentPage = animMaxPage -  1 ;
        mHandler . sendEmptyMessageDelayed ( 0 , animDuration / animMaxPage);
        isCheck =  false ;
    }

    /**
     * Set animation duration
     * @param animDuration
     */ public void setAnimDuration ( int animDuration ) {
        if (animDuration <= 0 )
            return ;
        this.animDuration = animDuration;

    }

    /**
     * Set the background circle color
     * @param color
     */ public void setBackgroundColor ( int color ){

        mPaint.setColor(color);
    }
}