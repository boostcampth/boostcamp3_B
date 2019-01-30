package com.swsnack.catchhouse.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.skt.Tmap.TMapView;

import io.reactivex.annotations.Nullable;

public class MapFragment extends BaseFragment<FragmentMapBinding, RoomsViewModel> {
    public FragmentManager mFragmentManager;

    @Override
    protected int setLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected Class setViewModel() {
        return RoomsViewModel.class;
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

        /* Tmap 연동 */
        TMapView tmapview = new TMapView(getContext());
        tmapview.setSKTMapApiKey(getResources().getString(R.string.tmap_api_key));
        ConstraintLayout mapLayout = getBinding().clMap;
        mapLayout.addView(tmapview);
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
