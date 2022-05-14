package com.example.ysjiot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ysjiot.common.Stables;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btn_logout;
    String rain,gas;
    private TextView txt_humidity,txt_temprature,txt_gas,txt_rain;
    private ImageView img_rain,img_air;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btn_logout = findViewById(R.id.btn_logout);
        txt_temprature = findViewById(R.id.txt_temprature);
        txt_humidity = findViewById(R.id.txt_humidity);
        txt_gas = findViewById(R.id.txt_gas);
        txt_rain = findViewById(R.id.txt_rain);
        img_rain = findViewById(R.id.img_rain);
        img_air = findViewById(R.id.img_air);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getProfileDetails();
            }
        }, 0, 2000);//put here time 1000 milliseconds=1 second
    }




    public void getProfileDetails(){

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);


        //Check user id availabl in db
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, new Stables().getdataset(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("code").equals("1")){
                        JSONObject dataObj=jsonObject.getJSONObject("dataset");
                        // Code
                        txt_humidity.setText(dataObj.getString("huminity"));
                        txt_temprature.setText(dataObj.getString("temprature"));
                        gas= dataObj.getString("gas").toString();
                        rain= dataObj.getString("rain").toString();
                        if (gas.equals("1")){
                            txt_gas.setText("Gas Found");
                            img_air.setImageResource(R.drawable.ic_gas_svgrepo_com);
                            int reqCode = 1;
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            showNotification(MainActivity.this, "Gas Found", "Your Gas Sensor has been enabled, Please check ASAP", intent, reqCode);
                        }else {
                            txt_gas.setText("Gas Not Found");
                            img_air.setImageResource(R.drawable.ic_wind_svgrepo_com);
                        }

                        if(rain.equals("1")){
                            txt_rain.setText("Clear");
                            img_rain.setImageResource(R.drawable.ic_cloudy_sunny_svgrepo_com);
                        }else {
                            txt_rain.setText("Raining");
                            img_rain.setImageResource(R.drawable.ic_rain_svgrepo_com);
                        }
                    }else{
                        Intent homeIntent = new Intent(MainActivity.this,Login.class);
                        startActivity(homeIntent);
                        finish();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //hide loading
//                Intent homeIntent = new Intent(MainActivity.this,Login.class);
//                startActivity(homeIntent);
//                finish();
            }
        });

        requestQueue.add(stringRequest);
    }
    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {


        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_iot_logo_new)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }

}