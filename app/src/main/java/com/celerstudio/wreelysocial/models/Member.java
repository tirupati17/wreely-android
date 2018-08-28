package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private Long id;
    private String name;
    private String contactNo;
    private String occupation;
    private String membershipTypeId;
    private String emailId;
    private String flexiCount;

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

    public String getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(String membershipTypeId) {
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
        dest.writeString(this.membershipTypeId);
        dest.writeString(this.emailId);
        dest.writeString(this.flexiCount);
    }

    protected Member(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.contactNo = in.readString();
        this.occupation = in.readString();
        this.membershipTypeId = in.readString();
        this.emailId = in.readString();
        this.flexiCount = in.readString();
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
