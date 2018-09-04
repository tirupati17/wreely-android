package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Company implements Parcelable {

    private Long id;

    @SerializedName("contact_person_email_id")
    private String contactPersonEmailId;

    @SerializedName("contact_person_name")
    private String contactPersonName;

    @SerializedName("contact_person_number")
    private String contactPersonNumber;

    private String name;

    @SerializedName("vendor_key")
    private String vendorKey;

    private String website;

    @SerializedName("firebase_id")
    private String firebaseId;

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactPersonEmailId() {
        return contactPersonEmailId;
    }

    public void setContactPersonEmailId(String contactPersonEmailId) {
        this.contactPersonEmailId = contactPersonEmailId;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonNumber() {
        return contactPersonNumber;
    }

    public void setContactPersonNumber(String contactPersonNumber) {
        this.contactPersonNumber = contactPersonNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorKey() {
        return vendorKey;
    }

    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.contactPersonEmailId);
        dest.writeString(this.contactPersonName);
        dest.writeString(this.contactPersonNumber);
        dest.writeString(this.name);
        dest.writeString(this.vendorKey);
        dest.writeString(this.website);
        dest.writeString(this.firebaseId);
    }

    protected Company(Parcel in) {
        this.id = in.readLong();
        this.contactPersonEmailId = in.readString();
        this.contactPersonName = in.readString();
        this.contactPersonNumber = in.readString();
        this.name = in.readString();
        this.vendorKey = in.readString();
        this.website = in.readString();
        this.firebaseId = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
