package com.example.tafesa_nfc_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentAttendanceActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            //changes the menu button colors
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
        // recyclerview stuff
        recyclerView = findViewById(R.id.home_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AttendanceAdapter(getApplicationContext(), inintList()));
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
        Object ini = inintmLis("Week Name", "Date & No. of Hours", R.drawable.status_none);
        for (int i=0;i<=5;i++){
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