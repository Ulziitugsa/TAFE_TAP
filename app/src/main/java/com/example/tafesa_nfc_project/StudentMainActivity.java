package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;

import java.util.jar.Attributes;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        TextView NameTxt = (TextView)findViewById(R.id.textView4);

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:
                            JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                            try {
                                String UserGivenName = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("given_name");
                                Log.i("AuthQuickStart", "User Name: " + cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                                NameTxt.setText(UserGivenName);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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
        onLogOutSuccess();
    }


    private void onLogOutSuccess() {
        Log.i("AuthQuickstart", "Signed out globally");
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }
}