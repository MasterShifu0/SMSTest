package com.example.ostabay.ostabay.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.ETC1;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ostabay.ostabay.R;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button loginButton,signUpButton;
    private EditText userName, passWord;
    private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.login_button);
        userName = (EditText)findViewById(R.id.user_name);
        passWord = (EditText)findViewById(R.id.login_password);
        forgotPassword = (TextView)findViewById(R.id.forgot_password);
        signUpButton = (Button)findViewById(R.id.sign_up);

        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.i("shekhar", "Onclick : " + view.getId());
        switch (view.getId()){
            case R.id.login_button:
                getUsernamePasswd();
                break;

            case R.id.sign_up:
                startSignupActivity();
                break;
            case R.id.forgot_password:
                //TODO
                //launch forgot session
                break;

        }

    }

    private void startSignupActivity() {
        Intent signUpIntent = new Intent(this, SignupActivity.class);
        startActivity(signUpIntent);
    }

    /*
    * This api gets the Uername and password from user
    * */
    public void getUsernamePasswd() {
        String user_name = "",pass_word="";
        if(userName.getText() != null)
            user_name = userName.getText().toString();
        if(passWord.getText() != null)
            pass_word = passWord.getText().toString();

        if(user_name.isEmpty() || pass_word.isEmpty())
        {
            Toast.makeText(this,"Please enter all the values", Toast.LENGTH_LONG).show();
        }else{
            //TODO
            //do authentication and start session
            Toast.makeText(this,"Login successful", Toast.LENGTH_LONG).show();
        }
    }
}
