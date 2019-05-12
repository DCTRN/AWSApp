package com.example.awsapp;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;

public class SignUpConfirmActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText ConfirmCode;

    private Button confirmBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);


        editUsername = findViewById(R.id.editConfirmUser);
        ConfirmCode = findViewById(R.id.editCode);

        confirmBTN = findViewById(R.id.confirmBTN);

        confirmBTN.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = editUsername.getText().toString();
                String confCode = ConfirmCode.getText().toString();
                AppHelper.getPool().getUser(user).confirmSignUpInBackground(confCode, false, confirmationCallback);
            }
        });
    }

    GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Intent intent = new Intent(SignUpConfirmActivity.this, RegisterActivity.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onFailure(Exception exception) {
        }
    };

}

