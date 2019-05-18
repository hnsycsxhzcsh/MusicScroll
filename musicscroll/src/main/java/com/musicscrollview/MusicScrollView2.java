package com.musicscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ChenShaohua on 2019/5/18
 */
public class MusicScrollView2 extends View {
    private Paint mPaint = new Paint();
    private int mTextColor;
    private String mText;
    private float mTextSize;
    private int mWid;
    private int mHei;
    private Bitmap mBackBitmap;

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

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MusicScrollView2);
        if (array != null) {
            mTextColor = array.getColor(R.styleable.MusicScrollView_textColor,
                    ContextCompat.getColor(context, R.color.white));
            mText = array.getString(R.styleable.MusicScrollView_text);
            mTextSize = array.getDimension(R.styleable.MusicScrollView_textSize, 26);
            array.recycle();
        }
        mBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music_songnamedisplay);
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


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);

        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(mText, 0, 0, mPaint);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(mBackBitmap, 0, 0, mPaint);
    }
}
