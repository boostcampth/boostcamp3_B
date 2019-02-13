package com.swsnack.catchhouse.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressBindingAdapter;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.FilterPopUpActivity;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public class MapFragment extends BaseFragment<FragmentMapBinding, SearchViewModel> {
    public FragmentManager mFragmentManager;
    private TMapView mTMapView;
    private CompositeDisposable mDisposable;

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
        mDisposable = new CompositeDisposable();
        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());
        getBinding().rvMapAddress.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        getBinding().rvMapAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        AddressBindingAdapter adapter = new AddressBindingAdapter(getContext());

        /* Tmap 연동 */
        mTMapView = new TMapView(getContext());
        mTMapView.setSKTMapApiKey(getResources().getString(R.string.tmap_api_key));
        ConstraintLayout mapLayout = getBinding().clMap;
        mapLayout.addView(mTMapView);
        getBinding().rvMapAddress.setAdapter(adapter);
        getBinding().rvMapAddress.bringToFront();

        getBinding().etMapSearch.setOnEditorActionListener((v, id, event) -> {
            if (v.getId() == R.id.et_map_search && id == EditorInfo.IME_ACTION_DONE) {
                mDisposable.add(getViewModel().searchAddress()
                        .subscribe(addressList -> {
                            adapter.updateItems(addressList);
                            getBinding().rvMapAddress.setVisibility(View.VISIBLE);
                        }, exception -> {
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            getBinding().rvMapAddress.setVisibility(View.GONE);
                        }));
            }

            return false;
        });

        adapter.setOnItemClickListener(((v, position) -> {
            getBinding().rvMapAddress.setVisibility(View.GONE);
            getViewModel().setKeyword(adapter.getItem(position).getName());
            moveMap(adapter.getItem(position));
        }));
        getBinding().btFilter.setOnClickListener(__ -> {
            Intent intent = new Intent(getContext(), FilterPopUpActivity.class);
            startActivityForResult(intent, Constant.FILTER);
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
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.FILTER) {
            // TODO: 2019-02-02 팝업에 대한 result 처리 추가 필요
        }

    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}