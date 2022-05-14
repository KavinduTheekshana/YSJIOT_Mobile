package com.example.ysjiot.common;

import org.json.JSONObject;

public class Stables {
    public static String baseUrl="http://iot.creatxsoftware.com/";

    private String loginController=baseUrl+"api/mobilelogin";
    public String getLoginController(String username,String password){
        return loginController+"?username="+username+"&password="+password;
    }

    public String getdataset(){
        return baseUrl+"api/getdataset";
    }
}
