package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event implements Parcelable {
    private Long id;
    private String title;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("description")
    private String description;
    @SerializedName("vendor_id")
    private Long vendorId;
    @SerializedName("header_image_url")
    private String headerImageUrl;
    @SerializedName("is_attending")
    private Boolean isAttending;
    @SerializedName("total_rsvp")
    private Long totalRsvp;
    private List<Member> attendees;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public Boolean getAttending() {
        return isAttending;
    }

    public void setAttending(Boolean attending) {
        isAttending = attending;
    }

    public Long getTotalRsvp() {
        return totalRsvp;
    }

    public void setTotalRsvp(Long totalRsvp) {
        this.totalRsvp = totalRsvp;
    }

    public List<Member> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Member> attendees) {
        this.attendees = attendees;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.description);
        dest.writeValue(this.vendorId);
        dest.writeString(this.headerImageUrl);
        dest.writeValue(this.isAttending);
        dest.writeValue(this.totalRsvp);
        dest.writeTypedList(this.attendees);
    }

    protected Event(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.description = in.readString();
        this.vendorId = (Long) in.readValue(Long.class.getClassLoader());
        this.headerImageUrl = in.readString();
        this.isAttending = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.totalRsvp = (Long) in.readValue(Long.class.getClassLoader());
        this.attendees = in.createTypedArrayList(Member.CREATOR);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
