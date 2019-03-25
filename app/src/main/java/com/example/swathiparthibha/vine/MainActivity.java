package com.example.swathiparthibha.vine;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.io.IOException;
import java.net.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);

        OkHttpClient client = new OkHttpClient();

        String url = "http://dweet.io/get/latest/dweet/for/865235030073924";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //int latitude = 2;
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            try {
//                                int latitude = convertJSON(myResponse);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            textView.setText(myResponse);
                        }
                    });
                }
            }
        });


        //textView.setText("Test");

    }

//    public int convertJSON(String JSON_DATA) throws JSONException {
//        final JSONObject obj = new JSONObject(JSON_DATA);
//        JSONObject with = (JSONObject) obj.getJSONObject("with");
//        JSONArray content = (JSONArray) with.getJSONArray("content");
//        final int n = content.length();
//        int latitude = 0;
//        for (int i = 0; i < n; ++i) {
//            final JSONObject person = content.getJSONObject(i);
//            latitude = person.getInt("lat");
//            int longitude = person.getInt("long");
//            int speed = person.getInt("speed");
//        }
//
//        return latitude;
//    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}