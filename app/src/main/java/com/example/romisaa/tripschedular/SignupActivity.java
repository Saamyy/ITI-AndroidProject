package com.example.romisaa.tripschedular;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            finishAffinity();
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
                String url="https://samybackend.herokuapp.com/SignupServlet?email="+emailEditText.getText().toString()+"&password="+passwordEditText.getText().toString();

                 StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","success");
//                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        if(response.equals("done")){
                            editor.putString("email",emailEditText.getText().toString());
                            editor.putString("password",passwordEditText.getText().toString());
                            editor.commit();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                           progressDialog.dismiss();
                            startActivity(intent);
                            finishAffinity();
                        }
                        else{
                            //TODO
                            //hntl3 error msg ll user (user already exists)
                            Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("MyTag",error.getMessage());
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                singleton.addToRequestQueue(stringRequest);

               progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("LoaDing....");
               progressDialog.show();




            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
