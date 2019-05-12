package com.example.awsapp;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProvider;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidentityprovider.model.AttributeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppHelper {
    // App settings

    private static AppHelper appHelper;
    private static CognitoUserPool userPool;
    private static CognitoUserSession userSession;
    private static CognitoDevice userDevice;
    private static CognitoCachingCredentialsProvider credentialsProvider;

    private static final String userPoolId = "eu-central-1_PE2EkYrPQ";
    private static final String clientId = "7u9u89g0e7rgu6737spefoqki8";
    private static final String clientSecret = "1rl3bpnve7g575ptnegq853vcf39b68tp9413sh0f0inli45eb9i";
    private static final Regions cognitoRegion = Regions.EU_CENTRAL_1;
    private static final String identityPoolId = "eu-central-1:be18cb1c-e74a-46cd-9434-27c22ec409b4";

    // User details from the service

    public static void init(Context context) {

        if (appHelper != null && userPool != null) {
            return;
        }

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

        if (userPool == null) {
            userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
        }

    }

    public static CognitoUserPool getPool() {
        return userPool;
    }

    public static CognitoUserSession getUserSession() {
        return userSession;
    }

    public static void setUserSession(CognitoUserSession userSession) {
        AppHelper.userSession = userSession;
    }

    public static CognitoDevice getUserDevice() {
        return userDevice;
    }

    public static void setUserDevice(CognitoDevice userDevice) {
        AppHelper.userDevice = userDevice;
    }

    public static CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public static void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider) {
        AppHelper.credentialsProvider = credentialsProvider;
    }
}

