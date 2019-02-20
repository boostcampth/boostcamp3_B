package com.swsnack.catchhouse.view.activitity;

import android.app.AlertDialog;
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
import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.AppDataManager;
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
import static com.swsnack.catchhouse.Constant.MODIFY;

public class PostActivity extends BaseActivity<ActivityPostBinding> {

    private PostViewModel mViewModel;
    private TMapView mTMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
        viewInit();

        setObservableData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_post;
    }


    @Override
    public void onSuccess(String success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(getString(R.string.dl_delete_finish))
                .setOnDismissListener(__ -> finish())
                .setPositiveButton(getString(R.string.dl_write_ok), (__, ___) -> finish());

        AlertDialog alert = builder.create();

        if (!isFinishing()) {
            alert.show();
        }
    }

    private void viewInit() {
        Room room = getIntent().getParcelableExtra(INTENT_ROOM);

        if (room.isDeleted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.dl_deleted_post))
                    .setOnDismissListener(__ -> finish())
                    .setPositiveButton(getString(R.string.dl_write_ok), (__, ___) -> finish());
            AlertDialog alert = builder.create();
            if (!isFinishing()) {
                alert.show();
            }
        } else {
            mViewModel.setRoomData(room);

            setToolbar();
            setViewPager();
            setListener();
            setTmapView();
        }
    }

    private void setObservableData() {
        mViewModel.room.observe(this, room ->
                updateMarker(room.getLongitude(), room.getLatitude())
        );
    }

    private void setViewPager() {
        getBinding().vpPost.setAdapter(
                new ImagePagerAdapter(mViewModel, getSupportFragmentManager())
        );
        getBinding().tabPost.setupWithViewPager(getBinding().vpPost, true);

    }

    private void setTmapView() {
        mTMapView = new TMapView(this);
        getBinding().llPostTmapContainer.addView(mTMapView);
        mTMapView.setUserScrollZoomEnable(true);
    }

    private void updateMarker(double longitude, double latitude) {
        mTMapView.removeAllMarkerItem();

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(latitude, longitude);
        markerItem.setTMapPoint(point);
        markerItem.setPosition(0.5f, 1.0f);
        mTMapView.addMarkerItem("center", markerItem);
        mTMapView.setCenterPoint(longitude, latitude, true);
    }

    private void setToolbar() {
        AppBarLayout appBarLayout = getBinding().appbarLayout;
        appBarLayout.addOnOffsetChangedListener((__, offset) -> {

            Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.back_button_primary, null);
            if (offset < -200) {
                upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                getBinding().tbPost.setNavigationIcon(upArrow);
            } else {
                upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                getBinding().tbPost.setNavigationIcon(upArrow);
            }

        });
    }

    private void setListener() {
        getBinding().tbPost.setNavigationOnClickListener(__ ->
                finish()
        );

        getBinding().tvPostChatting.setOnClickListener(__ -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Snackbar.make(getBinding().getRoot(), R.string.not_singed, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(this, ChattingMessageActivity.class);
                    intent.putExtra(UUID, mViewModel.room.getValue().getUuid());
                    startActivity(intent);
                }
        );

        getBinding().btPostModify.setOnClickListener(__ -> {
                    Intent intent = new Intent(this, WriteActivity.class);
                    intent.putExtra(INTENT_ROOM, mViewModel.room.getValue());
                    startActivityForResult(intent, MODIFY);
                }
        );

    }

    private void createViewModels() {
        mViewModel = ViewModelProviders.of(this,
                new PostViewModelFactory(
                        AppDataManager.getInstance(),
                        APIManager.getInstance(),
                        this))
                .get(PostViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MODIFY && resultCode == RESULT_OK && data != null) {
            Room room = data.getParcelableExtra(INTENT_ROOM);
            mViewModel.setRoomData(room);
        }
    }
}
