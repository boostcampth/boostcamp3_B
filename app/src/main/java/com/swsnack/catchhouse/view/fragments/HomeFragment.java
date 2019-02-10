package com.swsnack.catchhouse.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentHomeBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.homeviewmodel.HomeViewModel;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    public static final String TAG = HomeFragment.class.getName();

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());
    }
}
