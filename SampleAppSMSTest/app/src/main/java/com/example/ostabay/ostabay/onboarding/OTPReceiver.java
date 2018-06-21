package com.example.ostabay.ostabay.onboarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Shekhar on 21/06/18.
 */

public class OTPReceiver  extends BroadcastReceiver{
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            if(action.equalsIgnoreCase(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
                Log.i("shekhar", " onReceive() ");
                try {
                    String messageBody = "";
                    for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                        messageBody = smsMessage.getMessageBody();
                    }
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    int pin = sharedPreferences.getInt(SignupActivity.OTP_PIN, 0000);
                    Log.i("shekhar", "PIN from shared PREF is : " + pin);
                    if(pin != 0000 && pin == Integer.parseInt(messageBody)){
                        Toast.makeText(context,"Your Phone is verified :)", Toast.LENGTH_LONG).show();
                    }

                    Intent messageReceived = new Intent(SignupActivity.SMS_RECEIVED);
                    messageReceived.putExtra("sms", messageBody);
                    context.sendBroadcast(messageReceived); // when receiving it somewhere in your app, subString the additional text and leave only the code, then place it in the editText and do your verification
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
