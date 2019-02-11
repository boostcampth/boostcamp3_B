package com.swsnack.catchhouse.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressBindingAdapter;
import com.swsnack.catchhouse.databinding.FragmentAddressSearchBinding;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

public class AddressSearchFragment extends DialogFragment {

    FragmentAddressSearchBinding mBinding;
    RoomsViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_search, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            mViewModel = ViewModelProviders.of(getActivity()).get(RoomsViewModel.class);
        } else {
            throw new RuntimeException(this.getClass().getName() + "has null activity");
        }

        mBinding.setHandler(mViewModel);
        mBinding.setLifecycleOwner(getActivity());

        /* recycler view set */
        mBinding.rvAddress.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayout.VERTICAL, false));
        mBinding.rvAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBinding.rvAddress.setAdapter(new AddressBindingAdapter(getActivity()));

        ((AddressBindingAdapter) mBinding.rvAddress.getAdapter())
                .setOnItemClickListener((__, i) -> {
                    mViewModel.onSelectAddress(i);
                    this.dismiss();
                });
    }
}