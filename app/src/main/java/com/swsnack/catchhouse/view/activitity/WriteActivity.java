package com.swsnack.catchhouse.view.activitity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.DeletableImagePagerAdapter;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.chattingdata.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.locationdata.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.roomdata.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.databinding.ActivityWriteBinding;
import com.swsnack.catchhouse.util.DateCalculator;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragment.AddressSearchFragment;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends BaseActivity<ActivityWriteBinding> {

    private static final String TAG = WriteActivity.class.getSimpleName();
    final int PICK_IMAGE_MULTIPLE = 1;
    private RoomsViewModel mViewModel;

    @Override
    protected int getLayout() {
        return R.layout.activity_write;
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(getBinding().getRoot(), "error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void isWorking() {
        super.isWorking();
        getBinding().pgWrite.setVisibility(View.VISIBLE);
        getBinding().getRoot().setAlpha(0.6f);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    public void isFinished() {
        getBinding().pgWrite.setVisibility(View.INVISIBLE);
        getBinding().getRoot().setAlpha(1.0f);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onSuccess(String success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(getString(R.string.dl_write_finish))
                .setOnDismissListener(__ -> finish())
                .setPositiveButton(getString(R.string.dl_write_ok), (__, ___) -> {
                    finish();
                    // for test
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra("room", mViewModel.mRoom);
                    startActivity(intent);
                });

        AlertDialog alert = builder.create();

        if (!isFinishing()) {
            alert.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();

        getBinding().vpWrite.setAdapter(new DeletableImagePagerAdapter(mViewModel.mImageList.getValue(), mViewModel));

        getBinding().tbWrite.setNavigationIcon(R.drawable.action_back);
        getBinding().tbWrite.setNavigationOnClickListener(__ ->
                finish()
        );

        getBinding().tvWriteGallery.setOnClickListener(__ -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                }
        );

        getBinding().tvWriteDateFrom.setOnClickListener(__ ->
                createDatePicker((___, y, m, d) ->
                        mViewModel.onSelectFromDate(y, m, d)
                )
        );

        getBinding().tvWriteDateTo.setOnClickListener(__ ->
                createDatePicker((___, y, m, d) ->
                        mViewModel.onSelectToDate(y, m, d)
                )
        );

        mViewModel.mPrice.observe(this, __ ->
                mViewModel.onChangePriceAndPeriod()
        );

        getBinding().etWriteAddress.setOnClickListener(__ ->
                new AddressSearchFragment().show(getSupportFragmentManager(), "address selection")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> uriList = new ArrayList<>();

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            if (data.getData() != null) {
                uriList.add(data.getData());
            } else if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uriList.add(clipData.getItemAt(i).getUri());
                }
            }
        }
        mViewModel.onSelectImage(uriList);
    }

    private void createViewModels() {
        mViewModel = ViewModelProviders.of(this,
                new RoomsViewModelFactory(
                        getApplication(),
                        AppDataManager.getInstance(
                                AppAPIManager.getInstance(),
                                AppUserDataManager.getInstance(),
                                RemoteChattingManager.getInstance(),
                                AppRoomDataManager.getInstance(),
                                AppLocationDataManager.getInstance()
                        ),
                        this
                )).get(RoomsViewModel.class);

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

    private void createDatePicker(DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener,
                DateCalculator.getYear(),
                DateCalculator.getMonth(),
                DateCalculator.getDay()
        );

        dialog.show();
    }
}
