package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MeetingRoom implements Parcelable {

    private int id;
    private String name;
    private String[] images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public MeetingRoom() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeStringArray(this.images);
    }

    protected MeetingRoom(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.images = in.createStringArray();
    }

    public static final Creator<MeetingRoom> CREATOR = new Creator<MeetingRoom>() {
        @Override
        public MeetingRoom createFromParcel(Parcel source) {
            return new MeetingRoom(source);
        }

        @Override
        public MeetingRoom[] newArray(int size) {
            return new MeetingRoom[size];
        }
    };
}
