package com.swsnack.catchhouse.view.fragment;

import android.content.Intent;
import android.graphics.PointF;
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

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.data.pojo.RoomData;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public class MapFragment extends BaseFragment<FragmentMapBinding, SearchingViewModel> {
    public FragmentManager mFragmentManager;
    private TMapView mTMapView;
    private CompositeDisposable mDisposable;

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected Class<SearchingViewModel> getViewModelClass() {
        return SearchingViewModel.class;
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
        getBinding().setLifecycleOwner(this);
        getBinding().rvMapAddress.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        getBinding().rvMapAddress.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        AddressListAdapter adapter = new AddressListAdapter(getContext());

        /* Tmap 연동 */
        mTMapView = new TMapView(getContext());
        ConstraintLayout mapLayout = getBinding().clMap;
        mapLayout.addView(mTMapView);
        getBinding().rvMapAddress.setAdapter(adapter);
        getBinding().rvMapAddress.bringToFront();

        mTMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Log.v("csh","onPressEvent");
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //TMapMarkerItem item = arrayList.get(0);
                // TODO : PostActivity에 Room을 담아서 startActivity
                //Log.v("csh","detail:"+getViewModel().getRoomDataList().getValue().get(Integer.parseInt(item.getID())).getContent());

                return false;
            }
        });

        getBinding().etMapSearch.setOnEditorActionListener((v, id, event) -> {
            if (v.getId() == R.id.et_map_search && id == EditorInfo.IME_ACTION_DONE) {
                getViewModel().searchAddress();
            }
            return false;
        });

        adapter.setOnItemClickListener(((v, position) -> {
            getBinding().rvMapAddress.setVisibility(View.GONE);
            moveMap(adapter.getItem(position));
            getViewModel().updateData(adapter.getItem(position).getName(), adapter.getItem(position).getLatitude(), adapter.getItem(position).getLongitude());
        }));
        getBinding().btFilter.setOnClickListener(__ -> {
            //Intent intent = new Intent(getContext(), FilterPopUpActivity.class);


            //intent.putExtra(Constant.INTENT_FILTER, getViewModel().getFilterFromRepository());
            //startActivityForResult(intent, Constant.FILTER);
            new FilterFragment().show(mFragmentManager, "address selection");

        });

        getViewModel().getRoomDataList()
                .observe(this, roomDataList -> {
                    Log.v("csh","변화감지");
                    mTMapView.removeAllMarkerItem();
                    for(int i=0; i<roomDataList.size(); i++) {
                        addMarker(roomDataList.get(i), i);
                    }
                });

        getViewModel().mFilterPriceFrom
                .observe(this, __ -> {
                    Log.v("csh","Change:"+getViewModel().mFilterPriceFrom.getValue());
                });
    }

    private void addMarker(RoomData roomData, int index) {

        //TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(roomData.getLatitude(), roomData.getLongitude());
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        markerItem.setCanShowCallout(true);

        markerItem.setCalloutTitle(roomData.getTitle());
        markerItem.setCalloutSubTitle(roomData.getContent());


        markerItem.setAutoCalloutVisible(true);
        mTMapView.addMarkerItem(String.valueOf(index), markerItem);

        Log.v("csh", "마커추가:" + roomData.getLatitude() + "," + roomData.getLongitude());
    }

    /*
    private void addMarker(Room room) {

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(room.get, room.getAddress().getLongitude());
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        markerItem.setCanShowCallout(true);
        markerItem.setCalloutTitle("제목");
        markerItem.setCalloutSubTitle("부제목");
        markerItem.setAutoCalloutVisible(true);
        mTMapView.addMarkerItem(String.valueOf(room.getAddress().getLatitude()), markerItem);
        Log.v("csh", "마커추가:" + room.getAddress().getLatitude() + "," + room.getAddress().getLongitude());
    }*/

    private void moveMap(Address address) {
        mTMapView.setCenterPoint(address.getLongitude(), address.getLatitude(), true);

        /*

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(address.getLatitude(), address.getLongitude());
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        markerItem.setCanShowCallout(true);
        markerItem.setCalloutTitle("제목");
        markerItem.setCalloutSubTitle("부제목");
        markerItem.setAutoCalloutVisible(true);
        mTMapView.addMarkerItem("1", markerItem);*/
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