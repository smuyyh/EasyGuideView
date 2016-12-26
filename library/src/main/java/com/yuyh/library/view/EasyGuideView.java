package com.yuyh.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.support.HShape;

import java.util.List;

/**
 * @author yuyh.
 * @date 2016/12/24.
 */
public class EasyGuideView extends RelativeLayout {

    private int mScreenWidth;
    private int mScreenHeight;

    private int mBgColor = 0xaa000000;
    private float mStrokeWidth;
    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mBitmapRect;
    private RectF outRect = new RectF();

    private Canvas mCanvas;
    private List<HighlightArea> mHighlightList;

    private Xfermode mode;

    public EasyGuideView(Context context) {
        this(context, null);
    }

    public EasyGuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;

        initView();
    }

    private void initView() {

        initPaint();

        mBitmapRect = new RectF();
        mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        setWillNotDraw(false);
        setClickable(true);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
    }

    private void initCanvas() {
        if (mBitmapRect.width() > 0 && mBitmapRect.height() > 0) {
            mBitmap = Bitmap.createBitmap((int) mBitmapRect.width(),
                    (int) mBitmapRect.height(),
                    Bitmap.Config.ARGB_8888);
        } else {
            mBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }

        // 矩形最大边距
        mStrokeWidth = Math.max(Math.max(mBitmapRect.left, mBitmapRect.top),
                Math.max(mScreenWidth - mBitmapRect.right, mScreenHeight - mBitmapRect.bottom));

        outRect.left = mBitmapRect.left - mStrokeWidth / 2;
        outRect.top = mBitmapRect.top - mStrokeWidth / 2;
        outRect.right = mBitmapRect.right + mStrokeWidth / 2;
        outRect.bottom = mBitmapRect.bottom + mStrokeWidth / 2;

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mBgColor);
    }


    /**
     * 设置高亮区域
     *
     * @param list
     */
    public void setHightLightAreas(List<HighlightArea> list) {
        mHighlightList = list;
        if (list != null && !list.isEmpty()) {
            for (HighlightArea area : list) {
                // 合并矩形框
                mBitmapRect.union(area.getRectF());
            }
        }

        initCanvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHighlightList != null && mHighlightList.size() > 0) {



            mPaint.setXfermode(mode);
            mPaint.setStyle(Paint.Style.FILL);
            for (HighlightArea area : mHighlightList) {
                RectF rectF = area.getRectF();
                rectF.offset(-mBitmapRect.left, -mBitmapRect.top);
                switch (area.mShape) {
                    case HShape.CIRCLE:
                        mCanvas.drawCircle(rectF.centerX(), rectF.centerY(),
                                Math.min(area.mHightlightView.getWidth(), area.mHightlightView.getHeight())/2,
                                mPaint);
                        break;
                    case HShape.RECTANGLE:
                        mCanvas.drawRect(rectF, mPaint);
                        break;
                    case HShape.OVAL:
                        mCanvas.drawOval(rectF, mPaint);
                        break;
                }
            }
            canvas.drawBitmap(mBitmap, mBitmapRect.left, mBitmapRect.top, null);
            //绘制剩余空间的矩形
            mPaint.setXfermode(null);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth + 0.1f);
            canvas.drawRect(outRect, mPaint);
        }
    }

    public void recyclerBitmap() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
