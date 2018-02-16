package com.star.networktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mSendRequest;
    private TextView mResponseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendRequest = findViewById(R.id.send_request);
        mResponseText = findViewById(R.id.response_text);

        mSendRequest.setOnClickListener(v -> sendRequestWithOkHttp());
    }

    private void sendRequestWithHttpURLConnection() {

        new Thread(() -> {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL("https://www.baidu.com");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(8000);

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                showResponse(response.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }).start();
    }

    private void sendRequestWithOkHttp() {
        new Thread(() -> {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://192.168.1.168/get_data.xml")
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                String responseData = response.body().string();
                showResponse(responseData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showResponse(final String response) {

        runOnUiThread(() -> mResponseText.setText(response));
    }
}
