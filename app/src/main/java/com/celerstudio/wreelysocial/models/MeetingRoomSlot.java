package com.celerstudio.wreelysocial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MeetingRoomSlot implements Parcelable {

    private Long id;
    @SerializedName("meeting_room_id")
    private Long meetingRoomId;
    @SerializedName("booked_by_member_id")
    private Long bookedByMemberId;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("is_available")
    private int available;
    @SerializedName("member_id")
    private Long memberId;
    @SerializedName("room_id")
    private Long roomId;
    @SerializedName("meeting_room_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Long getBookedByMemberId() {
        return bookedByMemberId;
    }

    public void setBookedByMemberId(Long bookedByMemberId) {
        this.bookedByMemberId = bookedByMemberId;
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

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available == 0 ? false : true;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public MeetingRoomSlot() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.meetingRoomId);
        dest.writeValue(this.bookedByMemberId);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeInt(this.available);
        dest.writeValue(this.memberId);
        dest.writeValue(this.roomId);
        dest.writeString(this.name);
    }

    protected MeetingRoomSlot(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.meetingRoomId = (Long) in.readValue(Long.class.getClassLoader());
        this.bookedByMemberId = (Long) in.readValue(Long.class.getClassLoader());
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.available = in.readInt();
        this.memberId = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<MeetingRoomSlot> CREATOR = new Creator<MeetingRoomSlot>() {
        @Override
        public MeetingRoomSlot createFromParcel(Parcel source) {
            return new MeetingRoomSlot(source);
        }

        @Override
        public MeetingRoomSlot[] newArray(int size) {
            return new MeetingRoomSlot[size];
        }
    };
}
