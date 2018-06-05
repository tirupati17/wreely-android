package com.celerstudio.wreelysocial.persistence;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.celerstudio.wreelysocial.Constants;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.util.Util;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by sayagodshala on 15/05/17.
 */

public class PreferenceUtils extends PreferenceHelper {

    private Context context;

    public PreferenceUtils(Context context) {
        super(context);
        this.context = context;
    }

    public void setUser(User user) {
        setUserLoggedIn();
        addPreference(Constants.PreferenceKeys.USER_DATA, new Gson().toJson(user));
    }

    public User getUser() {
        User user = null;
        String raw = getString(Constants.PreferenceKeys.USER_DATA, "");
        Log.d("User Data", raw);
        if (!raw.equalsIgnoreCase(""))
            user = new Gson().fromJson(raw, User.class);
        return user;
    }

    public double[] getLatLng() {
        double lat = Double.parseDouble(getString(Constants.PreferenceKeys.LNG, "0.0"));
        double lng = Double.parseDouble(getString(Constants.PreferenceKeys.LNG, "0.0"));
        return new double[]{lat, lng};
    }

    public void setUserLoggedIn() {
        addPreference(Constants.PreferenceKeys.USER_LOGGED_IN, true);
    }

    public void setDatabaseName(String dbName) {
        addPreference(Constants.PreferenceKeys.DATABASE_NAME, dbName);
    }

    public String getDatabaseName() {
        String raw = getString(Constants.PreferenceKeys.DATABASE_NAME, "");
        if (Util.textIsEmpty(raw))
            raw = "skyappdb";
        return raw;
    }

    public boolean isUserLoggedIn() {
        return getBoolean(Constants.PreferenceKeys.USER_LOGGED_IN, false);
    }

    public void removeUserSession() {
//        String raw = getGCMToken();
        DatabaseUtils.deleteAll(context);
        super.clearSession();
//        setGcmToken(raw);
    }
}
