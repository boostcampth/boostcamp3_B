package com.swsnack.catchhouse.view.fragment;

import android.app.DatePickerDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.SeekBar;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentFilterBinding;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterFragment extends DialogFragment {
    private FragmentFilterBinding mBinding;
    private SearchingViewModel mViewModel;
    private final Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDate;
    private EditText mTargetEditText;

    private DecimalFormat mDecimalFormat;
    private String mStringResult;
    private EditText mPriceEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            mViewModel = ViewModelProviders.of(getActivity()).get(SearchingViewModel.class);
        } else {
            throw new RuntimeException(this.getClass().getName() + "has null activity");
        }

        mBinding.setHandler(mViewModel);
        mBinding.setLifecycleOwner(getActivity());

        mDate = (v, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate(v);
        };
        mBinding.etFilterDateFrom.setOnClickListener(this::onClickEditText);
        mBinding.etFilterDateTo.setOnClickListener(this::onClickEditText);
        mBinding.etFilterPriceFrom.setOnFocusChangeListener(this::onFocusChange);
        mBinding.etFilterPriceTo.setOnFocusChangeListener(this::onFocusChange);

        mDecimalFormat = new DecimalFormat("#,###");
        mStringResult = "";
        mPriceEditText = mBinding.etFilterPriceFrom;

        /*
        mBinding.sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updatePreview(progress);
            }
        });
*/

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(mStringResult)){
                    mStringResult = mDecimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    mPriceEditText.setText(mStringResult);
                    mPriceEditText.setSelection(mStringResult.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mBinding.etFilterPriceFrom.addTextChangedListener(watcher);
        mBinding.etFilterPriceTo.addTextChangedListener(watcher);
        mBinding.btFilterApply.setOnClickListener(v -> {
            //mViewModel.mFilterUpdate.setValue(true);
            mViewModel.onUpdateMap();
            dismiss();
        });
    }

    public void updateDate(View view) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        mTargetEditText.setText(sdf.format(mCalendar.getTime()));
    }

    public void onClickEditText(View v) {
        if(v == mBinding.etFilterDateTo || v == mBinding.etFilterDateFrom) {
            mTargetEditText = (EditText)v;
            new DatePickerDialog(getContext(), mDate, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if(v == mBinding.etFilterPriceFrom || v == mBinding.etFilterPriceTo) {
            mPriceEditText = (EditText)v;
        }
    }

}
