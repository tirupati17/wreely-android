package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vendor implements Parcelable {

    private String firebaseId;
    private String accessToken;
    private String email;
    private String mobile;
    private String name;
    private String vendorLogoUrl;
    private String domainNameReference;
    private List<String> companies;
    private List<String> members;
    private String id;

    public Vendor() {
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorLogoUrl() {
        return vendorLogoUrl;
    }

    public void setVendorLogoUrl(String vendorLogoUrl) {
        this.vendorLogoUrl = vendorLogoUrl;
    }

    public String getDomainNameReference() {
        return domainNameReference;
    }

    public void setDomainNameReference(String domainNameReference) {
        this.domainNameReference = domainNameReference;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firebaseId);
        dest.writeString(this.accessToken);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeString(this.vendorLogoUrl);
        dest.writeString(this.domainNameReference);
        dest.writeStringList(this.companies);
        dest.writeStringList(this.members);
        dest.writeString(this.id);
    }

    protected Vendor(Parcel in) {
        this.firebaseId = in.readString();
        this.accessToken = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.name = in.readString();
        this.vendorLogoUrl = in.readString();
        this.domainNameReference = in.readString();
        this.companies = in.createStringArrayList();
        this.members = in.createStringArrayList();
        this.id = in.readString();
    }

    public static final Creator<Vendor> CREATOR = new Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel source) {
            return new Vendor(source);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };
}
