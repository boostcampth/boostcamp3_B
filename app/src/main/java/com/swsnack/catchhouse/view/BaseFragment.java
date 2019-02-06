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
            mViewModel = ViewModelProviders.of(getActivity()).get(setViewModel());
        } else {
            throw new RuntimeException(this.getClass().getName() + "has null activity");
        }
    }

    protected abstract int getLayout();

    // FIXME setXXX는 통상적으로 void setXXX()와 같이 쓰입니다. 만약 return값이 있는 함수라면 Class<V> getViewModel()이 더 맞습니다. 사용되는 방식에 따라서 함수이름을 수정하거나 return값을 수정해주세요
    protected abstract Class<V> setViewModel();

    protected B getBinding() {
        return mBinding;
    }

    protected V getViewModel() {
        return mViewModel;
    }

}
