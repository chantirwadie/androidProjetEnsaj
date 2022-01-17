package com.ensaj.examanchantirwadie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity4 extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button btn;
    RequestQueue requestQueue;
    String insertUrl ="https://fierce-ridge-76224.herokuapp.com/user/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn = findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = email.getText().toString();
                String b = password.getText().toString();
                try {
                    envoiDonner(a,b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void envoiDonner(String email,String password) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);
        Log.d("rrrr", "envoiDonner: "+json);
        JsonObjectRequest req = new JsonObjectRequest(insertUrl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "onResponse: "+response);
                        try {
                            String condition =response.getString("response");
                            Log.d("TAG", "onResponse: "+condition);
                            if(condition.equals("ok")){
                                Toast.makeText(getApplicationContext(),"login avec succese",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity4.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Email ou Mot de pass est Incorrect",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Occupation est bien Ajouter", Toast.LENGTH_SHORT).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        requestQueue.add(req);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}