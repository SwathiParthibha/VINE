package com.example.swathiparthibha.vine;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity{
    public double longitude = 0;
    public double latitude = 0;
    public double speed = 0;
    public int panic = 0;
    public boolean speak = false;
    public boolean state = true;
    int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView1);
        final TextView textView2 = findViewById(R.id.textView);
        final ImageView lowImage = findViewById(R.id.lowImage);
        final ImageView mediumImage = findViewById(R.id.mediumImage);
        final ImageView fastImage = findViewById(R.id.fastImage);


        getDweet();

        Thread t=new Thread(){


            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                getDweet();
                                if(panic == 1) {
                                    textView.setText("Panic Button: EMERGENCY");
                                    textView.setTextColor(Color.RED);
                                    if(state) {
                                        addNotification();
                                        state = false;
                                    }
                                }
                                else if(panic == 0){
                                    textView.setText("Panic Button: Normal");
                                    textView.setTextColor(Color.rgb(30,96,52));
                                    state = true;

                                }
                                else{
                                    state = false;
                                }


                                if(speed < 1){
                                    lowImage.setVisibility(View.VISIBLE);
                                    mediumImage.setVisibility(View.INVISIBLE);
                                    fastImage.setVisibility(View.INVISIBLE);
                                    textView2.setText("Speed: Slow");
                                }
                                else if(speed >= 2 && speed <= 5){
                                    mediumImage.setVisibility(View.VISIBLE);
                                    lowImage.setVisibility(View.INVISIBLE);
                                    fastImage.setVisibility(View.INVISIBLE);
                                    textView2.setText("Speed: Medium");
                                }
                                else{
                                    fastImage.setVisibility(View.VISIBLE);
                                    lowImage.setVisibility(View.INVISIBLE);
                                    mediumImage.setVisibility(View.INVISIBLE);
                                    textView2.setText("Speed: FAST");
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start();

        //textView.setText("Test");


    }

    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("VINE App")
                .setContentText("EMERGENCY! PANIC BUTTON HAS BEEN TOGGLED")
                .setAutoCancel(true);

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    public void getDweet(){
        //final TextView textView = findViewById(R.id.textView);

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
                    if(myResponse.contains("succeeded")) {

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int latitude_index = myResponse.indexOf("lat");
                                String latitude_s = new String(myResponse.substring(latitude_index + 5, latitude_index + 13));

                                int longitude_index = myResponse.indexOf("long");
                                String longitude_s = new String(myResponse.substring(longitude_index + 6, longitude_index + 15));

                                int speed_index = myResponse.indexOf("speed");
                                int panic_index = myResponse.indexOf("panic");
                                String speed_s = new String(myResponse.substring(speed_index + 7, panic_index-2));


                                int last_index = myResponse.indexOf("}");
                                String panic_s = new String(myResponse.substring(panic_index + 7, last_index));


                                latitude = Double.parseDouble(latitude_s);
                                longitude = Double.parseDouble(longitude_s);
                                speed = Double.parseDouble(speed_s);
                                panic = Integer.parseInt(panic_s);

                                //textView.setText("Speed " + speed);

                            }
                        });
                    }
                }
            }
        });
    }

    public void checkSpeak(){
        //final TextView textView = findViewById(R.id.textView);

        OkHttpClient client = new OkHttpClient();

        String url = "http://dweet.io/get/latest/dweet/for/865235030073923";

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
                    if(myResponse.contains("succeeded")) {

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int speak_index = myResponse.indexOf("speak");
                                String speak_s = new String(myResponse.substring(speak_index + 7, speak_index + 8));

                                if(speak_s.equals("1")){
                                    speak = true;
                                }
                                else{
                                    speak = false;
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        getDweet();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        startActivity(intent);
    }

    public void sendMessage2(View view) {
        OkHttpClient client = new OkHttpClient();

        String url;
        checkSpeak();

        if(speak == true){
            url = "http://dweet.io/dweet/for/865235030073923?speak=0";
        }
        else{
            url = "http://dweet.io/dweet/for/865235030073923?speak=1";
        }

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
                }
            }
        });
    }
}