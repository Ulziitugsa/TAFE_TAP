package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amplifyframework.auth.cognito.AWSCognitoUserPoolTokens;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;


public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        TextView myAwesomeTextView = (TextView)findViewById(R.id.textView2);
        myAwesomeTextView.setText("a");
        AWSCognitoAuthSession a;
        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:
                            JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                            try {
                                String ab = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("cognito:groups");
                                Log.i("AuthQuickStart", "IdentityId: " + ab);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ;

                            break;

                        case FAILURE:
                            Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                    }
                },
                error -> Log.e("AuthQuickStart", error.toString())
        );





    }

    public void onPressLogOut(View view) {
        Amplify.Auth.signOut(
                AuthSignOutOptions.builder().globalSignOut(true).build(),
                () -> Log.i("AuthQuickstart", "Signed out globally"),
                error -> Log.e("AuthQuickstart", error.toString())

        );



    }
}