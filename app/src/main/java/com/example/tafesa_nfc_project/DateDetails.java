package com.example.tafesa_nfc_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateDetails extends AppCompatActivity {
    //Expandable ListView
    //ExpandableListView expandableListView;

    ListView listView; //remove later
	ArrayList<String> listGroup = new ArrayList<>();
	HashMap<String,ArrayList<String>> listChild = new HashMap<>();
	ListAdapter adapter;
	//Expandable recyclerView
	RecyclerView recyclerView;
	List<Subjects> subjectsList;
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String id = null;
        listView = (ListView) findViewById(R.id.listView); //change later

        //expandableListView = findViewById(R.id.exp_list_view);
		Date j =null;
        if(b!=null)
        {
            id = (String) b.get("ID");
            j = (Date) b.get("date");
            Log.i("AAAA", j.toString());
        }
        int date = j.getDate();
        int month = j.getMonth() +1;
        int year;
        year = j.getYear()+1900;

        Calendar c = Calendar.getInstance();
        c.setTime(j);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
        //IP Address 10.64.97.28:8080 / 192.168.1.181 / 10.62.13.220
        downloadJSON("http://192.168.1.181/test/DateDetails.php", date, month, year , dayOfWeek, id);

        // recyclerview stuff
        recyclerView = findViewById(R.id.recyclerView);
        setRecyclerView();
        initData();
		/*//Using for loop for expandableListView
		for(int g=0;g<=10;g++){
			//Add values in group list 
			listGroup.add("Group"+g);
			//Initialize array list 
			ArrayList<String> arrayList = new ArrayList<>();
			//Use for loop
			for (int gg=0; gg<=1; gg++){
				//Add values in array list 
				arrayList.add("Item"+c);
			}
			//put values in child list 
			listChild.put(listGroup.get(g),arrayList);
		}
		//Initialize adapter
        adapter = new ListAdapter(listGroup, listChild);
		//Set Adapter
        expandableListView.setAdapter(adapter);
        */

    }

    private List<Subjects> initData() {
        subjectsList = new ArrayList<>();
        subjectsList.add(new Subjects("5JAW","12-08-2021","9:00 AM - 11:00 AM", "Room B.B03"));
        subjectsList.add(new Subjects("5SDA","13-08-2021","9:00 AM - 11:00 AM", "Room B.B03"));
        subjectsList.add(new Subjects("5IOSMD","14-08-2021","9:00 AM - 11:00 AM", "Room B.B03"));
        return subjectsList;
    }

    private void setRecyclerView() {
        SubjectsAdapter subjectsAdapter = new SubjectsAdapter(subjectsList);
        recyclerView.setAdapter(subjectsAdapter);
        recyclerView.setHasFixedSize(true);
	}

    //for getting data from mysql php json
    private void downloadJSON(final String urlWebService, int date, int month, int year, int DayOfWeek, String id) {

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
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream ops = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                    String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")
                            +"&&"+URLEncoder.encode("day","UTF-8")+"="+URLEncoder.encode(String.valueOf(date),"UTF-8")
                            +"&&"+URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode(String.valueOf(month),"UTF-8")
                            +"&&"+URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(String.valueOf(year),"UTF-8")
                            +"&&"+URLEncoder.encode("DayOfWeek","UTF-8")+"="+URLEncoder.encode(String.valueOf(DayOfWeek),"UTF-8");
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    ops.close();

                    InputStream ips = con.getInputStream();
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
            stocks[i] = obj.getString("SubjectCode");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listView.setAdapter(arrayAdapter);
        //Jsonobject to recycler view
    }
}