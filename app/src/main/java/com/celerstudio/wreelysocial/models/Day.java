package com.celerstudio.wreelysocial.models;

public class Day {

    private String month;
    private int dayOfMonth;
    private String day;
    private String date;
    private boolean checked;

    public Day() {
    }

    public Day(String month, int dayOfMonth, String day) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
