package com.example.tafesa_nfc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Student;
import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Attributes;

public class StudentMainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        //setups
        downloadJSON("http://10.64.96.214:8080/test/getAttendance.php");
        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
            //changes the menu button colors
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_attendance: //goes to attendance
                        Intent intent1 = new Intent(StudentMainActivity.this, StudentAttendanceActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                        break;
                    case R.id.nav_timetable: //goes to timetable
                        Intent intent2 = new Intent(StudentMainActivity.this, StudentMainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent2);
                        break;
                    case R.id.nav_settings: //goes to settings
                        Intent intent3 = new Intent(StudentMainActivity.this, StudentSettingsActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                        break;
                }
                return false;
            }
        });


        listView = (ListView) findViewById(R.id.listView);
        TextView NameTxt = (TextView)findViewById(R.id.textView3);

        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView1);
        Student user = getUserDetailsfromSession();
        NameTxt.setText(user.given_name);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                DateClickListener(year, month, dayOfMonth, user.id);
            }
        });

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
    //for getting data from mysql php json
    private void downloadJSON(final String urlWebService) {

           class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("GivenName") + " " + obj.getString("LastName");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listView.setAdapter(arrayAdapter);
    }

    public void DateClickListener(int year, int month, int dayOfMonth, String StudentID)
    {
        Intent intent = new Intent(this, DateDetails.class);
        intent.putExtra("ID", StudentID);
        intent.putExtra("dayOfMonth", dayOfMonth);
        intent.putExtra("month", month);
        intent.putExtra("year", year);

        startActivity(intent);
    }

    public Student getUserDetailsfromSession()
    {
        final Student student = new Student();
        //Getting user information from the userpool
        final String[] id = {null};
        final String[] UserGivenName = {null};
        final String[] email = {null};
        final String[] family_name = {null};
        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:

                            JWT token = new JWT(cognitoAuthSession.getUserPoolTokens().getValue().getIdToken());

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
                            Log.i("ABCD", student.getId());
                            Log.i("ABCD", email[0]);
                            Log.i("ABCD", UserGivenName[0]);
                            Log.i("ABCD", family_name[0]);



                            break;

                        case FAILURE:
                            Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                    }
                },
                error -> Log.e("AuthQuickStart", error.toString())
        );
        return student;

    }
}
