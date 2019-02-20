package com.swsnack.catchhouse.view.activitity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.DeletableImagePagerAdapter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.databinding.ActivityWriteBinding;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.util.DateCalculator;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragment.AddressSearchFragment;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import static com.swsnack.catchhouse.Constant.INTENT_ROOM;
import static com.swsnack.catchhouse.Constant.RequestCode.PICK_IMAGE_MULTIPLE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_PRICE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_ROOM_SIZE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_TITLE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NETWORK;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_ADDRESS;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_DATE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_IMAGE;

public class WriteActivity extends BaseActivity<ActivityWriteBinding> {

    private RoomsViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();


        /* check modify mode */
        Room room = getIntent().getParcelableExtra(INTENT_ROOM);
        if (room != null) {
            mViewModel.setRoomData(room);
        }

        /* viewpager set */
        getBinding().vpWrite.setAdapter(new DeletableImagePagerAdapter(mViewModel.mImageList.getValue(), mViewModel));
        getBinding().tabWrite.setupWithViewPager(getBinding().vpWrite, true);

        setClickListener();
        setObservableData();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_write;
    }

    @Override
    public void onError(String errorMessage) {

        clearErrorMessage();

        switch (errorMessage) {

            case ERROR_NO_SELECTION_IMAGE:
                Snackbar.make(getBinding().getRoot(), getString(R.string.empty_image_error)
                        , Snackbar.LENGTH_SHORT).show();
                break;

            case ERROR_NETWORK:
                Snackbar.make(getBinding().getRoot(), getString(R.string.network_error)
                        , Snackbar.LENGTH_SHORT).show();
                break;

            case ERROR_EMPTY_PRICE:
                getBinding().etWriteValue.setError(getString(R.string.empty_price_error));
                getBinding().etWriteValue.requestFocus();
                break;

            case ERROR_NO_SELECTION_DATE:
                getBinding().tvWriteDateTo.setError(getString(R.string.no_selection_date_error));
                getBinding().tvWriteDateFrom.setError(getString(R.string.no_selection_date_error));
                getBinding().tvWriteDateFrom.requestFocus();
                break;

            case ERROR_NO_SELECTION_ADDRESS:
                getBinding().etWriteAddress.setError(getString(R.string.no_selection_address));
                getBinding().etWriteAddress.requestFocus();
                break;

            case ERROR_EMPTY_ROOM_SIZE:
                getBinding().etWriteRoomSize.setError(getString(R.string.empty_room_size_error));
                getBinding().etWriteRoomSize.requestFocus();
                break;

            case ERROR_EMPTY_TITLE:
                getBinding().etWriteTitle.setError(getString(R.string.empty_title_error));
                getBinding().etWriteTitle.requestFocus();
                break;
        }
    }

    @Override
    public void isWorking() {
        super.isWorking();

    }

    @Override
    public void isFinished() {
        super.isFinished();
    }

    @Override
    public void onSuccess(String success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(getString(R.string.dl_write_finish))
                .setOnDismissListener(__ -> finish())
                .setPositiveButton(getString(R.string.dl_write_ok), (__, ___) -> {
                            Intent intent = new Intent();
                            intent.putExtra(INTENT_ROOM, mViewModel.room.getValue());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                );

        AlertDialog alert = builder.create();

        if (!isFinishing()) {
            alert.show();
        }
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

            mViewModel.onSelectImage(uriList);
        }
    }

    private void createViewModels() {
        mViewModel = ViewModelProviders.of(this,
                new RoomsViewModelFactory(
                        getApplication(),
                        AppDataManager.getInstance(),
                        APIManager.getInstance(),
                        this
                )).get(RoomsViewModel.class);

        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

    private void createDatePicker(View v) {
        EditText editText = (EditText) v;

        DatePickerDialog dialog =
                new DatePickerDialog(this,
                        (__, y, m, d) -> {
                            editText.setText(DateCalculator.createDateString(y, m, d));
                            if (editText.equals(getBinding().tvWriteDateFrom)) {
                                getBinding().tvWriteDateTo.requestFocus();
                            } else {
                                getBinding().etWriteAddress.requestFocus();
                            }

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

        mViewModel.mAddress.observe(this, __ ->
                getBinding().etWriteRoomSize.requestFocus()
        );
    }

    private void clearErrorMessage() {
        getBinding().etWriteValue.setError(null);
        getBinding().tvWriteDateFrom.setError(null);
        getBinding().tvWriteDateTo.setError(null);
        getBinding().etWriteAddress.setError(null);
        getBinding().etWriteRoomSize.setError(null);
    }

    private void setClickListener() {
        /* back button */
        getBinding().tbWrite.setNavigationIcon(R.drawable.back_button_primary);
        getBinding().tbWrite.setNavigationOnClickListener(__ ->
                finish()
        );

        /* gallery button */
        getBinding().tvWriteGallery.setOnClickListener(__ -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                }
        );

        /* date pick button */
        getBinding().tvWriteDateFrom.setKeyListener(null);
        getBinding().tvWriteDateTo.setKeyListener(null);
        getBinding().tvWriteDateFrom.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.performClick();
                        createDatePicker(v);
                    }
                    return false;
                }
        );
        getBinding().tvWriteDateTo.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.performClick();
                        createDatePicker(v);
                    }
                    return false;
                }
        );

        /* search address button */
        getBinding().etWriteAddress.setKeyListener(null);
        getBinding().etWriteAddress.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.performClick();
                        new AddressSearchFragment().show(getSupportFragmentManager(),
                                "address selection");
                    }
                    return false;
                }

        );

        /* post button */
        getBinding().tvWritePost.setOnClickListener(__ -> mViewModel.onClickPost());
    }
}
