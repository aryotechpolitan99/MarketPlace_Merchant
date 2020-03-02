package com.aryotech.merchant.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    @SerializedName("productId")
    private long productId;

    @SerializedName("productName")
    private String productNama;

    @SerializedName("productSlug")
    private String productSlug;

    @SerializedName("productQty")
    private int productQty;

    @SerializedName("productImage")
    private String productImage;

    @SerializedName("merchant")
    private Merchant merchant;

    @SerializedName("category")
    private Category category;

    @SerializedName("productPrice")
    private int price;

    @SerializedName("productDesc")
    private String productDesc;

    public Product(long productId, String productNama, String productSlug, int productQty, String productImage, Merchant merchants, Category categories, int price, String productDesc) {
        this.productId = productId;
        this.productNama = productNama;
        this.productSlug = productSlug;
        this.productQty = productQty;
        this.productImage = productImage;
        this.merchant = merchants;
        this.category = categories;
        this.price = price;
        this.productDesc = productDesc;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductNama() {
        return productNama;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getProductDesc() {
        return productDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.productId);
        dest.writeString(this.productNama);
        dest.writeString(this.productSlug);
        dest.writeInt(this.productQty);
        dest.writeString(this.productImage);
        dest.writeParcelable(this.merchant,flags);
        dest.writeParcelable(this.category,flags);
        dest.writeInt(this.price);
        dest.writeString(this.productDesc);
    }

    protected Product(Parcel in){

        this.productId = in.readLong();
        this.productNama = in.readString();
        this.productSlug = in.readString();
        this.productQty = in.readInt();
        this.productImage = in.readString();
        this.merchant = in.readParcelable(Merchant.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.price = in.readInt();
        this.productDesc = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
