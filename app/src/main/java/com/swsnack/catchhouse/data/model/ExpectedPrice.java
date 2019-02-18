package com.swsnack.catchhouse.data.model;

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

    private String expectedPrice;
    private String price;
    private String fromDate;
    private String toDate;
    private int diffDays;

    public ExpectedPrice(String price, String from, String to) {
        this.price = price;
        this.fromDate = from;
        this.toDate = to;

        onChangePriceAndInterval();
    }

    public String update(String price, String from, String to) {
        this.price = price;
        this.fromDate = from;
        this.toDate = to;

        onChangePriceAndInterval();

        return expectedPrice;
    }

    public void onChangePriceAndInterval() {
        if (isPriceAndDateValid()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date fromDate = formatter.parse(this.fromDate);
                Date toDate = formatter.parse(this.toDate);
                diffDays = DateCalculator.getDiffDate(fromDate, toDate);

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                expectedPrice = myFormatter.format(Integer.parseInt(price) * diffDays) +
                        "원" + "  (" + diffDays + "박)";
            } catch (Exception e) {
                Log.e(TAG, "parse error", e);
            }
        } else {
            expectedPrice = "";
        }
    }


    public int getDiffDays() {
        return diffDays;
    }

    public String getExpectedPrice() {
        return expectedPrice;
    }

    private boolean isPriceAndDateValid() {

        return (!TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(fromDate) &&
                !TextUtils.isEmpty(toDate) &&
                !TextUtils.equals(fromDate, DEFAULT_DATE_STRING) &&
                !TextUtils.equals(toDate, DEFAULT_DATE_STRING)
        );
    }
}
