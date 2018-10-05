package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import retrofit2.http.Query;

public class User implements Parcelable {
    private String id;
    private String name;
    @SerializedName("email_id")
    private String email;
    @SerializedName("contact_no")
    private String mobile;
    @SerializedName("about_me")
    private String aboutMe;
    @SerializedName("website_url")
    private String websiteUrl;
    @SerializedName("linkedin_url")
    private String linkedinUrl;
    @SerializedName("instagram_url")
    private String instagramUrl;
    @SerializedName("twitter_url")
    private String twitterUrl;
    @SerializedName("facebook_url")
    private String facebookUrl;
    @SerializedName("company_id")
    private Long companyId;
    @SerializedName("profile_pic_url")
    private String profilePicUrl;
    @SerializedName("membership_type_id")
    private Long membershipTypeId;
    private String accessToken;
    private String companyKey;
    private String occupation;
//    private List<Vendor> vendors;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

//    public List<Vendor> getVendors() {
//        return vendors;
//    }
//
//    public void setVendors(List<Vendor> vendors) {
//        this.vendors = vendors;
//    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.aboutMe);
        dest.writeString(this.websiteUrl);
        dest.writeString(this.linkedinUrl);
        dest.writeString(this.instagramUrl);
        dest.writeString(this.twitterUrl);
        dest.writeString(this.facebookUrl);
        dest.writeValue(this.companyId);
        dest.writeString(this.profilePicUrl);
        dest.writeValue(this.membershipTypeId);
        dest.writeString(this.accessToken);
        dest.writeString(this.companyKey);
        dest.writeString(this.occupation);
//        dest.writeTypedList(this.vendors);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.aboutMe = in.readString();
        this.websiteUrl = in.readString();
        this.linkedinUrl = in.readString();
        this.instagramUrl = in.readString();
        this.twitterUrl = in.readString();
        this.facebookUrl = in.readString();
        this.companyId = (Long) in.readValue(Long.class.getClassLoader());
        this.profilePicUrl = in.readString();
        this.membershipTypeId = (Long) in.readValue(Long.class.getClassLoader());
        this.accessToken = in.readString();
        this.companyKey = in.readString();
        this.occupation = in.readString();
//        this.vendors = in.createTypedArrayList(Vendor.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
