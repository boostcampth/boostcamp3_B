package com.swsnack.catchhouse.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentMyPageBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

public class MyPageFragment extends BaseFragment<FragmentMyPageBinding, UserViewModel> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected Class<UserViewModel> setViewModel() {
        return UserViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());

        getViewModel().getUser();

        getBinding().tvMyPageSignOut.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());

    }
}
