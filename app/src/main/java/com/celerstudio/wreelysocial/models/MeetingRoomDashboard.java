package com.celerstudio.wreelysocial.models;

import com.google.gson.annotations.SerializedName;

public class MeetingRoomDashboard {
    @SerializedName("booking_slot")
    private int bookingSlot;
    @SerializedName("total_booking_count")
    private int totalBookingCount;
    @SerializedName("month")
    private String month;
    @SerializedName("total_usage")
    private String totalUsage;
    @SerializedName("free_remaining")
    private String freeRemaining;
    @SerializedName("free_credit")
    private String freeCredit;
    @SerializedName("paid_usage")
    private String paidUsage;

    public MeetingRoomDashboard() {
    }

    public int getBookingSlot() {
        return bookingSlot;
    }

    public void setBookingSlot(int bookingSlot) {
        this.bookingSlot = bookingSlot;
    }

    public int getTotalBookingCount() {
        return totalBookingCount;
    }

    public void setTotalBookingCount(int totalBookingCount) {
        this.totalBookingCount = totalBookingCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(String totalUsage) {
        this.totalUsage = totalUsage;
    }

    public String getFreeRemaining() {
        return freeRemaining;
    }

    public void setFreeRemaining(String freeRemaining) {
        this.freeRemaining = freeRemaining;
    }

    public String getFreeCredit() {
        return freeCredit;
    }

    public void setFreeCredit(String freeCredit) {
        this.freeCredit = freeCredit;
    }

    public String getPaidUsage() {
        return paidUsage;
    }

    public void setPaidUsage(String paidUsage) {
        this.paidUsage = paidUsage;
    }
}
