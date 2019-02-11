package com.swsnack.catchhouse.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<B extends ViewDataBinding, V extends ViewModel> extends Fragment {

    private B mBinding;
    protected V mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            if (getViewModelClass() != null) {
                mViewModel = ViewModelProviders.of(getActivity()).get(getViewModelClass());
            }
        } else {
            throw new RuntimeException(this.getClass().getName() + "has null activity");
        }
    }

    protected abstract int getLayout();

    protected abstract Class<V> getViewModelClass();

    protected B getBinding() {
        return mBinding;
    }

    protected V getViewModel() {
        return mViewModel;
    }

}
