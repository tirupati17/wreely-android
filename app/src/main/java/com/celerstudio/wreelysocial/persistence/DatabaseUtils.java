package com.celerstudio.wreelysocial.persistence;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

public class DatabaseUtils {

    public static WreelyDatabase getDatabase(Context context) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        WreelyDatabase database = WreelyDatabase.getInstance(context, preferenceUtils.getDatabaseName());
        return database;
    }

    public static void init(Context context, String dbName) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        preferenceUtils.setDatabaseName(dbName);
        WreelyDatabase database = WreelyDatabase.getInstance(context, dbName);
    }

    public static void init(Context context) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        String dbName = preferenceUtils.getDatabaseName();
        if (dbName.equalsIgnoreCase("")) {
            dbName = "wreely" + String.valueOf(new Date().getTime());
            preferenceUtils.setDatabaseName(dbName);
        }
        WreelyDatabase database = WreelyDatabase.getInstance(context, dbName);
    }

    public static void saveChatMessage(Context context, ChatMessage chatMessage) {
        WreelyDatabase database = getDatabase(context);
        database.chatMessageDao().insertChat(chatMessage);
    }

    public static void deleteAll(Context context) {
        WreelyDatabase database = getDatabase(context);
        database.chatMessageDao().deleteAll();
    }

    public static boolean isChatBotMessage(Map<String, String> rawData) {
        boolean flag = false;
        if (rawData != null && rawData.size() > 0 && rawData.containsKey("botMessage")) {
            flag = true;
        }
        return flag;
    }

}
