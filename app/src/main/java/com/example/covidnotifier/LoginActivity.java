package com.example.covidnotifier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;




public class LoginActivity extends AppCompatActivity {


    EditText phoneET;
    Button loginBtn;
    ProgressBar mLoginProgress;
    String phone;
    String txnId;
    TextView mLoginFeedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneET = findViewById(R.id.phone_number_text);
        loginBtn = findViewById(R.id.generate_btn);
        mLoginProgress = findViewById(R.id.login_progress_bar);
        mLoginFeedbackText = findViewById(R.id.login_form_feedback);

//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://cdn-api.co-vin.in/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Api users = retrofit.create(Api.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneET.getText().toString().trim();
                loginBtn.setEnabled(false);
                mLoginProgress.setVisibility(View.VISIBLE);
                mLoginFeedbackText.setVisibility(View.INVISIBLE);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("check", response.toString());

                                try {
                                    txnId =   response.getString("txnId");
                                    Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
                                    intent.putExtra("txnId",txnId);
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("check", error.toString()+" "+error.getMessage());
                                loginBtn.setEnabled(true);
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText(error.toString());
                            }
                        }){

                    @Override
                    public byte[] getBody() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("mobile",phone);
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
}