package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Lecturer;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;


public class LoginActivity extends AppCompatActivity {

    String UserGroup = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onPressLogin(View view) {
        EditText txtEmail = view.findViewById(R.id.txtEmail);
        EditText txtPassword = view.findViewById(R.id.txtPassword);

        Amplify.Auth.signIn(
                txtEmail.getText().toString(),
                txtPassword.getText().toString(),
                this::onLoginSuccess,
                this::onLoginError
        );

        Object a = Amplify.Auth.getCurrentUser();
        //Needs Validation  for empty textboxes
    }

    private void onLoginError(AuthException e) {
        this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                .show());
    }

    private void onLoginSuccess(AuthSignInResult authSignInResult) {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:
                            JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());
                            try {
                                UserGroup = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("cognito:groups");
                                Log.i("AuthQuickStartwtfistji", "IdentityId: " + UserGroup);
                                Intent intent = new Intent();
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


}