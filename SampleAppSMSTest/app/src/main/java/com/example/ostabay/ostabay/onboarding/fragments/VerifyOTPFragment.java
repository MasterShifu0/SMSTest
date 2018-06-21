package com.example.ostabay.ostabay.onboarding.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ostabay.ostabay.R;
import com.example.ostabay.ostabay.onboarding.SignupActivity;

import java.security.SecureRandom;

/**
 * Created by wishall on 22/06/18.
 */

public class VerifyOTPFragment extends Fragment implements View.OnClickListener {

    private Button sendOTPButton, verifyOTPButton;
    private EditText mPhoneNmbr;
    private EditText userEneredPhoneNmbr;

    public static final String OTP_PIN = "otp_pin";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        sendOTPButton = (Button) view.findViewById(R.id.send_otp);
        verifyOTPButton = (Button) view.findViewById(R.id.verify_otp);
        mPhoneNmbr = (EditText) view.findViewById(R.id.phone_number);
        userEneredPhoneNmbr = (EditText)view.findViewById(R.id.entered_otp) ;
        sendOTPButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendOTPToPhone();
            }
        });
        verifyOTPButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
        Toast.makeText(this.getActivity(),"no. clicked", Toast.LENGTH_LONG).show();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_otp:
                Toast.makeText(this.getActivity(),"no. clicked", Toast.LENGTH_LONG).show();
                sendOTPToPhone();
                break;

            case R.id.verify_otp:

                break;
        }
    }

    private void sendOTPToPhone() {
        String phoneNumbr = "";
        if(mPhoneNmbr.getText() != null) {
            phoneNumbr = mPhoneNmbr.getText().toString();
        }

        if(!phoneNumbr.isEmpty() && phoneNumbr.length() >= 10){
            int otpPin = generateRandomNumber();
            sendSMS(phoneNumbr, "" + otpPin);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(OTP_PIN, otpPin);
            editor.commit();
            //showInputOTPDialog();
            Log.i("shekhar", "OTP to be sent : " + otpPin);
            Toast.makeText(this.getActivity(),"OTP sent! : " + otpPin, Toast.LENGTH_LONG).show();
            //registerOTPReceiver();
        }else{
            Toast.makeText(this.getActivity(),"Please provide phone number !", Toast.LENGTH_LONG).show();
        }
    }

    private void verifyOTP() {
        if(userEneredPhoneNmbr.getText() != null && !userEneredPhoneNmbr.getText().toString().isEmpty())
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            int pin = sharedPreferences.getInt(SignupActivity.OTP_PIN, 0000);
            Log.i("shekhar", "PIN from shared PREF is : " + pin);
            if (pin != 0000 && pin == Integer.parseInt(userEneredPhoneNmbr.getText().toString())) {
                Toast.makeText(this.getActivity(), "Your Phone is verified :)", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.getActivity(), "Wrong PIN", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this.getActivity(),"Enter OTP", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this.getActivity(),
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}
