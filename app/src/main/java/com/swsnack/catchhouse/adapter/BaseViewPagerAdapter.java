package com.swsnack.catchhouse.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseViewPagerAdapter<T, VM> extends PagerAdapter {

    protected VM mViewModel;
    protected List<T> mList;

    public BaseViewPagerAdapter(List<T> list, VM viewModel) {
        mViewModel = viewModel;
        mList = list;
    }

    public void setItem(List<T> items) {
        mList = items;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        container.invalidate();
    }
}
