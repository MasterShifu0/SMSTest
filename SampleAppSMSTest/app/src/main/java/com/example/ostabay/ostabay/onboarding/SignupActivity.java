package com.example.ostabay.ostabay.onboarding;

import android.app.Activity;
import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ostabay.ostabay.R;

import java.security.SecureRandom;

/**
 * Created by Shekhar on 16/06/18.
 */

public class SignupActivity extends Activity implements View.OnClickListener{
    public static final String OTP_PIN = "otp_pin";
    public static final String SMS_RECEIVED = "sms_received";
    private Button mVerifyPhone,mVerifyOTP;
    private EditText mPhoneNmbr;
    private EditText userEneredPhoneNmbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_signup);
        mVerifyPhone = (Button) findViewById(R.id.verify_phone);
        mVerifyOTP = (Button) findViewById(R.id.verify_otp_btn);
        mPhoneNmbr = (EditText) findViewById(R.id.phone_number);
        userEneredPhoneNmbr = (EditText)findViewById(R.id.entered_otp) ;

        mVerifyPhone.setOnClickListener(this);
        mVerifyOTP.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.verify_phone:
                sendOTPToPhone();
                break;

            case R.id.verify_otp_btn:
                if(userEneredPhoneNmbr.getText() != null && !userEneredPhoneNmbr.getText().toString().isEmpty())
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int pin = sharedPreferences.getInt(SignupActivity.OTP_PIN, 0000);
                    Log.i("shekhar", "PIN from shared PREF is : " + pin);
                    if (pin != 0000 && pin == Integer.parseInt(userEneredPhoneNmbr.getText().toString())) {
                        Toast.makeText(this, "Your Phone is verified :)", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(this, "Wrong PIN", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this,"Enter OTP", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void sendOTPToPhone() {
        String phoneNumbr = "";
        if(mVerifyPhone.getText() != null) {
            phoneNumbr = mPhoneNmbr.getText().toString();
        }

        if(!phoneNumbr.isEmpty() && phoneNumbr.length() >= 10){
            int otpPin = generateRandomNumber();
            sendSMS(phoneNumbr, "" + otpPin);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(OTP_PIN, otpPin);
            editor.commit();
            //showInputOTPDialog();
            Log.i("shekhar", "OTP to be sent : " + otpPin);
            Toast.makeText(this,"OTP sent! : " + otpPin, Toast.LENGTH_LONG).show();
            //registerOTPReceiver();
        }else{
            Toast.makeText(this,"Please provide phone number !", Toast.LENGTH_LONG).show();
        }
    }

    private void registerOTPReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(new OTPReceiver(), intentFilter);
    }

    private void showInputOTPDialog() {
    }

    /*
    * This api is used to Send OTP for verification
    * @param {phoneNumer,otpPin}
    * */
    private void sendSMS(String phoneNumber, String otpPin) {
        /*SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, otpPin, null, null);*/
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , "+91"+ phoneNumber);
        smsIntent.putExtra("sms_body"  ,  otpPin);

        try {
            startActivity(smsIntent);
            //finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }


    public int generateRandomNumber() {
        int randomNumber;
        int range = 9;  // to generate a single number with this range, by default its 0..9
        int length = 4; // by default length is 4
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }

        randomNumber = Integer.parseInt(s);

        return randomNumber;
    }
    
    class OTPListener extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                if(SMS_RECEIVED.equalsIgnoreCase(action)){
                    
                }
            }
        }
    }
}
