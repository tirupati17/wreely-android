package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Member implements Parcelable {

    private Long id;
    private String name;
    @SerializedName("contact_no")
    private String contactNo;
    private String occupation;
    @SerializedName("membership_type_id")
    private Long membershipTypeId = -1l;
    @SerializedName("email_id")
    private String emailId;
    private String flexiCount;
    @SerializedName("profile_image")
    private String profileImage;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFlexiCount() {
        return flexiCount;
    }

    public void setFlexiCount(String flexiCount) {
        this.flexiCount = flexiCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.contactNo);
        dest.writeString(this.occupation);
        dest.writeLong(this.membershipTypeId);
        dest.writeString(this.emailId);
        dest.writeString(this.flexiCount);
        dest.writeString(this.profileImage);
    }

    protected Member(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.contactNo = in.readString();
        this.occupation = in.readString();
        this.membershipTypeId = in.readLong();
        this.emailId = in.readString();
        this.flexiCount = in.readString();
        this.profileImage = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
