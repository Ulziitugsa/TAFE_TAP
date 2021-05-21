package com.example.tafesa_nfc_project;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StudentAttendanceActivity extends AppCompatActivity {

    public String StartDate = "";
    public String EndDate = "";
    int TermNum = 2;
    int WeekNum = 0;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    List<String> WeekDates;
    String[] termSubjects;
    Student user = getUserDetailsfromSession();
    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_attendance);

        if(user != null)
        {


            downloadJSON("http://10.64.96.238:8080/test/weekDates.php", year, TermNum, UserGetId());

        }

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            //changes the menu button colors
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_attendance: //goes to attendance
                    Intent intent1 = new Intent(StudentAttendanceActivity.this, StudentAttendanceActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent1);
                    break;
                case R.id.nav_timetable: //goes to timetable
                    Intent intent2 = new Intent(StudentAttendanceActivity.this, StudentMainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                    break;
                case R.id.nav_settings: //goes to settings
                    Intent intent3 = new Intent(StudentAttendanceActivity.this, StudentSettingsActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_to_right, R.anim.slide_to_left);
                    break;
            }
            return true;
        });

        TextView title = (TextView) findViewById(R.id.activityTitle1);
        title.setText("This is the student Attendance Activity");


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> WeekDates()
    {

        Date LclStartDate = null;
        Date LclEndDate = null;
        if(!StartDate.equals("") && !EndDate.equals("")) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                LclStartDate = formatter.parse(StartDate);
                LclEndDate = formatter.parse(EndDate);
            }
            catch (Exception e)
            {
                Log.i("Error", e.getMessage());
            }
        }
        else
        {
            Log.e("Error", "Couldn't find the term dates");
        }

        Calendar start = Calendar.getInstance();
        start.setTime(LclStartDate);
        Calendar end = Calendar.getInstance();
        end.setTime(LclEndDate);

        List<String> list = new ArrayList<>();

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 7), date = start.getTime()) {
            WeekNum++;
            String temp = "Week " + WeekNum  + ", Date:" + date.toString();
            list.add(temp);

        }

        return list;
    }

    private void downloadJSON(final String urlWebService, int year, int TermCode, String id) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    try {
                        loadIntoListView(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    String data =
                                URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(year), "UTF-8")
                                +"&&"+URLEncoder.encode("termcode", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(TermCode), "UTF-8")
                                +"&&"+URLEncoder.encode("id", "UTF-8")+"="+ URLEncoder.encode(id, "UTF-8");


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
                    Log.e("Haloo", e.getMessage());
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        termSubjects = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            StartDate = obj.getString("StartDate");
            EndDate = obj.getString("EndDate");
            termSubjects[i] = obj.getString("SubjectCode");

        }
        WeekDates = WeekDates();
        Log.i("test123Start", StartDate);
        Log.i("test123End", EndDate);
        Log.i("test123Week", WeekDates.toString());
        Log.i("test123Sub", Arrays.toString(termSubjects));


    }

    public Student getUserDetailsfromSession()
    {
        Student student = new Student();
        //Getting user information from the userpool
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

        Log.i("ABCDE", "A:" + student.getId());
        Log.i("ABCD", "A:" + student.getEmail());
        Log.i("ABCD", "A:" + student.getFamilyName());
        Log.i("ABCD", "A:" + student.getGivenName());
        return student;

    }

    public String UserGetId()
    {
        return user.id;
    }
}