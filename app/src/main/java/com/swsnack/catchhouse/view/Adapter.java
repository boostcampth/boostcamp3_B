package com.swsnack.catchhouse.view;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swsnack.catchhouse.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Adapter extends PagerAdapter {

    ArrayList<Uri> mUri;
    private LayoutInflater inflater;

    public Adapter() {
        mUri = new ArrayList<>();
    }

    public void setUriArray(ArrayList<Uri> uri) {
        mUri = uri;
    }

    @Override
    public int getCount() {
        return mUri.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = LayoutInflater.from(container.getContext());
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = v.findViewById(R.id.imageView);
        TextView textView = v.findViewById(R.id.textView);
        try {
            Glide.with(container.getContext())
                    .load(mUri.get(position))
                    .apply(new RequestOptions().override(340, 324))
                    .into(imageView);
            String text = (position + 1) + "번째 이미지";
            textView.setText(text);
            container.addView(v);

        } catch (Exception e) {
        }
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}