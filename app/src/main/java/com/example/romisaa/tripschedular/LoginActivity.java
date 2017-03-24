package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener{

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView link;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Singleton singleton;
    RequestQueue requestQueue;
    Gson gson;
    int  RC_SIGN_IN=0;
    LinearLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        gson=new Gson();
        singleton=Singleton.getInstance(getApplicationContext());
        requestQueue=singleton.getRequestQueue();
        if (sharedPreferences.contains("email"))
        {
            System.out.println(sharedPreferences.getString("email",null));
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        mLayout = (LinearLayout) findViewById(R.id.activity_login);
        link = (TextView) findViewById(R.id.linkToSignup);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                if(!isValidEmail(emailEditText.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordEditText.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                }

                //TODO Send Mail & Password To Servlet
                String url="http://192.168.0.100:8082/tripSchedularBackEnd/LoginServlet?email="+emailEditText.getText().toString()+"&password="+passwordEditText.getText().toString();
                StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ana fel response "+response);
                        ArrayList<Trip> trips;
                        java.lang.reflect.Type type = new TypeToken<ArrayList<Trip>>() {}.getType();
                        if (response.equals("not exist")){
                            //TODO
                            // error message for user that invalid email or password
                        }
                        else
                        {
                            trips = gson.fromJson(response, type);
                            for (Trip trip : trips  ) {
                                new DataBaseHandler(getApplicationContext()).addTrip(trip);
                            }
                            editor.putString("email",emailEditText.getText().toString());
                            editor.putString("password",passwordEditText.getText().toString());
                            editor.commit();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
//                            System.out.println("Trips"+trips.get(0).getNotes().get(0).getContent());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        Toast.makeText(LoginActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                singleton.addToRequestQueue(stringRequest);

            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("mafesh connectiom 3lya l ne3ma ");
        Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
    }
    //google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

// here we will write the code to handle the respond from google api

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("out", "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
          //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            System.out.println("user name: "+acct.getDisplayName());
            System.out.println("email:"+ acct.getEmail());
            String url="http://192.168.0.100:8082/tripSchedularBackEnd/LoginServlet?email="+acct.getEmail()+"&flag=app";
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    ArrayList<Trip> trips;
                    java.lang.reflect.Type type = new TypeToken<ArrayList<Trip>>() {}.getType();
                    if (response.equals("not exist")){
                        //TODO
                        // error message for user that invalid email or password
                    }
                    else
                    {
                        trips = gson.fromJson(response, type);
                        for (Trip trip : trips  ) {
                            new DataBaseHandler(getApplicationContext()).addTrip(trip);
                        }
                        editor.putString("email",acct.getEmail());
                        editor.commit();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        System.out.println("Trips"+trips.get(0).getNotes().get(0).getContent());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            });
            singleton.addToRequestQueue(stringRequest);

           // updateUI(true);
        } else {

            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
}
