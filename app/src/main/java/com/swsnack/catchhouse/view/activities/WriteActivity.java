package com.swsnack.catchhouse.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.databinding.ActivityWriteBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;

import java.util.ArrayList;

public class WriteActivity extends BaseActivity<ActivityWriteBinding> implements ViewModelListener {

    final int PICK_IMAGE_MULTIPLE = 1;
    private RoomsViewModel mViewModel;

    @Override
    protected int setLayout() {
        return R.layout.activity_write;
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void isWorking() {

    }

    @Override
    public void isFinished() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();
        mViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);

        getBinding().tvWriteGallery.setOnClickListener(__ -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Uri> uriList = new ArrayList<>();

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                && null != data) {
            if (data.getData() != null) {
                uriList.add(data.getData());
            } else {
                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uriList.add(clipData.getItemAt(i).getUri());
                }
            }
        }
        mViewModel.gallerySelectionResult(uriList);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void createViewModels() {
        createViewModel(RoomsViewModel.class, new RoomsViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
    }
}
