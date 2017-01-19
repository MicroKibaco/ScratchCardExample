package com.asiainfo.scratchcard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 刮刮卡扩展类
 */

public class ScratchCard extends View {
    public OnScratchCardCompleteListener mListener;
    /**
     * 遮盖层的变量
     */
    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int mLastX;

    //private Bitmap mDawnBitmap;
    private int mLastY;
    private String mText;
    private Paint mBackPaint;
    private Rect mTextBound;
    private int mTextSize;
    /**
     * 判断遮盖层区域是否消除达到域值
     */
    private volatile boolean mComplete = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalAreas = w * h;

            Bitmap bitmap = mBitmap;
            int mPixels[] = new int[w * h];

            //获取Bitmap上所有的像素信息
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = j * w + i ;

                    if (mPixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }

            if (wipeArea > 0 && totalAreas > 0) {

                int percent = (int) (wipeArea * 100 / totalAreas);

                Log.e("ScratchCard", percent + "");

                if (percent > 50) {
                    //清除涂层区域

                    mComplete = true;

                    postInvalidate();

                }
            }

        }
    };

    /**
     * 记录刮奖信息文本的宽和高
     */

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

    public void setOnScratchCardCompleteListener(OnScratchCardCompleteListener listener) {
        mListener = listener;
    }

    /**
     * 进行一些初始化操作
     */

    private void init() {

        mOuterPaint = new Paint();

        mPath = new Path();

        //mDawnBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_card);

        mText = "谢谢惠顾";

        mTextBound = new Rect();
        mBackPaint = new Paint();
        mTextSize = 80;
    }

    /**
     * 获取控件的宽和高
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
        setUpBackPaint();

        mCanvas.drawColor(Color.parseColor("#c0c0c0"));


    }

    /**
     * 设置我们绘制获奖信息的画笔属性
     */
    private void setUpBackPaint() {

        mBackPaint.setColor(Color.DKGRAY);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextSize(mTextSize);
        //获得当前画笔绘制文本的宽和高
        mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);


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
                //计算绘制像素
                new Thread(mRunnable).start();
                //postInvalidate();

                break;


            default:
                break;

        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas.drawBitmap(mDawnBitmap, 0, 0, null);

        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mBackPaint);


        if (mComplete){

            if (mListener!=null){

                mListener.compelete();
            }


        } else {

            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);//刷缓冲

        }

    }

    private void drawPath() {
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mCanvas.drawPath(mPath, mOuterPaint);
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
        mOuterPaint.setStrokeWidth(45);
        mOuterPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 刮刮卡刮完的回调
     */
    public interface OnScratchCardCompleteListener{
        void compelete();
    }
}
