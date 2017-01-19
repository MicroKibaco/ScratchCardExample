package com.asiainfo.scratchcard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 刮刮卡扩展类
 */

public class ScratchCard extends View {

    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    public ScratchCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 进行一些初始化操作
     */

    private void init() {

        mOuterPaint = new Paint();

        mPath = new Path();


    }

    public ScratchCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCard(Context context) {
        this(context, null);
    }

    /**
     * 获取控件的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int high = getMeasuredHeight();

        //初始化我们的bitmap
        mBitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        setOutPaint();


    }


    /**
     * 用户在画板上绘制
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;

                mPath.moveTo(mLastX, mLastY);

                break;

            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 1 || dy > 1) {

                    mPath.lineTo(x, y);

                }
                mLastX = x;
                mLastY = y;

                break;

            case MotionEvent.ACTION_UP:
                break;



            default:
                break;

        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPath();
        canvas.drawBitmap(mBitmap,0,0,null);//刷缓冲
        super.onDraw(canvas);
    }


    private void drawPath() {
        mCanvas.drawPath(mPath,mOuterPaint);
    }

    /**
     * 设置绘制path画笔的属性
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOutPaint() {
        mOuterPaint.setColor(Color.BLUE);
        mOuterPaint.setDither(true);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStrokeWidth(10);
        mOuterPaint.setStyle(Paint.Style.STROKE);
    }
}
