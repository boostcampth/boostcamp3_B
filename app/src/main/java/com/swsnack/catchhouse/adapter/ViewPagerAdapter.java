package com.swsnack.catchhouse.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> itemViews;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (itemViews == null) {
            return null;
        }
        return itemViews.get(i);
    }

    @Override
    public int getCount() {
        if (itemViews == null) {
            return 0;
        }
        return itemViews.size();
    }

    public void addItem(Fragment itemView) {
        if (itemViews == null) {
            itemViews = new ArrayList<>();
        }
        itemViews.add(itemView);
        notifyDataSetChanged();
    }

    public void setItems(List<Fragment> newItemViews) {
        itemViews = newItemViews;
        notifyDataSetChanged();
    }

    public void changeItem(int position, Fragment newItem) {
        if (itemViews == null) {
            itemViews = new ArrayList<>();
        }

        if (itemViews.size() < position) {
            throw new ArrayIndexOutOfBoundsException("pager item size is " + itemViews.size());
        }

        itemViews.remove(position);
        itemViews.add(position, newItem);
        notifyDataSetChanged();
    }
}
