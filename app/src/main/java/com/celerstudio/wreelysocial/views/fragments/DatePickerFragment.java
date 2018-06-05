package com.celerstudio.wreelysocial.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerFragmentListener {
        public void onDateSet(String displayDate, Date date);
    }

    private DatePickerFragmentListener datePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // Create a new instance of DatePickerDialog and return it
        return dpd;
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date, d = null, m = null, y = null;

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

        d = "" + day;
        m = month + 1 + "";
        y = "" + year;

        if (d.length() == 1)
            d = "0" + d;

        if (m.length() == 1)
            m = "0" + m;

        date = d + "/" + m + "/" + year;

        Log.d("Date Selected", date);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date tDate = new Date(calendar.getTimeInMillis());
        notifyDatePickerListener(date, tDate);
    }

    public DatePickerFragmentListener getDatePickerListener() {
        return this.datePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(String displayDate, Date date) {
        if (this.datePickerListener != null) {
            this.datePickerListener.onDateSet(displayDate, date);
        }
    }

}