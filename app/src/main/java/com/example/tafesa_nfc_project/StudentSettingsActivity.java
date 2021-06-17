package com.example.tafesa_nfc_project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentSettingsActivity extends AppCompatActivity {
    //Expandable recyclerView
    RecyclerView recyclerView;
    List<Subjects> subjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_settings);
        Button btnSend;
        btnSend = (Button) this.findViewById(R.id.btnLogOut);
        btnSend.setOnClickListener(new LogOutButtonClick());
        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        //changes the menu button colors
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_attendance: //goes to attendance
                        Intent intent1 = new Intent(StudentSettingsActivity.this, StudentAttendanceActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                        break;
                    case R.id.nav_timetable: //goes to timetable
                        Intent intent2 = new Intent(StudentSettingsActivity.this, StudentMainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);
                        break;
                    case R.id.nav_settings: //goes to settings
                        Intent intent3 = new Intent(StudentSettingsActivity.this, StudentSettingsActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //startActivity(intent3);
                        break;
                }
                return true;
            }
        });


    }

    public class LogOutButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Amplify.Auth.signOut(
                    AuthSignOutOptions.builder().globalSignOut(true).build(),
                    () -> Log.i("AuthQuickstart", "Signed out globally"),
                    error -> Log.e("AuthQuickstart", error.toString())


            );
            onLogOutSuccess();
        }
    }
    private void onLogOutSuccess() {
        Log.i("AuthQuickstart", "Signed out globally");
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    public ArrayList inintList () {
        ArrayList<ArrayList> list = new ArrayList<ArrayList>();
       /*
        list.add(inintmLis("Here is CardView", "Cool effects hey", R.drawable.ic_arrow_back_ios_black_24dp));
        String[] splitStr = authors.split("\\*");
        for (int i=0;i<splitStr.length;i++) {
            books.add(splitStr[i]);
        }
        */
        Object ini = inintmLis("Here is CardView", "Cool effects hey", R.drawable.ic_arrow_back_ios_black_24dp);
        for (int i=0;i<=100;i++){
            list.add((ArrayList) ini);
        }
        return list;

    }

    private ArrayList inintmLis (String str1, String str2,int s){
        ArrayList list1 = new ArrayList();
        list1.add(str1);
        list1.add(str2);
        list1.add(s);
        return list1;
    }
}