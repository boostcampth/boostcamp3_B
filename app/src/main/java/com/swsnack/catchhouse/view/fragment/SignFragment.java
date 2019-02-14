package com.swsnack.catchhouse.view.fragment;

import androidx.lifecycle.ViewModel;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentSignBinding;
import com.swsnack.catchhouse.view.BaseFragment;

public class SignFragment extends BaseFragment<FragmentSignBinding, ViewModel> {

    private FragmentManager mFragmentManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_sign;
    }

    @Override
    protected Class<ViewModel> getViewModelClass() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void init() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment()).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new MyPageFragment()).commit();
        }
    }
}
