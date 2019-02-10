package com.swsnack.catchhouse.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentHomeBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activities.WriteActivity;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, SearchViewModel> {
    public static final String TAG = HomeFragment.class.getName();

    private OnSearchButtonListener mListener;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<SearchViewModel> getViewModelClass() {
        return SearchViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvHomeSearch.setOnClickListener(__ ->
                mListener.onClicked()
        );

        getBinding().tvHomePost.setOnClickListener(__ ->
                startActivity(new Intent(getActivity(), WriteActivity.class))
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // FIXME 여기서 OnSearchButtonListener는 BottomNavActivity를 예상한것인데 이러한 코드는 좋지 않습니다.
        // 1. context가 무조건 OnSearchButtonListener일것이라고 확신할 수 없음
        // 2. HomeFragment의 생성자로 OnSearchButtonListener를 받아서 사용하는게 더 좋음
        mListener = (OnSearchButtonListener) context;
    }

    public interface OnSearchButtonListener {
        void onClicked();
    }

}
