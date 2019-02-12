package com.swsnack.catchhouse.data.roomdata.model;

import android.text.TextUtils;
import android.util.Log;

import com.swsnack.catchhouse.util.DateCalculator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.swsnack.catchhouse.Constant.DEFAULT_DATE_STRING;

public class ExpectedPrice {

    private static final String TAG = ExpectedPrice.class.getSimpleName();

    private String mExpectedPrice;
    private int mDiffDays;
    private String mPrice;
    private String mFromDate;
    private String mToDate;

    public ExpectedPrice(String price, String from, String to) {
        mPrice = price;
        mFromDate = from;
        mToDate = to;

        onChangePriceAndInterval();
    }

    public String updatePrice(String price) {
        mPrice = price;

        return onChangePriceAndInterval();
    }

    public String updateFromDate(String from) {
        mFromDate = from;

        return onChangePriceAndInterval();
    }

    public String updateToDate(String to) {
        mToDate = to;

        return onChangePriceAndInterval();
    }

    public String onChangePriceAndInterval() {

        if (isPriceAndDateValid()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date fromDate = formatter.parse(mFromDate);
                Date toDate = formatter.parse(mToDate);
                mDiffDays = DateCalculator.getDiffDate(fromDate, toDate);

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                mExpectedPrice = myFormatter.format(Integer.parseInt(mPrice) * mDiffDays) +
                        "원" + "  (" + mDiffDays + "박)";

                return mExpectedPrice;
            } catch (Exception e) {
                Log.e(TAG, "parse error", e);
                return "";
            }
        } else {
            return "";
        }
    }

    private boolean isPriceAndDateValid() {

        return (!TextUtils.isEmpty(mPrice) &&
                !TextUtils.isEmpty(mFromDate) &&
                !TextUtils.isEmpty(mToDate) &&
                !TextUtils.equals(mFromDate, DEFAULT_DATE_STRING) &&
                !TextUtils.equals(mToDate, DEFAULT_DATE_STRING)
        );
    }
}
