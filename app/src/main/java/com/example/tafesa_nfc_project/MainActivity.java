package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

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

public class    MainActivity extends AppCompatActivity  {

    String UserGroup = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthUser currentUser = Amplify.Auth.getCurrentUser();


        if(currentUser == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            //go to login screen
        }
        else
        {
            Amplify.Auth.fetchAuthSession(
                    result -> {
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                        switch(cognitoAuthSession.getIdentityId().getType()) {
                            case SUCCESS:
                                JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                                Intent intent = new Intent(this, BlankActivity.class);
                                try {
                                    UserGroup = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("cognito:groups");
                                    Log.i("AuthQuickStartwtfistji", "IdentityId: " + UserGroup);
                                    Log.i("THeFUCKIS", "IdentityId: " + UserGroup);
                                    Log.i("THeFUCKIS", "IdentityId: " + UserGroup.contains("Students"));
                                    if(UserGroup.contains("Students")) {
                                        intent = new Intent(this, StudentMainActivity.class);
                                    }
                                    else if(UserGroup.contains("Lecturers"))
                                    {
                                        intent = new Intent(this, TeacherMainActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("DUMBASS", e.getMessage());
                                    Intent intent1 = new Intent(this, LoginActivity.class);
                                    startActivity(intent1);
                                }



                                break;

                            case FAILURE:
                                Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                        }
                    },
                    error -> Log.e("AuthQuickStart", error.toString())
            );
        }


    }
}
