package com.musicscrollview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by HARRY on 2019/5/8.
 */

public class MusicScrollView extends RelativeLayout {
    private Paint mPaint = new Paint();
    private int mTextColor;
    private String mText;
    private float mTextSize;
    private LayoutInflater mInflater;
    private int mHeight;
    private int mItemWid;
    private int mWidth;
    private ObjectAnimator animator2;
    private ObjectAnimator animator;
    private boolean needInitAnim = true;
    private float mPadding = 100;

    public MusicScrollView(Context context) {
        this(context, null);
    }

    public MusicScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setClipChildren(false);
        this.mInflater = LayoutInflater.from(context);
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MusicScrollView);
        if (array != null) {
            mTextColor = array.getColor(R.styleable.MusicScrollView_textColor,
                    ContextCompat.getColor(context, R.color.white));
            mText = array.getString(R.styleable.MusicScrollView_text);
            mTextSize = array.getDimension(R.styleable.MusicScrollView_textSize, 26);
            array.recycle();
        }
        setBackground(getResources().getDrawable(R.drawable.music_songnamedisplay));
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heiMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int heiMode = MeasureSpec.getMode(heightMeasureSpec);
        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int widMeasure = MeasureSpec.getSize(widthMeasureSpec);

        View child1 = getChildAt(0);
        // 子View占据的宽度
        int childWidth = child1.getMeasuredWidth();
        int width = child1.getWidth();
        // 子View占据的高度
        mHeight = child1.getMeasuredHeight();
        mItemWid = childWidth;
//        int wid = (int) ((float) 2 * (float) childWidth);
//        setMeasuredDimension((widMode == MeasureSpec.EXACTLY) ? widMeasure : widMeasure, mHeight);
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //防止多次生成动画
        if (needInitAnim) {
            //开启动画
            //获取第二个子控件
            final View child2 = getChildAt(1);
            int child2Wid = child2.getMeasuredWidth();
            //第二个子控件动画开始的位置
            float start2 = (float) mWidth / (float) 2 + (float) child2Wid / (float) 2;
            //第二个子控件结束的位置
            float end2 = -((float) mWidth / (float) 2 + (float) child2Wid / (float) 2);
            animator2 = ObjectAnimator.ofFloat(child2, "translationX", start2, end2);
            animator2.setDuration((long) (Math.abs(end2 - start2) * 10));
            //设置匀速移动
            animator2.setInterpolator(new LinearInterpolator());
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    child2.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    child2.setVisibility(INVISIBLE);
                }
            });
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //当控件左侧滑动到父控件左侧边缘时候，开启另一个动画
                    float x = child2.getX();
                    if (x < 0 && !animator.isStarted()) {
                        animator.start();
                    }
                }
            });

            //第一个子控件
            final View child1 = getChildAt(0);
            int child1Wid = child1.getMeasuredWidth();
            //第一个子控件动画开始位置
            float start1 = (float) mWidth / (float) 2 + (float) child1Wid / (float) 2;
            //第一个子控件动画结束位置
            float end1 = -((float) mWidth / (float) 2 + (float) child1Wid / (float) 2);
            animator = ObjectAnimator.ofFloat(child1, "translationX", start1, end1);
            animator.setDuration((long) (Math.abs(end1 - start1) * 10));
            animator.setInterpolator(new LinearInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    child1.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    child1.setVisibility(INVISIBLE);
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //当控件左侧滑动到父控件左侧边缘时候，开启另一个动画
                    float x = child1.getX();
                    if (x < 0 && !animator2.isStarted()) {
//                        animator2.start();
                    }
                }
            });
            animator.start();

            needInitAnim = false;
        }
    }

    private void initData() {
        removeAllViews();

        MusicScrollItem musicScrollItem1 = new MusicScrollItem(getContext());
        musicScrollItem1.setText(mText);
        musicScrollItem1.setTextColor(mTextColor);
        musicScrollItem1.setVisibility(INVISIBLE);
        musicScrollItem1.setTextSize(mTextSize);

        MusicScrollItem musicScrollItem2 = new MusicScrollItem(getContext());
        musicScrollItem2.setText(mText);
        musicScrollItem2.setTextColor(mTextColor);
        musicScrollItem2.setVisibility(INVISIBLE);
        musicScrollItem2.setTextSize(mTextSize);

        addView(musicScrollItem1);
        addView(musicScrollItem2);

        setGravity(Gravity.CENTER);
    }
}
