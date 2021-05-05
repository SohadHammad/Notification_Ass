package com.example.pushnotificationass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    RequestQueue queue;
    EditText email_sign, password_sign;
    Button btn_login;
    String token;
    TextView registration;
    String email;
    String passwordSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        queue = Volley.newRequestQueue(getApplicationContext());

        email_sign = (EditText) findViewById(R.id.edt_email);
        password_sign = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.login_btn);

        registration =findViewById(R.id.tex_sign);

        //
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReqTokenquqe();
                validateUser();
                updateUsers();

            }
        });
    }


    //
    private void getReqTokenquqe() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.e("msg","Failed to get token"+task.getException());
                    return;
                }

                token = task.getResult();
                Log.d("msg","token : "+token);
            }
        });
    }

    //
    private void validateUser() {
        email = email_sign.getText().toString();
        passwordSign = password_sign.getText().toString();

        if (TextUtils.isEmpty(email)) {
            email_sign.setError("Enter your email");
            email_sign.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordSign)) {
            password_sign.setError("Enter password");
            password_sign.requestFocus();
            return;
        }
        String URL="https://mcc-users-api.herokuapp.com/login";


        StringRequest postRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            Log.d("TAG", "Success: "+objres.toString());
                        } catch (JSONException e) {
                            Log.d("TAG", "Error");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", passwordSign);
                return params;
            }
        };
        email_sign.setText("");
        password_sign.setText("");
        queue.add(postRequest);
    }


    //
    private void updateUsers() {
        String URL="https://mcc-users-api.herokuapp.com/add_reg_token";

        StringRequest postRequest = new StringRequest(Request.Method.PUT,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            Log.d("TAG", "SuccessOnResponse: "+objres.toString());
                        } catch (JSONException e) {
                            Log.d("TAG", "Server Error ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }) {

            //
            @Override
            protected Map<String, String> getParams(){
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", passwordSign);
                params.put("token",token);
                return params;
            }
        };
        queue.add(postRequest);
    }
}