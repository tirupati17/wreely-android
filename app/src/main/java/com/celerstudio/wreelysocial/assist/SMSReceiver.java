package com.celerstudio.wreelysocial.assist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.celerstudio.wreelysocial.util.Util;

public class SMSReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    private boolean once = true;
    private SMSListener listener;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d("MSG RCVD", senderNum + " , " + message);

                    if (senderNum.toLowerCase().contains("msgind") && message.toLowerCase().contains("wreely")) {
                        if (once) {
                            once = false;
                            String otp = message.replaceAll("[^0-9\\\\.]+", "");
                            if (!Util.textIsEmpty(otp)) {
                                listener.onReceived(otp);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    public void setListener(SMSListener listener) {
        this.listener = listener;
    }

    public interface SMSListener {
        public void onReceived(String otp);
    }

}