package com.musicscrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by HARRY on 2019/5/11.
 */

public abstract class MusciScrollBaseItem extends FrameLayout {
    private LayoutInflater mInflater;
    private View itemRoot;

    public MusciScrollBaseItem(@NonNull Context context) {
        this(context,null);
    }

    public MusciScrollBaseItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MusciScrollBaseItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mInflater = LayoutInflater.from(context);
        initView(context, attrs);
        initData();
        addView(itemRoot);
    }

    private void initView(Context context, AttributeSet attrs) {
        itemRoot = mInflater.inflate(getLayout(), this, false);
    }

    protected abstract int getLayout();

    protected abstract void initData();

    public View getItemRoot() {
        return itemRoot;
    }
}
