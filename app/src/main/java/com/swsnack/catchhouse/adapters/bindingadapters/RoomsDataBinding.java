package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;

import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;

import java.util.List;

public class RoomsDataBinding {

    // FIXME 특정케이스에 대한 items 세팅 함수인데 items보다는 더 명확한 이름으로 수정해주세요
    @BindingAdapter("items")
    public static void setItems(ViewPager viewPager, List<Uri> items) {
        ImageSlideAdapter adapter;

        adapter = (ImageSlideAdapter) viewPager.getAdapter();
        adapter.setUri(items);
        adapter.notifyDataSetChanged();
    }
}
