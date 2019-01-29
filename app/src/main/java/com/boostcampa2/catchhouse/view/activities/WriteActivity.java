package com.boostcampa2.catchhouse.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.databinding.ActivityWriteBinding;
import com.boostcampa2.catchhouse.view.BaseActivity;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;

public class WriteActivity extends BaseActivity<ActivityWriteBinding> implements ViewModelListener {
    final int PICK_IMAGE_MULTIPLE = 1;

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
        getBinding().tvWriteGallery.setOnClickListener(__ -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                && null != data) {
            /* data to uri list */
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
