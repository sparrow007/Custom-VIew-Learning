package com.example.customviewimple.views.cupview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyBezierProgressView extends View {
    private Paint paint;
    private Path path,circlePath;
    private int radius=350; //The radius of the circle, assuming that the coordinates of the upper left corner of the circle are: (radius*2, radius*3)
    private int marginWidth=radius*2-radius; //The distance from the left side of the circle to the left side of the screen
    private int marginHeight=radius*3-radius; //The distance from the left side of the circle to the top of the screen
    private float moveSpeed=5f; //wave moving speed
    private int isReverse=-1; //The control point of the Bezier curve is reversed
    private int controlNum=2; //The number of control points in the circle
    private MyPoint[] points=new MyPoint[controlNum+2]; //Set of Bezier points
    public MyBezierProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        path=new Path();
        circlePath=new Path();
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(6);
        points[0]=new MyPoint(marginWidth-radius*2/controlNum,marginHeight+radius);
        for(int i=1;i<=controlNum+1;i++){
            points[i]=new MyPoint(marginWidth+(radius*2/controlNum)*(i-1),marginHeight+radius);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Split out the wave area
        canvas.save();
        canvas.clipPath(getBezierPath());
        //canvas.clipPath(getCirclePath());
        canvas.drawColor(Color.BLUE);
        canvas.restore();

       // canvas.drawCircle(radius*2,radius*3,radius,paint);
        changeBezierPos();
        postInvalidateDelayed(5);
    }

    //Change the position of the Bezier curve
    private void changeBezierPos(){
        for(int i=0;i<points.length;i++){
            points[i].setX(points[i].getX()+moveSpeed);
        }
        //If the last Bézier reaches the end, reset it to the first position. And reorder the array elements
        if(points[points.length-1].getX()>=marginWidth+radius*2+radius*2/controlNum){
            points[points.length-1].setX(marginWidth-radius*2/controlNum);
            //The following is the array reordering
            MyPoint myPoint=points[points.length-1];
            for(int i=points.length-2;i>=0;i--){
                points[i+1]=points[i];
            }
            points[0]=myPoint;
            isReverse=-isReverse;
        }
    }

    //Get the area of ​​the circle
    private Path getCirclePath(){
        circlePath.reset();
        circlePath.addCircle(radius*2,radius*3,radius, Path.Direction.CW);
        return circlePath;
    }

    //Get the area under the Bezier curve
    private Path getBezierPath(){
        path.reset();
        path.moveTo(points[0].getX(),points[0].getY());
        for(int i=1;i<points.length;i++){
            //If the array index is odd, the control point of the Bezier curve is below
            if(i%2==1)path.quadTo((points[i].getX()+points[i-1].getX())/2,points[i].getY()+isReverse*radius/5,points[i].getX(),points[i].getY());
                //If the array index is even, the control point of the Bezier curve is on the top
            else path.quadTo((points[i].getX()+points[i-1].getX())/2,points[i].getY()-isReverse*radius/5,points[i].getX(),points[i].getY());
        }
        path.lineTo(marginWidth+radius*2,marginHeight+radius*2);
        path.lineTo(marginWidth-radius*2/controlNum,marginHeight+radius*2);
        return path;
    }
}