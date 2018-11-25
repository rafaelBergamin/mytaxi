package com.mytaxi.android_demo.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by RAFAELBERGAMINDASILV on 25/11/2018.
 */

public class LoginDAO {

    private static String url = "https://randomuser.me/api/?seed=a1f30d446f820665";

    public static String getLogin(String field) throws IOException {

        String credential = null;
        OkHttpClient client = new OkHttpClient();

        // reading the json and obtaining the credentials
        try {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        JsonParser mJsonParser = new JsonParser();
        JsonObject jsonObject = mJsonParser.parse(response.body().string()).getAsJsonObject();
        JsonArray results = jsonObject.getAsJsonArray("results");
        JsonElement jsonElement = results.get(0);
        JsonObject jsonUser = jsonElement.getAsJsonObject();

        // with the results, the field is returned to our calling class
        JsonObject login = jsonUser.getAsJsonObject("login");
        credential = login.get(field).getAsString();

        }catch (Exception ex)
        {
            System.out.println("Error getting credentials: " + ex.getMessage() + ex.getStackTrace());
            return null;
        }
        return credential;
    }
}