package com.swsnack.catchhouse.view.activitity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.ImagePagerAdapter;
import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.db.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.db.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.db.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.data.pojo.Room;
import com.swsnack.catchhouse.databinding.ActivityPostBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModelFactory;

public class PostActivity extends BaseActivity<ActivityPostBinding> {

    private PostViewModel mViewModel;
    private TMapView mTMapView;

    @Override
    protected int getLayout() {
        return R.layout.activity_post;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);

        viewInit();
    }

    private void viewInit() {
        Room room = getIntent().getParcelableExtra("room");

        double longitude = getIntent().getDoubleExtra("longitude", 0);
        double latitude = getIntent().getDoubleExtra("latitude", 0);

        String period = room.getFrom() + " ~ " + room.getTo();
        String size = room.getSize() + "평";

        mTMapView = new TMapView(this);
        getBinding().llPostTmapContainer.addView(mTMapView);

        getBinding().vpPost.setAdapter(
                new ImagePagerAdapter(mViewModel.mImageList.getValue(), mViewModel)
        );

        getBinding().tvPostAddress.setText(room.getAddress());
        getBinding().tvPostTitle.setText(room.getTitle());
        getBinding().tvPostContent.setText(room.getContent());
        getBinding().tvPostPeriod.setText(period);
        getBinding().tvPostSize.setText(size);

        getBinding().tvPostSend.setOnClickListener(__ -> {
            Intent intent = new Intent(this, ChattingMessageActivity.class);
            intent.putExtra("uuid", room.getUUID());
            startActivity(intent);
        });

        setTmapView(longitude, latitude);
        mViewModel.setInitRoomData(room);
    }

    private void setTmapView(double longitude, double latitude) {
        mTMapView.setCenterPoint(longitude, latitude, true);
        mTMapView.setUserScrollZoomEnable(true);

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(latitude, longitude);
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        mTMapView.addMarkerItem("center", markerItem);
    }

    private void createViewModels() {
        mViewModel = ViewModelProviders.of(this,
                new PostViewModelFactory(
                        AppDataManager.getInstance(
                                AppUserDataManager.getInstance(),
                                RemoteChattingManager.getInstance(),
                                AppRoomDataManager.getInstance(),
                                AppLocationDataManager.getInstance()),
                        APIManager.getInstance(),
                        this))
                .get(PostViewModel.class);
    }
}
