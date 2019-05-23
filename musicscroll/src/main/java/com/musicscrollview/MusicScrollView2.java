package com.musicscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by ChenShaohua on 2019/5/18
 */
public class MusicScrollView2 extends View {
    private static final int MESSAGE_X = 0;
    private static final int MESSAGE_PIC = 1;
    private Paint mPaint = new Paint();
    private int mTextColor;
    private String mText;
    private float mTextSize;
    private int mWid;
    private int mHei;
    private Bitmap mBackBitmap;
    private ArrayList<Integer> mPicList = new ArrayList<>();
    //当前要绘制的图片的位置
    private int mCurrentPicPosition = 0;
    private float mBaseLineY;
    private float mTextWid;
    private float mPicWid = 70;
    private float mPicHei = 50;
    //当前第一个子控件的绘制的位置
    private float mCurrentFirstPos;
    //当前第二个子控件绘制的位置
    private float mCurrentSecondPos;
    private MsgHandler mHandlerOffset = new MsgHandler(this);
    private MsgHandler mHandlerPic = new MsgHandler(this);
    //子控件位置移动的时间刷新间距值
    private long mOffsetTimeX = 10;
    //音符跳动图片的时间刷新间距值
    private long mOffsetTimePic = 50;
    private float mMaxLeft;
    private float mMaxRight;
    //两个子控件的间距，只有在子控件大于父控件时候此值才会生效
    private float mBigTwoMarginOffset = 100;
    //文字和音符图片的间距
    private float mTextAndPicOffset = 40;
    private boolean isStart = false;
    private boolean isInitStart = false;

    public MusicScrollView2(Context context) {
        this(context, null);
    }

