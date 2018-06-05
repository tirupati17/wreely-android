package com.celerstudio.wreelysocial.util;

import com.celerstudio.wreelysocial.models.User;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MixpanelEvents {

    private MixpanelAPI mixpanelAPI;
    private User user;

    public MixpanelEvents(MixpanelAPI mixpanelAPI, User user) {
        this.mixpanelAPI = mixpanelAPI;
        this.user = user;
    }

    public MixpanelEvents(MixpanelAPI mixpanelAPI) {
        this.mixpanelAPI = mixpanelAPI;
    }

    private static final String LOGIN = "Login";
    private static final String DASHBOARD_LOADED = "Dashboard Loaded";


    public void customEvent(String eventName) {
        mixpanelAPI.track(eventName);
    }

    public void login() {
        JSONObject props = new JSONObject();
        try {
            props.put("Gender", "Female");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanelAPI.track(LOGIN, null);
    }

    public void dashboardLoaded() {
        mixpanelAPI.track(DASHBOARD_LOADED, null);
    }


    public void createUserInMixpanel() {
        mixpanelAPI.identify(String.valueOf(user.getId())); //this is the distinct_id value that
        mixpanelAPI.getPeople().identify(String.valueOf(user.getId()));
        mixpanelAPI.getPeople().set("$name", user.getName());
        mixpanelAPI.getPeople().set("$first_name", user.getName());
        if (!Util.textIsEmpty(user.getMobile()))
            mixpanelAPI.getPeople().set("$phone", user.getMobile());
        mixpanelAPI.getPeople().set("$email", user.getEmail());
        mixpanelAPI.getPeople().set("occupation", user.getOccupation());
        login();
    }

}

