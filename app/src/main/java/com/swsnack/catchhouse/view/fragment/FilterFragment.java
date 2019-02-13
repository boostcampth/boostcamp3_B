package com.swsnack.catchhouse.view.fragment;

import android.app.DatePickerDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentFilterBinding;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterFragment extends DialogFragment {
    private FragmentFilterBinding mBinding;
    private SearchingViewModel mViewModel;
    private final Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDate;
    private EditText mTargetEditText;

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
        mBinding.etFilterDateFrom.setOnClickListener(this::onClickDate);
        mBinding.etFilterDateTo.setOnClickListener(this::onClickDate);
    }

    public void updateDate(View view) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        mTargetEditText.setText(sdf.format(mCalendar.getTime()));
    }

    public void onClickDate(View v) {
        Log.v("csh","onclick");

        if(v == mBinding.etFilterDateTo || v == mBinding.etFilterDateFrom) {
            mTargetEditText = (EditText)v;
            new DatePickerDialog(getContext(), mDate, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }
}
