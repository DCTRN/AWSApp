package com.example.awsapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPass;
    private Button btnGoToReg;
    private Button btnGoToLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsername = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPass);

        btnGoToReg = findViewById(R.id.btnGoReg);
        btnGoToLog = findViewById(R.id.btnGoLog);

        AppHelper.init(getApplicationContext());

        btnGoToReg.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });



        btnGoToLog.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String pass = editPass.getText().toString();

                AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    //do smth
                }
        }
    }


    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

            Intent intent = new Intent(MainActivity.this, MainAppActivity.class);
            AppHelper.setUserSession(userSession);
            AppHelper.setUserDevice(newDevice);

            String idToken = userSession.getIdToken().getJWTToken();

            // Create a credentials provider, or use the existing provider.
            AppHelper.setCredentialsProvider(new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "eu-central-1:be18cb1c-e74a-46cd-9434-27c22ec409b4", // Identity pool ID
                    Regions.EU_CENTRAL_1 // Region
            ));

            // Set up as a credentials provider.
            Map<String, String> logins = new HashMap<String, String>();
            logins.put("eu-central-1:be18cb1c-e74a-46cd-9434-27c22ec409b4", userSession.getIdToken().getJWTToken());
            AppHelper.getCredentialsProvider().setLogins(logins);

            startActivity(intent);
            finish();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, editPass.getText().toString(), null);

            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);

            // Allow the sign-in to continue
            authenticationContinuation.continueTask();

        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onFailure(Exception exception) {

        }
    };


}
