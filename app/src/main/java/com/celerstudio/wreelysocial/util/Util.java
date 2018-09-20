package com.celerstudio.wreelysocial.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;

import com.celerstudio.wreelysocial.models.MeetingRoom;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.RestError;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class Util {

    public static RestError handleError(ResponseBody response) {
        RestError restError = new RestError();
        restError.setError(false);
        restError.setMessage("Something Went Wrong!");
        if (response instanceof ResponseBody) {
            ResponseBody responseBody = response;
            try {
                restError = new Gson().fromJson(new String(responseBody.bytes()), RestError.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return restError;
    }

    public static String decodeString(String base64) {
        byte[] decodeValue = Base64.decode(base64, Base64.DEFAULT);
        String des = new String(decodeValue);
        return des;
    }

    public static boolean textIsEmpty(String value) {

        if (value == null)
            return true;

        if (value.equalsIgnoreCase("null"))
            return true;

        boolean empty = false;

        String message = value.trim();

        if (TextUtils.isEmpty(message)) {
            empty = true;
        }

        boolean isWhitespace = message.matches("^\\s*$");

        if (isWhitespace) {
            empty = true;
        }

        return empty;
    }

    public static String getCertificateSHA1Fingerprint(Context context) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String uniqueDeviceID(Context context) {


        UUID deviceUuid;
        String tmDevice = "", tmSerial = "", androidId = "";
        String deviceIdStr = "";

        TelephonyManager telephonyManager = null;

        PackageManager packageManager = context.getPackageManager();

        int hasPhoneStatePerm = packageManager.checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName());
        if (hasPhoneStatePerm == PackageManager.PERMISSION_GRANTED) {
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }

        try {
            if (telephonyManager != null) {
                tmDevice = "" + telephonyManager.getDeviceId();
                tmSerial = "" + telephonyManager.getSimSerialNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (androidId == null) {
            androidId = "";
        }


        String build = "";
        if (android.os.Build.SERIAL != null) {
            build = android.os.Build.SERIAL;
        }

        try {
            if (!tmDevice.equalsIgnoreCase("") && !tmSerial.equalsIgnoreCase("")) {
                deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                deviceIdStr = deviceUuid.toString();
            } else {
                deviceUuid = new UUID(androidId.hashCode(), build.hashCode());
                deviceIdStr = deviceUuid.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceIdStr;
    }


    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static boolean isMatchTimeExpired(MeetingRoomSlot slot) {
        String raw = slot.getStartTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date matchDate = dateFormat.parse(raw);
            Date currentDate = new Date();
            return currentDate.after(matchDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getEventTimeDuration(String sd, String ed) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            Date startDate = dateFormat.parse(sd);
            Date endDate = dateFormat.parse(ed);
            String startTime = new SimpleDateFormat("h:mm a, d MMM yyyy").format(startDate);
            String endTime = new SimpleDateFormat("h:mm a, d MMM yyyy").format(endDate);
            if (isEventOfSameDay(startDate, endDate)) {
                startTime = new SimpleDateFormat("h:mm a").format(startDate);
                endTime = new SimpleDateFormat("h:mm a").format(endDate);
                String day = new SimpleDateFormat("d MMM yyyy").format(endDate);
                time = startTime + " - " + endTime + ", " + day;
            } else {
                time = startTime + " - " + endTime;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static boolean isEventOfSameDay(Date startDate, Date endDate) {
        boolean sameDay = false;
        Calendar sC = Calendar.getInstance();
        sC.setTime(startDate);
        Calendar eC = Calendar.getInstance();
        eC.setTime(endDate);
        boolean sameYear = sC.get(Calendar.YEAR) == eC.get(Calendar.YEAR);
        boolean sameMonth = sC.get(Calendar.MONTH) == eC.get(Calendar.MONTH);
        boolean sDay = sC.get(Calendar.DAY_OF_MONTH) == eC.get(Calendar.DAY_OF_MONTH);
        if (sameYear && sameMonth && sDay)
            sameDay = true;
        return sameDay;
    }

    public static boolean isMeetingTimeExpired(MeetingRoomSlot slot) {
        String raw = slot.getStartTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date matchDate = dateFormat.parse(raw);
            Date currentDate = new Date();
            return currentDate.after(matchDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMeetingCancelable(MeetingRoomSlot slot) {
        String raw = slot.getStartTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date matchDate = dateFormat.parse(raw);
            Date currentDate = new Date();
            long duration = matchDate.getTime() - currentDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            return !(diffInMinutes < 15);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getFriendlyTime(String time) {
        String formattedDate = time;
        String[] splits = time.split(":");
        int hour = Integer.parseInt(splits[0]);
        String amPm = "AM";
        if (hour > 11) {
            hour = hour - 12;
            amPm = "PM";
        }
        if (hour == 0) {
            hour = 12;
        }

        String hrs = hour > 9 ? String.valueOf(hour) : "0" + hour;

        formattedDate = hrs + ":" + splits[1] + " " + amPm;
        return formattedDate;
    }

}
