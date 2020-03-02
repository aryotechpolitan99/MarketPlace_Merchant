package com.aryotech.merchant.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Merchant implements Parcelable {

    @SerializedName("merchantId")
    private long merchantId;

    @SerializedName("merchantName")
    private String merchantName;

    @SerializedName("merchantSlug")
    private String merchantSlug;

    public Merchant(long merchantId, String merchantName, String merchantSlug) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantSlug = merchantSlug;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantSlug() {
        return merchantSlug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.merchantId);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantSlug);
    }

    protected Merchant(Parcel in){
        this.merchantId = in.readLong();
        this.merchantName = in.readString();
        this.merchantSlug = in.readString();
    }

    public static final Parcelable.Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel source) {
            return new Merchant(source);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };
}
