package com.example.tafesa_nfc_project;

import android.app.Application;
import android.util.Log;

import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.DataStorePlugin;
import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;

public class AmplifyApp extends Application {
    public void onCreate() {

        super.onCreate();
        try {
            AmplifyModelProvider modelProvider = AmplifyModelProvider.getInstance();
            Amplify.addPlugin(new AWSDataStorePlugin(modelProvider));
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("amplify","configured" );
        }
        catch (AmplifyException e)
        {
            e.printStackTrace();
        }


    }

}
