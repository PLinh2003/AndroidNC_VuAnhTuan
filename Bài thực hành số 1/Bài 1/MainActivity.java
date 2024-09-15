package com.example.androidnc_b1;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.TextView01);
    }

    public void readWebpage(View view) {
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute("https://www.baomoi.com");
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                for (String Url : strings) {
                    URL url = new URL(Url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                }
            } catch (Exception e) {
                return e.toString();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }

}