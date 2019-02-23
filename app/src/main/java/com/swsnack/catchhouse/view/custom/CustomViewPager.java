package com.swsnack.catchhouse.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.swsnack.catchhouse.Constant;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == Constant.PAGE_MAP)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

}
