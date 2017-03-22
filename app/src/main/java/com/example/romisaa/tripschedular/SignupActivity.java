package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class SignupActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView link;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        final Singleton singleton=Singleton.getInstance(getApplicationContext());
        final RequestQueue requestQueue=singleton.getRequestQueue();

        if (sharedPreferences.contains("email"))
        {
            System.out.println(sharedPreferences.getString("email",null));
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        link = (TextView) findViewById(R.id.linkToLogin);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                if(!isValidEmail(emailEditText.getText().toString())){
                    Toast.makeText(SignupActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordEditText.getText().toString().equals("")){
                    Toast.makeText(SignupActivity.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                }

                //TODO Send Mail & Password To Servlet
                Log.i("MyTag","");
                String url="http://10.142.1.187:5030/tripSchedularBackEnd/SignupServlet?email="+emailEditText.getText().toString()+"&password="+passwordEditText.getText().toString();

                 StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","success");
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        if(response.equals("done")){
                            editor.putString("email",emailEditText.getText().toString());
                            editor.putString("password",passwordEditText.getText().toString());
                            editor.commit();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            //TODO
                            //hntl3 error msg ll user (user already exists)

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("MyTag",error.getMessage());
                        Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                    }
                });
                singleton.addToRequestQueue(stringRequest);

            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });





    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
