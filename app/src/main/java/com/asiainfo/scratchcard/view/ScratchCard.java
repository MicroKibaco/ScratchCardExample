package com.asiainfo.scratchcard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.asiainfo.scratchcard.R;

/**
 * 刮刮卡扩展类
 */

public class ScratchCard extends View {
    /**
     * 遮盖层的变量
     */
    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    private Bitmap mDawnBitmap;

    public ScratchCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public ScratchCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCard(Context context) {
        this(context, null);
    }

    /**
     * 进行一些初始化操作
     */

    private void init() {

        mOuterPaint = new Paint();

        mPath = new Path();

        mDawnBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_card);

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

        mCanvas.drawColor(Color.parseColor("#c0c0c0"));


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
        canvas.drawBitmap(mDawnBitmap, 0, 0, null);
        drawPath();
        canvas.drawBitmap(mBitmap,0,0,null);//刷缓冲
        super.onDraw(canvas);
    }


    private void drawPath() {
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mCanvas.drawPath(mPath,mOuterPaint);
    }

    /**
     * 设置绘制path画笔的属性
     */

    private void setOutPaint() {
        mOuterPaint.setColor(Color.BLUE);
        mOuterPaint.setDither(true);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStrokeWidth(25);
        mOuterPaint.setStyle(Paint.Style.STROKE);
    }
}
