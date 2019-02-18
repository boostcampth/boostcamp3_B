package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.ImagePagerAdapter;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.AppDataManager;
import com.swsnack.catchhouse.repository.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.repository.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.repository.room.RoomRepository;
import com.swsnack.catchhouse.repository.searching.remote.AppSearchingDataManager;
import com.swsnack.catchhouse.repository.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.databinding.ActivityPostBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModelFactory;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;

import static com.swsnack.catchhouse.Constant.FirebaseKey.UUID;
import static com.swsnack.catchhouse.Constant.INTENT_ROOM;

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

        AppBarLayout appBarLayout = getBinding().appbarLayout;
        appBarLayout.addOnOffsetChangedListener((__, offset) -> {

            Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
            if (offset < -200) {
                upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                getBinding().tbPost.setNavigationIcon(upArrow);
            } else {
                upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                getBinding().tbPost.setNavigationIcon(upArrow);
            }

        });

        getBinding().tbPost.setNavigationOnClickListener(__ ->
                finish()
        );

        Room room = getIntent().getParcelableExtra(INTENT_ROOM);
        getBinding().setRoomData(room);
        mViewModel.setRoomData(room);
        double longitude = room.getLongitude();
        double latitude = room.getLatitude();

        mTMapView = new TMapView(this);
        getBinding().llPostTmapContainer.addView(mTMapView);

        getBinding().vpPost.setAdapter(
                new ImagePagerAdapter(mViewModel.mImageList.getValue(), mViewModel, getSupportFragmentManager())
        );
        getBinding().tabPost.setupWithViewPager(getBinding().vpPost, true);

        getBinding().tvPostChatting.setOnClickListener(__ -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Snackbar.make(getBinding().getRoot(), R.string.not_singed, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(this, ChattingMessageActivity.class);
                    intent.putExtra(UUID, room.getUuid());
                    startActivity(intent);
                }
        );

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
                                RoomRepository.getInstance(),
                                AppLocationDataManager.getInstance(),
                                AppSearchingDataManager.getInstance()),
                        APIManager.getInstance(),
                        this))
                .get(PostViewModel.class);
    }
}
