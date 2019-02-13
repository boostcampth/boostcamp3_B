package com.swsnack.catchhouse.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.geofire.GeoLocation;

public class Filter {
    private String priceFrom;
    private String priceTo;
    private String dateFrom;
    private String dateTo;

    private double latitude;
    private double longitude;
    private double distance;

    private boolean optionStandard;
    private boolean optionGender;
    private boolean optionPet;
    private boolean optionSmoking;

    public Filter() {
        this.priceFrom = "";
        this.priceTo = "";
        this.dateFrom = "";
        this.dateTo = "";
        this.latitude = 0;
        this.longitude = 0;
        this.distance = 1;
        this.optionStandard = false;
        this.optionGender = false;
        this.optionPet = false;
        this.optionSmoking = false;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isOptionStandard() {
        return optionStandard;
    }

    public void setOptionStandard(boolean optionStandard) {
        this.optionStandard = optionStandard;
    }

    public boolean isOptionGender() {
        return optionGender;
    }

    public void setOptionGender(boolean optionGender) {
        this.optionGender = optionGender;
    }

    public boolean isOptionPet() {
        return optionPet;
    }

    public void setOptionPet(boolean optionPet) {
        this.optionPet = optionPet;
    }

    public boolean isOptionSmoking() {
        return optionSmoking;
    }

    public void setOptionSmoking(boolean optionSmoking) {
        this.optionSmoking = optionSmoking;
    }

}
