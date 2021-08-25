package com.example.covidnotifier;

import org.json.JSONObject;

public interface VolleyJsonRespondsListener {

    public void onSuccessJson(JSONObject result, String type);
    public void onFailureJson(int responseCode, String responseMessage);
}