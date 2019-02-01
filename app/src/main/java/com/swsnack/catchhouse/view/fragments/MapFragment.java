package com.swsnack.catchhouse.view.fragments;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.AddressBindingAdapter;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.databinding.ItemMapAddressBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.annotations.Nullable;

public class MapFragment extends BaseFragment<FragmentMapBinding, SearchViewModel> {
    public FragmentManager mFragmentManager;

    @Override
    protected int setLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected Class setViewModel() {
        return SearchViewModel.class;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentManager = getActivity().getSupportFragmentManager();

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());
        getBinding().rvMapAddress.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        getBinding().rvMapAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        AddressBindingAdapter adapter = new AddressBindingAdapter(getContext());
        getBinding().rvMapAddress.setAdapter(adapter);
        adapter.updateItems(getViewModel().getAddressList().getValue());

        /* Tmap 연동 */
        TMapView tmapview = new TMapView(getContext());
        tmapview.setSKTMapApiKey(getResources().getString(R.string.tmap_api_key));
        ConstraintLayout mapLayout = getBinding().clMap;
        mapLayout.addView(tmapview);

        getBinding().rvMapAddress.bringToFront();

        final Observer<List<Address>> addressObserver = (__ -> {
            Log.v("csh","addressList is Changed");
            adapter.updateItems(getViewModel().getAddressList().getValue());
        });

        getViewModel().getAddressList().observe(this, addressObserver);

        getBinding().etMapSearch.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                getViewModel().searchAddress();
                getBinding().rvMapAddress.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });

        adapter.setOnItemClickListener(((v, position) -> {

        }));
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
