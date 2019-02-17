package com.swsnack.catchhouse.view.fragment;

import androidx.lifecycle.ViewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentHomeBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.BottomNavActivity;
import com.swsnack.catchhouse.view.activitity.WriteActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, ViewModel> {

    private HomeFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavActivity) {
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
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                new AlertDialog.Builder(getContext())
                        .setMessage("로그인 후 이용가능합니다. \n 로그인 화면으로 이동하겠습니까?")
                        .setPositiveButton(R.string.dialog_positive, ((dialog, which) ->
                                Snackbar.make(getBinding().getRoot(), "구현안함. 액티비티로 변환할 예정. \n리스너 동작대신, 액티비티 전환으로 처리", Snackbar.LENGTH_LONG).show()))
                        .setNeutralButton(R.string.negative, ((dialog, which) -> dialog.dismiss()))
                        .show();
                return;
            }
            startActivity(new Intent(getContext(), WriteActivity.class));
        });
        getBinding().tvHomeSearch.setOnClickListener(v -> mListener.openMapFragment());
    }

}