    public MusicScrollView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicScrollView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MusicScrollView2);
        if (array != null) {
            mTextColor = array.getColor(R.styleable.MusicScrollView2_textColor,
                    ContextCompat.getColor(context, R.color.white));
            mText = array.getString(R.styleable.MusicScrollView2_text);
            mTextSize = array.getDimension(R.styleable.MusicScrollView2_textSize, DensityUtil.sp2px(context, 16));
            mBigTwoMarginOffset = array.getDimension(R.styleable.MusicScrollView2_bigTwoOffset, DensityUtil.dip2px(context, 40));
            mTextAndPicOffset = array.getDimension(R.styleable.MusicScrollView2_textAndPicOffset, DensityUtil.dip2px(context, 5));
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            array.recycle();
        }
        //背景图片
        mBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music_songnamedisplay);

        //获取图片地址存放在list集合中
        TypedArray flagmusic = getResources().obtainTypedArray(R.array.flagmusic);
        int len = flagmusic.length();
        for (int i = 0; i < len; i++) {
            mPicList.add(flagmusic.getResourceId(i, 0));
        }
        flagmusic.recycle();

        //获取文字的宽高
        mTextWid = mPaint.measureText(mText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heiMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int heiMode = MeasureSpec.getMode(heightMeasureSpec);
        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int widMeasure = MeasureSpec.getSize(widthMeasureSpec);

        mWid = widMeasure;
        mHei = heiMeasure;
        mCurrentFirstPos = (float) mWid / (float) 2;
        mMaxLeft = -(mTextWid / 2 + mPicWid) + mTextAndPicOffset;
        mMaxRight = (float) mWid + mTextWid / 2 + mPicWid + mTextAndPicOffset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);

        drawText(canvas);

        drawPic(canvas, mCurrentPicPosition);
    }

    private void drawPic(Canvas canvas, int position) {
        if (position >= 0 && position < mPicList.size()) {
            Integer picInt = mPicList.get(position);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), picInt);

            //画第一个左侧图片
            drawLeftPic(canvas, mCurrentFirstPos, bitmap);
            //画第一个右侧图片
            drawRightPic(canvas, mCurrentFirstPos, bitmap);

            if (isInitStart) {
                //画第二个左侧图片
                drawLeftPic(canvas, mCurrentSecondPos, bitmap);
                //画第二个右侧图片
                drawRightPic(canvas, mCurrentSecondPos, bitmap);
            }
        }
    }

    private void drawRightPic(Canvas canvas, float currentPos, Bitmap bitmap) {
        float left = currentPos + mTextWid / 2 + mTextAndPicOffset;
        float right = left + mPicWid;
        float top = (float) mHei / (float) 2 - mPicHei / (float) 2;
        float bottom = (float) mHei / (float) 2 + mPicHei / (float) 2;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, rectF, mPaint);
    }

    private void drawLeftPic(Canvas canvas, float currentPos, Bitmap bitmap) {
        float right = currentPos - mTextWid / (float) 2 - mTextAndPicOffset;
        float left = right - mPicWid;
        float top = (float) mHei / (float) 2 - mPicHei / (float) 2;
        float bottom = (float) mHei / (float) 2 + mPicHei / (float) 2;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, rectF, mPaint);
    }

    private void drawText(Canvas canvas) {
        //画第一个
        float center = (float) mHei / (float) 2;
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        mBaseLineY = center + (float) (fm.bottom - fm.top) / (float) 2 - fm.bottom;
        canvas.drawText(mText, mCurrentFirstPos, mBaseLineY, mPaint);

        //画第二个
        if (isInitStart) {
            canvas.drawText(mText, mCurrentSecondPos, mBaseLineY, mPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        RectF rectF = new RectF(0, 0, mWid, mHei);
        canvas.drawBitmap(mBackBitmap, null, rectF, mPaint);
    }

    public void startMove() {
        stopMove();
        mHandlerOffset.sendEmptyMessageDelayed(MESSAGE_X, mOffsetTimeX);
    }

    private void stopMove() {
        mHandlerOffset.removeMessages(MESSAGE_X);
    }

    public void startChangePic() {
        stopChangePic();
        mHandlerPic.sendEmptyMessageDelayed(MESSAGE_PIC, mOffsetTimePic);
    }

    private void stopChangePic() {
        mHandlerPic.removeMessages(MESSAGE_PIC);
    }

    public void startAnim() {
        isInitStart = true;
        isStart = true;
        startMove();
        startChangePic();
    }

    public void stopAnim() {
        isStart = false;
        stopMove();
        stopChangePic();
    }

    public boolean isStart() {
        return isStart;
    }

    static class MsgHandler extends Handler {
        private WeakReference<MusicScrollView2> mView;

        MsgHandler(MusicScrollView2 view) {
            mView = new WeakReference<MusicScrollView2>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicScrollView2 view = mView.get();
            if (view != null) {
                view.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        int what = msg.what;
        if (what == MESSAGE_X) {
            if (mCurrentFirstPos >= mMaxLeft && mCurrentFirstPos <= mMaxRight) {
                mCurrentFirstPos = mCurrentFirstPos - 1;
            } else if (mCurrentFirstPos < mMaxRight) {
                //当第一个滑出左侧时候，mCurrentFirstPos和mCurrentSecondPos进行交换
                mCurrentFirstPos = mCurrentSecondPos;
            } else {
                mCurrentFirstPos = mMaxRight;
            }
            if (isSonThanFather()) {
                //如果子控件的长度大于父控件那么，使用两个子控件固定间距的场景
                mCurrentSecondPos = mCurrentFirstPos + mTextWid + 2 * mPicWid + mBigTwoMarginOffset + 2 * mTextAndPicOffset;
            } else {
                //如果子控件不大于父控件那么，使用当第一个子控件左侧滑动到父控件左侧时候第二个控件显示的场景
                mCurrentSecondPos = mCurrentFirstPos + (float) mWid;
            }
            startMove();
        } else if (what == MESSAGE_PIC) {
            if (mCurrentPicPosition >= 0 && mCurrentPicPosition < mPicList.size() - 1) {
                mCurrentPicPosition = mCurrentPicPosition + 1;
            } else {
                mCurrentPicPosition = 0;
            }
            startChangePic();
        }
        System.out.println("mCurrentPicPosition:" + mCurrentPicPosition);
        invalidate();
    }

    private boolean isSonThanFather() {
        if (mTextWid + 2 * mPicWid + 2 * mTextAndPicOffset > mWid) {
            return true;
        } else {
            return false;
        }
    }
}
