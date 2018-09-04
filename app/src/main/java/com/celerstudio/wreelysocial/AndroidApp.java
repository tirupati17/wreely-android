package com.celerstudio.wreelysocial;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.network.APIClient;
import com.celerstudio.wreelysocial.network.APIService;
import com.celerstudio.wreelysocial.persistence.PreferenceUtils;
import com.celerstudio.wreelysocial.util.MixpanelEvents;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import io.fabric.sdk.android.Fabric;


public class AndroidApp extends Application implements ConnectivityReceiverListener {

    private PreferenceUtils preferenceUtils;
    private APIService apiService;
    private User user;
    ConnectivityReceiver connectivityReceiver;
    private ConnectivityReceiverListener connectivityReceiverListener;
    public static final String MIXPANEL_TOKEN = "3f6651d5c7ffd264fb50afe9fc595403";
    private MixpanelAPI mixpanel;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference myRef;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        this.firebaseDatabase = FirebaseDatabase.getInstance();
        Stetho.initializeWithDefaults(this);
        preferenceUtils = new PreferenceUtils(this);
        apiService = APIClient.getAdapterApiService();
        setUser();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver(this);
        registerReceiver(connectivityReceiver, intentFilter);
        Stetho.initializeWithDefaults(this);
        mixpanel =
                MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
        Fabric.with(this, new Crashlytics());

    }

    public PreferenceUtils getPreferences() {
        if (preferenceUtils == null)
            throw new NullPointerException("Preference Utils cannot be null");
        return preferenceUtils;
    }

    public void setConnectivityListener(ConnectivityReceiverListener listener) {
        connectivityReceiverListener = listener;
    }

    public APIService getAPIService() {
        return apiService;
    }

    public User getUser() {
        if (user == null)
            user = getPreferences().getUser();
        return user;
    }

    public void setUser() {
        user = getPreferences().getUser();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (connectivityReceiverListener != null)
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
    }

    public MixpanelEvents getMixpanel(User user) {
        return new MixpanelEvents(mixpanel, user);
    }

    public MixpanelEvents getMixpanel() {
        return new MixpanelEvents(mixpanel);
    }

//    public DatabaseReference getFirebaseDBRef(String table) {
////        return myRef = firebaseDatabase.getReference().child(table);
//        return null;
//    }

}
