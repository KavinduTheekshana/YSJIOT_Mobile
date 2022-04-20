package com.example.ysjiot.common;

import org.json.JSONObject;

public class Stables {
    public static String baseUrl="http://192.168.0.44" +
            ":8000/";

    private String loginController=baseUrl+"api/mobilelogin";
    public String getLoginController(String username,String password){
        return loginController+"?username="+username+"&password="+password;
    }
}
