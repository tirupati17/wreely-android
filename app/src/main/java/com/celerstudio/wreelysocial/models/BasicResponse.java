package com.celerstudio.wreelysocial.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasicResponse {

    private boolean error;
    private String message;
    private User user;
    @SerializedName("access_token")
    private String accessToken;
    private List<Company> companies;
    private List<Member> members;
    @SerializedName("meeting_rooms")
    private List<MeetingRoom> meetingRooms;
    @SerializedName("meeting_room_bookings")
    private List<MeetingRoomSlot> meetingRoomSlots;
    @SerializedName("meeting_rooms_history")
    private List<MeetingRoomSlot> meetingRoomHistory;
    @SerializedName("member_id")
    private Long memberId;

    public BasicResponse() {
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    public void setMeetingRooms(List<MeetingRoom> meetingRooms) {
        this.meetingRooms = meetingRooms;
    }

    public List<MeetingRoomSlot> getMeetingRoomSlots() {
        return meetingRoomSlots;
    }

    public void setMeetingRoomSlots(List<MeetingRoomSlot> meetingRoomSlots) {
        this.meetingRoomSlots = meetingRoomSlots;
    }

    public List<MeetingRoomSlot> getMeetingRoomHistory() {
        return meetingRoomHistory;
    }

    public void setMeetingRoomHistory(List<MeetingRoomSlot> meetingRoomHistory) {
        this.meetingRoomHistory = meetingRoomHistory;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
