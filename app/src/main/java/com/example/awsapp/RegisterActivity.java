package com.example.awsapp;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsername;
    private EditText regPass;

    private EditText regEmail;
    private EditText regGivenName;
    private EditText regPhone;

    private Button btnLog;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.regUsername);
        regPass = findViewById(R.id.regPass);

        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.editPhone);
        regGivenName = findViewById(R.id.regGivenName);

        btnLog = findViewById(R.id.btnGoLog);
        btnReg = findViewById(R.id.btnReg);

        btnLog.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        btnReg.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                String username = regUsername.getText().toString();
                String pass = regPass.getText().toString();

                String email = regEmail.getText().toString();
                userAttributes.addAttribute("email", email);

                String phone = regPhone.getText().toString();
                userAttributes.addAttribute("phone_number", phone);

                String givenName = regGivenName.getText().toString();
                userAttributes.addAttribute("given_name", givenName);

                AppHelper.getPool().signUpInBackground(username, pass, userAttributes, null, signupCallback);


            }
        });

    }



    SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful

            // Check if this user (cognitoUser) needs to be confirmed
            if(!userConfirmed) {
                Intent intent = new Intent(RegisterActivity.this, SignUpConfirmActivity.class);
                intent.putExtra("username",regUsername.getText().toString());
                startActivityForResult(intent, 2);
            }
            else {
                // The user has already been confirmed
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("username", regUsername.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-up failed, check exception for the cause
            Toast.makeText(RegisterActivity.this, "Sign-up failed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("username", regUsername.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
        }

    }
}

