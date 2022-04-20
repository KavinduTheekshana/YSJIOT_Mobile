package com.example.ysjiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ysjiot.common.Stables;

import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG);

        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);

                if(sharedPreferences!=null){
                    if(sharedPreferences.getString("userid","0").equals("0")){
                        Intent homeIntent = new Intent(SplashScreen.this,Login.class);
                        startActivity(homeIntent);
                        finish();
                    }else{
                        Intent homeIntent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }

                }else{

                    //Welcome
                    Intent homeIntent = new Intent(SplashScreen.this,Login.class);
                    startActivity(homeIntent);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);


    }
}