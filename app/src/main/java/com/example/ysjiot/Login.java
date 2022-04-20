package com.example.ysjiot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ysjiot.common.Stables;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private Button  login;
    private TextInputEditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        login = findViewById(R.id.btnLogin);
        username = findViewById(R.id.textFieldEmail);
        password = findViewById(R.id.textFieldPassword);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLogin();
            }
        });








    }

    private void createLogin() {
        if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
            //loading
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            StringRequest stringRequest=new StringRequest(Request.Method.GET, new Stables().getLoginController(username.getText().toString(),password.getText().toString()), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //hide loading
                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        if(jsonObject.getString("code").equals("1")){
                            Toast.makeText(Login.this, "Welcome To YSJIOT", Toast.LENGTH_SHORT).show();

                            JSONObject userObj=jsonObject.getJSONObject("user");
                            SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("userid",userObj.getString("id"));
                            editor.putString("email",userObj.getString("email"));
                            editor.commit();

                            getApplicationContext().startService(new Intent(getApplicationContext(), ScheduleNotification.class));

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(Login.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    //hide loading
                    Toast.makeText(Login.this, "Network Connection", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
//            TextViewCompat.setTextAppearance(username,R.style.);
        }
    }


}