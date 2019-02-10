package com.swsnack.catchhouse.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentHomeBinding;
import com.swsnack.catchhouse.view.activities.WriteActivity;

// FIXME HomeFragment도 BaseFragment를 상속받도록 해주세요
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();

    private FragmentHomeBinding mBinding;
    private OnSearchButtonListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.tvHomeSearch.setOnClickListener(__ ->
                mListener.onClicked()
        );
        
        mBinding.tvHomePost.setOnClickListener(__ ->
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
