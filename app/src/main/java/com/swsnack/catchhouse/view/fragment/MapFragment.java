package com.swsnack.catchhouse.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.adapter.SimpleDividerItemDecoration;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.databinding.FragmentMapBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.PostActivity;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

import static com.swsnack.catchhouse.Constant.INTENT_ROOM;

public class MapFragment extends BaseFragment<FragmentMapBinding, SearchingViewModel> {
    private FragmentManager mFragmentManager;
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
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
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

        mTMapView.setOnCalloutRightButtonClickListener(tMapMarkerItem -> {
            Room room = getViewModel().getRoomList().getValue().get(Integer.parseInt(tMapMarkerItem.getID()));
            Intent intent = new Intent(getActivity(), PostActivity.class);
            intent.putExtra(INTENT_ROOM, room);
            /*
            intent.putExtra(INTENT_LAT, room.getLatitude());
            intent.putExtra(INTENT_LON, room.getLongitude());*/
            startActivity(intent);
        });




/*
        mTMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Log.v("csh","onPressEvent");
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                if(arrayList.size() > 0) {
                    if(arrayList.get(0).getAutoCalloutVisible()==false) {
                        return false;
                    }
                    RoomData roomData = getViewModel().getRoomDataList().getValue().get(Integer.parseInt(arrayList.get(0).getID()));
                    Room room = new Room(roomData.getPrice(), roomData.getFrom(), roomData.getTo(),
                            roomData.getTitle(), roomData.getContent(), roomData.getImages(),
                            roomData.getUUID(), roomData.getSize(), roomData.getAddress(),
                            roomData.getAddressName(), roomData.isOptionStandard(), roomData.isOptionGender(),
                            roomData.isOptionPet(), roomData.isOptionSmoking());

                    Intent intent = new Intent(getActivity(), PostActivity.class);
                    intent.putExtra(INTENT_ROOM, room);
                    intent.putExtra(INTENT_LAT, roomData.getLatitude());
                    intent.putExtra(INTENT_LON, roomData.getLongitude());
                    startActivity(intent);
                }
                return false;
            }
        });*/

        getBinding().etMapSearch.setOnEditorActionListener((v, id, event) -> {
            if (v.getId() == R.id.et_map_search && id == EditorInfo.IME_ACTION_DONE) {
                getViewModel().searchAddress();
            }
            return false;
        });

        adapter.setOnItemClickListener(((v, position) -> {
            getBinding().rvMapAddress.setVisibility(View.GONE);
            moveMap(adapter.getItem(position));

            updateMap(adapter.getItem(position).getLatitude(), adapter.getItem(position).getLongitude());
        }));

        getBinding().btFilter.setOnClickListener(__ -> {
            new FilterFragment().show(mFragmentManager, "address selection");
            Log.v("csh","aaa");
        });

        getViewModel().getRoomList()
                .observe(this, roomList -> {
                    Log.v("csh","변화감지");
                    mTMapView.removeAllMarkerItem();
                    for(int i=0; i<roomList.size(); i++) {
                        addMarker(roomList.get(i), i);
                    }
                });

        getBinding().btMapNear.setOnClickListener(__ -> {
            TMapPoint point = mTMapView.getCenterPoint();

            updateMap(point.getLatitude(), point.getLongitude());
        });

        getViewModel().getFilterUpdate()
                .observe(this, filterUpdate -> {
                    Log.v("csh","FilterUpdate 감지" + filterUpdate);
                    TMapPoint point = mTMapView.getCenterPoint();

                    updateMap(point.getLatitude(), point.getLongitude());
                });

    }

    private void updateMap(double latitude, double longitde) {
        mTMapView.removeAllTMapCircle();
        mTMapView.removeAllMarkerItem();

        addCircle(latitude, longitde);
        getViewModel().updateData(latitude, longitde);
    }

    private void addCircle(double latitude, double longitude) {
        TMapPoint point = new TMapPoint(latitude, longitude);
        TMapCircle circle = new TMapCircle();
        circle.setCenterPoint(point);
        circle.setAreaColor(Color.CYAN);
        circle.setAreaAlpha(1000000);
        circle.setLineAlpha(-1);
        circle.setRadius(getViewModel().getFilterDistance()*1000);
        mTMapView.addTMapCircle("circle", circle);

    }

    private void addMarker(Room room, int index) {
        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(room.getLatitude(), room.getLongitude());
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        markerItem.setCanShowCallout(true);

        //markerItem.setCalloutTitle(roomData.getTitle());
        //markerItem.setCalloutSubTitle(roomData.getContent());
        //FIXME : model에 bitmap을 가지고 있을 필요가 없다 생각해서, 기능 동작을 위해서 이렇게 우선 해뒀어요. 고쳐주세요
        Glide.with(getContext()).asBitmap().load(room.getImages().get(0)).apply(new RequestOptions().override(320, 180).centerCrop())
                .listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                markerItem.setCalloutRightButtonImage(resource);
                return false;
            }
        }).submit();

        //markerItem.setAutoCalloutVisible(true);
        mTMapView.addMarkerItem(String.valueOf(index), markerItem);

    }

    private void moveMap(Address address) {
        mTMapView.setCenterPoint(address.getLongitude(), address.getLatitude(), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }


}