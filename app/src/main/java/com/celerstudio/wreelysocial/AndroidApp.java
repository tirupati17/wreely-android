package com.celerstudio.wreelysocial;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.network.APIClient;
import com.celerstudio.wreelysocial.network.APIService;
import com.celerstudio.wreelysocial.persistence.PreferenceUtils;
import com.celerstudio.wreelysocial.util.MixpanelEvents;
import com.celerstudio.wreelysocial.views.activity.VendorsDashboardActivity;
import com.celerstudio.wreelysocial.views.activity.SplashActivity;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

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

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
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

    public DatabaseReference getFirebaseDBRef(String table) {
//        return myRef = firebaseDatabase.getReference().child(table);
        return null;
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }


    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String customKey;
            String openURL = null;
            Object activityToLaunch = VendorsDashboardActivity.class;

            if (data != null) {
                customKey = data.optString("customkey", null);
                openURL = data.optString("openURL", null);

                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);

                if (openURL != null)
                    Log.i("OneSignalExample", "openURL to webview with URL value: " + openURL);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                if (result.action.actionID.equals("id1")) {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                    activityToLaunch = SplashActivity.class;
                } else
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
            }
            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openURL", openURL);
            Log.i("OneSignalExample", "openURL = " + openURL);
            // startActivity(intent);
            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your vendors Activity
            //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
        }
    }

}
