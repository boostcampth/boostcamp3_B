package com.boostcampa2.catchhouse.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boostcampa2.catchhouse.R;

public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();

    private TextView mSearchButton;
    private TextView mPostArticleButton;
    private OnSearchButtonListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchButton = view.findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(__ -> mListener.onClicked());

        mPostArticleButton = view.findViewById(R.id.post_article_btn);
        mPostArticleButton.setOnClickListener(__ -> Log.i(TAG, "start write activity"));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnSearchButtonListener) context;
    }

    public interface OnSearchButtonListener {
        void onClicked();
    }
}
