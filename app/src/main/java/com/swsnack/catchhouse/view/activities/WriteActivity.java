package com.swsnack.catchhouse.view.activities;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.databinding.ActivityWriteBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class WriteActivity extends BaseActivity<ActivityWriteBinding> implements ViewModelListener {

    private static final String TAG = WriteActivity.class.getSimpleName();
    final int PICK_IMAGE_MULTIPLE = 1;
    private RoomsViewModel mViewModel;
    private Calendar currentDate;

    @Override
    protected int setLayout() {
        return R.layout.activity_write;
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d(TAG, "error");
        Log.e(TAG, "error", throwable);
    }

    @Override
    public void isWorking() {
        Log.d(TAG, "working...");
    }

    @Override
    public void isFinished() {
        Log.d(TAG, "finished");
    }

    @Override
    public void onSuccess(String success) {
        Log.d(TAG, "success__" + success);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();
        mViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);

        getBinding().vpWrite.setAdapter(new ImageSlideAdapter(mViewModel));

        getBinding().tbWrite.setNavigationIcon(R.drawable.action_back);
        getBinding().tbWrite.setNavigationOnClickListener(__ -> finish());

        getBinding().tvWriteGallery.setOnClickListener(__ -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
        });

        currentDate = new GregorianCalendar(Locale.KOREA);
        getBinding().tvWriteDateFrom.setOnClickListener(v -> createDatePicker((TextView) v));
        getBinding().tvWriteDateTo.setOnClickListener(v -> createDatePicker((TextView) v));
        mViewModel.mRoomValue.observe(this, __ ->
                getBinding().tvWriteExpectedValue.setText(calExpectedValue()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> uriList = new ArrayList<>();

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

    private void createDatePicker(TextView view) {
        DatePickerDialog dialog = new DatePickerDialog(this, (__, y, m, d) -> {
            view.setText(y + "-" + (m + 1) + "-" + d);
            getBinding().tvWriteExpectedValue.setText(calExpectedValue());
        },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private int calcDiffDate(String from, String to) {
        int diffDay = -1;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(from);
            Date endDate = formatter.parse(to);

            long diff = endDate.getTime() - beginDate.getTime();
            diffDay = (int) (diff / (24 * 60 * 60 * 1000));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diffDay;
    }

    private String calExpectedValue() {
        String from = (String) getBinding().tvWriteDateFrom.getText();
        String to = (String) getBinding().tvWriteDateTo.getText();
        String defaultStr = getString(R.string.tv_write_date);
        String value = getBinding().etWriteValue.getText().toString();

        if (defaultStr.equals(from) | defaultStr.equals(to)
                | value.equals("")) {
            return "";
        } else {
            int oneDayValue = Integer.parseInt(getBinding().etWriteValue.getText().toString());
            int diffDay = calcDiffDate(from, to);

            if (diffDay < 0) {
                return "";
            }
            DecimalFormat myFormatter = new DecimalFormat("###,###");
            return myFormatter.format(oneDayValue * diffDay) + "원" + "  (" + diffDay + "박)";
        }
    }
}
