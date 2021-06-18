package com.example.tafesa_nfc_project;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Student;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  {

    Tag myTag;
    Context context;
    NfcAdapter nfcAdapter;
    ProgressBar progressCircle;
    ImageView approvedCircle;
    ImageView failedCircle;

    String UserGroup = "";

    Student user = getUserDetailsFromSession();
    String RoomNum = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressCircle = findViewById(R.id.progressCircle);
        failedCircle = findViewById(R.id.failedCircle);
        approvedCircle = findViewById(R.id.approvedCircle);
        TextView nfcScan = findViewById(R.id.scanText);
        //NFC part
        context = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            RoomNum = readFromIntent(getIntent());
            Toast.makeText(context, "NFC content: " + RoomNum, Toast.LENGTH_SHORT).show();
        AuthUser currentUser = Amplify.Auth.getCurrentUser();

            if (currentUser == null) {
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
                                Intent intent;
                                try {
                                    UserGroup = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("cognito:groups");
                                    Log.i("StudentDetails", "IdentityId: " + UserGroup);
                                    Log.i("StudentDetails", "IdentityId: " + UserGroup.contains("Students"));
                                    if (UserGroup.contains("Students")) {
                                        if(!RoomNum.equals("")) {
                                            nfcScan.setVisibility(View.VISIBLE);
                                            Log.i("elloo", "jk");
                                            CheckIfUserBelongsToTheRoom(UserGetId(), RoomNum);
                                        }
                                        else {
                                            intent = new Intent(this, StudentMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else if (UserGroup.contains("Lecturers")) {
                                        intent = new Intent(this, TeacherMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("MainActivityError", e.getMessage());
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


    private void CheckIfUserBelongsToTheRoom(String userGetId, String roomNum) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("00:HH:mm", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedTime = tf.format(c);
        Calendar ca = Calendar.getInstance();
        ca.setTime(c);
        int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK)-1;
        downloadJSON("http://192.168.137.1:8080/test/ScanIn.php", roomNum, userGetId, formattedDate, formattedTime, dayOfWeek);
    }

    private void downloadJSON(final String urlWebService,  String roomNum, String id, String Date, String Time, int dayOfWeek) {

        @SuppressLint("StaticFieldLeak")
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if(s == null || s.contains("No subject found") || s.contains("No term found") || s.contains("Failed to connect to MySQL:") || s.contains("error"))
                {
                    failed(s);
                }
                else
                {

                    approval();
                }


            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream ops = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                    String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")
                            +"&&"+URLEncoder.encode("roomNum","UTF-8")+"="+URLEncoder.encode(String.valueOf(roomNum),"UTF-8")
                            +"&&"+URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(String.valueOf(Date),"UTF-8")
                            +"&&"+URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(String.valueOf(Time),"UTF-8")
                            +"&&"+URLEncoder.encode("dayOfWeek","UTF-8")+"="+URLEncoder.encode(String.valueOf(dayOfWeek),"UTF-8");

                    writer.write(data);
                    writer.flush();
                    writer.close();
                    ops.close();

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {

                    Log.i("FAILED123", e.getMessage());
                    return null;

                }


            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }



    public String UserGetId()
    {
        return user.id;
    }

    public Student getUserDetailsFromSession()
    {
        Student student = new Student();
        //Getting user information from the UserPool
        String[] id = {null};
        String[] UserGivenName = {null};
        String[] email = {null};
        String[] family_name = {null};
        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:



                            try {
                                id[0] = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("custom:Student_ID");
                                UserGivenName[0] = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("given_name");
                                email[0] = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("email");
                                family_name[0] = CognitoJWTParser.getPayload(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken()).getString("family_name");




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            student.setId(id[0]);
                            student.setEmail(email[0]);
                            student.setGivenName(UserGivenName[0]);
                            student.setFamilyName(family_name[0]);



                            break;

                        case FAILURE:
                            Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                    }
                },
                error -> Log.e("AuthQuickStart", error.toString())
        );

        Log.i("StudentInfoTest", "A:" + student.getId());
        Log.i("StudentInfoTest", "A:" + student.getEmail());
        Log.i("StudentInfoTest", "A:" + student.getFamilyName());
        Log.i("StudentInfoTest", "A:" + student.getGivenName());
        return student;

    }
	  //NFC Part
    private String readFromIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            return buildTagViews(msgs);
        }
        return "";
    }
    private String buildTagViews(NdefMessage[] msgs){
        if (msgs == null || msgs.length == 0){
            return "";
        }
        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0]&128) == 0)? "UTF-8":"UTF-16";
        int languageCodeLength = payload[0] & 0063;
        try{
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.toString());
        }
        return text;
    }
    @Override
    protected  void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }



    @SuppressLint("SetTextI18n")
    public void approval() {
        approvedCircle.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        TextView nfcScan = findViewById(R.id.scanText);
        nfcScan.setText("Successfully Scanned");

    }

    @SuppressLint("SetTextI18n")
    public void failed(String s) {
        failedCircle.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        TextView nfcScan = findViewById(R.id.scanText);
        nfcScan.setText("Failed to scan : " + s);

    }


}