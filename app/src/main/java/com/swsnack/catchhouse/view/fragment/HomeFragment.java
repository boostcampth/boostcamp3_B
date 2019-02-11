package com.swsnack.catchhouse.view.fragments;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentHomeBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activities.BottomNavActivity;
import com.swsnack.catchhouse.view.activities.WriteActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, ViewModel>  {

    private HomeFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof BottomNavActivity) {
            mListener = (HomeFragmentListener) getActivity();
        } else {
            throw new RuntimeException("not implements HomeFragmentListener");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<ViewModel> getViewModelClass() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvHomePost.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getContext(), WriteActivity.class));
        });
        getBinding().tvHomeSearch.setOnClickListener(v -> mListener.openMapFragment());
    }

}
