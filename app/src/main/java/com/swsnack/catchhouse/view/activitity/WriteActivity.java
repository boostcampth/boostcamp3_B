package com.swsnack.catchhouse.view.activitity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.DeletableImagePagerAdapter;
import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.db.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.db.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.db.room.RoomRepository;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomRemoteDataManager;
import com.swsnack.catchhouse.data.db.searching.remote.AppSearchingDataManager;
import com.swsnack.catchhouse.data.db.user.remote.AppUserDataManager;
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
                .setPositiveButton(getString(R.string.dl_write_ok), (__, ___) ->
                        finish()
                );

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

        /* 데이트 픽커 설정 */
        getBinding().tvWriteDateFrom.setOnClickListener(v -> createDatePicker(v));
        getBinding().tvWriteDateTo.setOnClickListener(v -> createDatePicker(v));

        getBinding().etWriteAddress.setOnClickListener(__ ->
                new AddressSearchFragment().show(getSupportFragmentManager(), "address selection")
        );

        getBinding().tvWritePost.setOnClickListener(__ ->
                mViewModel.onClickPost(
                        getBinding().cbWriteStandard.isChecked(),
                        getBinding().cbWriteGender.isChecked(),
                        getBinding().cbWritePet.isChecked(),
                        getBinding().cbWriteSmoking.isChecked()
                )
        );

        setObservableData();
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
                                AppUserDataManager.getInstance(),
                                RemoteChattingManager.getInstance(),
                                RoomRepository.getInstance(),
                                AppLocationDataManager.getInstance(),
                                AppSearchingDataManager.getInstance()
                        ),
                        APIManager.getInstance(),
                        this
                )).get(RoomsViewModel.class);

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

    private void createDatePicker(View v) {
        TextView textView = (TextView) v;

        DatePickerDialog dialog =
                new DatePickerDialog(this,
                        (__, y, m, d) -> {
                            textView.setText(DateCalculator.createDateString(y, m, d));
                            mViewModel.onChangePriceAndPeriod();
                        },
                        DateCalculator.getYear(),
                        DateCalculator.getMonth(),
                        DateCalculator.getDay()
                );

        dialog.show();
    }

    private void setObservableData() {
        mViewModel.mPrice.observe(this, __ ->
                mViewModel.onChangePriceAndPeriod()
        );
    }
}
