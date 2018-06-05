package com.celerstudio.wreelysocial.util;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;

public class FragmentUtils {

    public static AlertDialog getDialog(Activity activity, int layout, String title) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(layout, null);
        TextView t = alertLayout.findViewById(R.id.title);
        t.setText(title);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        return dialog;
    }

}
