package com.swsnack.catchhouse.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.databinding.FragmentAddressSearchBinding;
import com.swsnack.catchhouse.util.KeyboardUtil;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                RecyclerView.VERTICAL, false));
        mBinding.rvAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBinding.rvAddress.setAdapter(new AddressListAdapter(getActivity()));

        ((AddressListAdapter) mBinding.rvAddress.getAdapter())
                .setOnItemClickListener((__, i) -> {
                    mViewModel.onSelectAddress(i);
                    this.dismiss();
                });

        mBinding.tvAddressSearch.setOnClickListener(__ -> {
            mViewModel.onSearchAddress(mBinding.etAddressKeyword.getText().toString());
            KeyboardUtil.keyBoardClose(getActivity());
        });
    }
}