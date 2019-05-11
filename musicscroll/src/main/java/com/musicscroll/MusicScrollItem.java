package com.musicscroll;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HARRY on 2019/5/11.
 */

public class MusicScrollItem extends MusciScrollBaseItem {
    private String mText;
    private int mTextColor;
    private TextView mTvText;
    private ImageView mIvFirst;
    private ImageView mIvSecond;

    public MusicScrollItem(@NonNull Context context) {
        this(context, null);
    }

    public MusicScrollItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicScrollItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_music_scroll;
    }

    @Override
    protected void initData() {
        AnimationDrawable animationDrawable11 = (AnimationDrawable) getResources().getDrawable(R.drawable.music);
        AnimationDrawable animationDrawable12 = (AnimationDrawable) getResources().getDrawable(R.drawable.music);

        mTvText = (TextView) getItemRoot().findViewById(R.id.tv_text);
        mIvFirst = (ImageView) getItemRoot().findViewById(R.id.iv_music_first);
        mIvSecond = (ImageView) getItemRoot().findViewById(R.id.iv_music_second);
        mTvText.setText(getmText());
        mTvText.setTextColor(getmTextColor());
        mIvFirst.setBackgroundDrawable(animationDrawable11);
        mIvSecond.setBackgroundDrawable(animationDrawable12);

        animationDrawable11.start();
        animationDrawable12.start();
    }

    public String getmText() {
        return mText;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setText(String text) {
        this.mText = text;
        mTvText.setText(mText);
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mTvText.setTextColor(mTextColor);
    }

    public void setTextSize(float textSize) {
        mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}
