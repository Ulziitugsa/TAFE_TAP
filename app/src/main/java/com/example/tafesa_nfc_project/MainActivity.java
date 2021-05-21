package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

public class    MainActivity extends AppCompatActivity {
    ProgressBar progressCircle;
    ImageView approvedCircle;
    ImageView failedCircle;
    String UserGroup = "";
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressCircle = findViewById(R.id.progressCircle);
        failedCircle = findViewById(R.id.failedCircle);
        approvedCircle = findViewById(R.id.approvedCircle);
        AuthUser currentUser = Amplify.Auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            //go to login screen
        } else {
            Amplify.Auth.fetchAuthSession(
                    result -> {
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                        switch (cognitoAuthSession.getIdentityId().getType()) {
                            case SUCCESS:
                                JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                                Intent intent = new Intent(this, BlankActivity.class);
                                try {
                                    UserGroup = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("cognito:groups");
                                    Log.i("AuthQuickStartwtfistji", "IdentityId: " + UserGroup);
                                    Log.i("THeFUCKIS", "IdentityId: " + UserGroup);
                                    Log.i("THeFUCKIS", "IdentityId: " + UserGroup.contains("Students"));
                                    if (UserGroup.contains("Students")) {
                                        // This leads to fail / Approval change doesn't work
                                        //approval(view);
                                        intent = new Intent(this, StudentMainActivity.class);

                                    } else if (UserGroup.contains("Lecturers")) {
                                        //approval(view);
                                        intent = new Intent(this, TeacherMainActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    failed(view);
                                    Log.e("DUMBASS", e.getMessage());
                                    Intent intent1 = new Intent(this, LoginActivity.class);
                                    startActivity(intent1);
                                }

                                break;

                            case FAILURE:
                                Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                                failed(view);
                        }
                    },
                    error -> Log.e("AuthQuickStart", error.toString())

            );
        }


    }

    public void approval(View view) {
        approvedCircle.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
    }

    public void failed(View view) {
        failedCircle.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
    }
}