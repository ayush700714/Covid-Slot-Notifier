package com.example.covidnotifier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidnotifier.utils.LoginState;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.common.hash.Hashing;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpActivity extends AppCompatActivity {

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    Button otpBtn;
    EditText otp;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String otp_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Log.d("txnId", getIntent().getStringExtra("txnId"));
        Toast.makeText(OtpActivity.this,getIntent().getStringExtra("txnId"),Toast.LENGTH_SHORT).show();

        otpBtn = findViewById(R.id.otp_btn);
        otp = findViewById(R.id.otp_text);
        startSmartUserConsent();

        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp_text = otp.getText().toString().trim();
                otpBtn.setEnabled(false);

                RequestQueue queue = Volley.newRequestQueue(OtpActivity.this);
                String url = "https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP";
                final String hashed = Hashing.sha256()
                        .hashString(otp_text, StandardCharsets.UTF_8)
                        .toString();
                Log.d("otpsha256", hashed);
                Toast.makeText(OtpActivity.this,hashed,Toast.LENGTH_SHORT).show();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(OtpActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                                Log.d("check", response.toString());

                                DatabaseReference databaseReference =firebaseDatabase.getReference().child("Users").child(getIntent().getStringExtra("phone")).getRef();
                                databaseReference.child("phone").setValue(getIntent().getStringExtra("phone"));

                                Intent intent= new Intent(OtpActivity.this,MainActivity.class);
                                LoginState.savePrefsData(getApplicationContext(),getIntent().getStringExtra("phone"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                                Log.d("error", error.toString()+" "+error.getMessage());
                                Toast.makeText(OtpActivity.this,error.toString(),Toast.LENGTH_SHORT).show();


                                String body=null,errName=null;
//                                //get status code here
                                String statusCode = String.valueOf(error.networkResponse.statusCode);
//                                //get response body and parse with appropriate encoding
                                if(statusCode.equals("400"))
                                {
                                    if(error.networkResponse.data!=null) {
                                        try {
                                            body = new String(error.networkResponse.data,"UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            error.printStackTrace();
                                        }

                                        try {
                                            JSONObject jsonObject = new JSONObject(body);

                                            errName =   jsonObject.getString("error");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if(errName.equals("Beneficiary Not Registered"))
                                        {

                                        }
                                        Log.d("body", body+" "+errName);
                                        Toast.makeText(OtpActivity.this,body+"  "+errName,Toast.LENGTH_SHORT).show();
                                        otpBtn.setEnabled(true);

                                        Intent intent= new Intent(OtpActivity.this,MainActivity.class);
                                        LoginState.savePrefsData(getApplicationContext(),getIntent().getStringExtra("phone"));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }


                            }
                        }){

                    @Override
                    public byte[] getBody() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("txnId",getIntent().getStringExtra("txnId"));
                            jsonObject.put("otp",hashed);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String p = jsonObject.toString();
                        return p.getBytes();

                    }
                };

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);

            }
        });

    }
    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){

            if ((resultCode == RESULT_OK) && (data != null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);


            }


        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){

            otp.setText(matcher.group(0));
            Log.d("otp", matcher.group(0));
            Toast.makeText(OtpActivity.this,matcher.group(0),Toast.LENGTH_SHORT).show();

        }


    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}
//400	Error:
//{ "errorCode": "USRAUT0014", "error": "Invalid OTP" }

//400 error
//{"errorCode":"USRAUT0024", "error":"Beneficiary Not Registered"}


//    Code	Details
//401	Error:

//200
//        {
//        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiI0NjkzYTRiZi00MDdkLTRiMWEtODNiOS1hZDJlYjE0NDllODMiLCJ1c2VyX3R5cGUiOiJCRU5FRklDSUFSWSIsInVzZXJfaWQiOiI0NjkzYTRiZi00MDdkLTRiMWEtODNiOS1hZDJlYjE0NDllODMiLCJtb2JpbGVfbnVtYmVyIjo2MzA2ODgyNjg1LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNzQ2NjM0Mzk5OTgwLCJ0eG5JZCI6ImMzNDJlZTg1LWQzN2UtNDUxOS1iM2VjLTE1NDIyN2Q5MjBhMCIsImlhdCI6MTYyNTM4NjUzNCwiZXhwIjoxNjI1Mzg3NDM0fQ.VcXWo7mVlc0AENle4Gz8SRW8m0yyA8UdKri1G8-7nOs"
//        }