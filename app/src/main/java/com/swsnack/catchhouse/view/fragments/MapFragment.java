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

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.AddressBindingAdapter;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.databinding.ItemMapAddressBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.view.activities.FilterPopUpActivity;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.annotations.Nullable;

public class MapFragment extends BaseFragment<FragmentMapBinding, SearchViewModel> {
    public FragmentManager mFragmentManager;
    private TMapView mTMapView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected Class<SearchViewModel> getViewModelClass() {
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
        //FIXME NPE 원인입니다. 수정 부탁드려요
//        adapter.updateItems(getViewModel().getAddressList().getValue());

        /* Tmap 연동 */
        mTMapView = new TMapView(getContext());
        mTMapView.setSKTMapApiKey(getResources().getString(R.string.tmap_api_key));
        ConstraintLayout mapLayout = getBinding().clMap;
        mapLayout.addView(mTMapView);

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
            getBinding().rvMapAddress.setVisibility(View.GONE);
            Address address = getViewModel().getAddress(position);
            moveMap(address);
        }));
        getBinding().btFilter.setOnClickListener(__ -> {
            Intent intent = new Intent(getContext(), FilterPopUpActivity.class);
            startActivityForResult(intent, Constants.FILTER);
        });

    }

    private void moveMap(Address address) {
        mTMapView.setCenterPoint(address.getLongitude(), address.getLatitude(), true);

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(address.getLatitude(), address.getLongitude());
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        markerItem.setCanShowCallout(true);
        markerItem.setCalloutTitle("제목");
        markerItem.setCalloutSubTitle("부제목");
        markerItem.setAutoCalloutVisible(true);
        mTMapView.addMarkerItem("1", markerItem);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.FILTER) {
            // TODO: 2019-02-02 팝업에 대한 result 처리 추가 필요
        }


    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}