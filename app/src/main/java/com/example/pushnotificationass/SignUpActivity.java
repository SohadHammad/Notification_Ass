package com.example.pushnotificationass;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    RequestQueue queue;
    EditText userName, phoneNumber , email, password;
    Button btn_reg;
    TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        queue = Volley.newRequestQueue(getApplicationContext());


        userName = (EditText) findViewById(R.id.edt_name);
        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_password);
        phoneNumber = (EditText) findViewById(R.id.edt_phone);


        btn_reg = (Button) findViewById(R.id.sign_up_btn);
        signIn =findViewById(R.id.tex_login);


        ////////////////////////////////////
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        ////////////////////////////////////////
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();

            }
        });

    }

     ///////////////////////////////////////
    private void createNewUser() {
        final String email = this.email.getText().toString();
        final String myPassword = password.getText().toString();
        final String user_Name = userName.getText().toString();
        final String phone_Number = phoneNumber.getText().toString();

        if (TextUtils.isEmpty(email)) {
            this.email.setError("Enter your email");
            this.email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(myPassword)) {
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }
        //
        String URL="https://mcc-users-api.herokuapp.com/add_new_user";

        //
        StringRequest postRequestquqe = new StringRequest(Request.Method.POST,URL,
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
                params.put("userName", user_Name);
                params.put(" phoneNumber", phone_Number);
                params.put("email", email);
                params.put("password", myPassword);
                return params;
            }
        };

        queue.add(postRequestquqe);

    }
}