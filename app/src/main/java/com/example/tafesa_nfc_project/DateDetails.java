package com.example.tafesa_nfc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateDetails extends AppCompatActivity {
    ListView listView;
    List<String> listGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_details);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String id = null;
        listView = (ListView) findViewById(R.id.listView);


        Date j =null;
        if(b!=null)
        {
            id = (String) b.get("ID");
            j = (Date) b.get("date");
            Log.i("AAAA", j.toString());

        }
        int date = j.getDate();
        int month = j.getMonth() +1;
        int year = j.getYear()+1900;

        Calendar c = Calendar.getInstance();
        c.setTime(j);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
        downloadJSON("http://10.64.96.212:8080/test/DateDetails.php", date, month, year , dayOfWeek, id);


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

    }
}