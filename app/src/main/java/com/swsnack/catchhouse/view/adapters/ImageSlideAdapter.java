package com.swsnack.catchhouse.view.adapters;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swsnack.catchhouse.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class ImageSlideAdapter extends PagerAdapter {

    ArrayList<Uri> mUri;
    private LayoutInflater inflater;

    public ImageSlideAdapter() {
    }

    public void setUriArray(ArrayList<Uri> uri) {
        mUri = uri;
    }

    public void removeItem(int position) {
        if(mUri.get(position) != null) {
            mUri.remove(position);
        }
    }

    @Override
    public int getCount() {
        if(mUri == null) {
            return 0;
        } else {
            return mUri.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView deleteImageView = view.findViewById(R.id.imageView2);
        deleteImageView.setOnClickListener(__ -> {
            Log.d("Tag_OnClick", Integer.toString(position));
            removeItem(position);
            notifyDataSetChanged();
        });

        TextView textView = view.findViewById(R.id.textView);
        try {
            Glide.with(container.getContext())
                    .load(mUri.get(position))
                    .apply(new RequestOptions().override(340, 324))
                    .into(imageView);
            String text = (position + 1) + "/" + Integer.toString(getCount());
            textView.setText(text);
            container.addView(view);

        } catch (Exception e) {
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        container.invalidate();
    }

    @Override
    public int getItemPosition(@android.support.annotation.NonNull Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }
}