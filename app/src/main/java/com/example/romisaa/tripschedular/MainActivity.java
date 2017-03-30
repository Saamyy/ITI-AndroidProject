package com.example.romisaa.tripschedular;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBaseHandler dataBaseHandler;
    AlertDialog.Builder alertBuilder;
    Singleton singleton;
    RequestQueue requestQueue;
    Gson gson;
    SharedPreferences sharedPreferences;
    String currentFragment = "home";
    ProgressDialog progressDialog;
    private Boolean exit = false;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MyTag",currentFragment+" onstart");
        exit=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        singleton = Singleton.getInstance(this);
        requestQueue = singleton.getRequestQueue();
        gson = new Gson();
        dataBaseHandler = new DataBaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTrip.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager mgr = getFragmentManager();
        FragmentTransaction trns = mgr.beginTransaction();

        trns.replace(R.id.content_main, new HomeFragment(), "home_fragment");
        trns.commit();
        getSupportActionBar().setTitle("Home");
        currentFragment = "home";
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            HomeFragment currentFragment = (HomeFragment) getFragmentManager().findFragmentByTag("home_fragment");
            if (currentFragment != null && currentFragment.isVisible()) {
               // super.onBackPressed();
                if(exit)
                {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this, "Press the back button once again to close.", Toast.LENGTH_SHORT).show();
                    exit=true;
                }

            }

            else {
                FragmentManager mgr = getFragmentManager();
                FragmentTransaction trns = mgr.beginTransaction();
                trns.replace(R.id.content_main, new HomeFragment(), "home_fragment");
                trns.commit();
                getSupportActionBar().setTitle("Home");
                MainActivity.this.currentFragment = "home";
            }

        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.start_now) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure that you want to sync?");
        alertBuilder.setTitle("Syncing");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Trip> trips = dataBaseHandler.getallTrips();
                final String tripJson = gson.toJson(trips);

                String url = "https://samybackend.herokuapp.com/SynchServlet";
                StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("success " + response);
                        progressDialog.dismiss();
                        //  Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("trips", tripJson);
                        params.put("email", sharedPreferences.getString("email", null));
                        return params;
                    }
                };
                singleton.addToRequestQueue(stringRequest);
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Fetching Data");
                progressDialog.show();
            }
        });

        alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Fragment fragment = null;
        if (id == R.id.upcoming) {
            fragment = new UpcomingFragment();
            getSupportActionBar().setTitle("Upcoming Trips");
            currentFragment = "upcoming";
            Log.i("MyTag",currentFragment);
        } else if (id == R.id.past) {
            fragment = new PastFragment();
            getSupportActionBar().setTitle("Past Trips");
            currentFragment = "past";
        } else if (id == R.id.history) {
            fragment = new HistoryFragment();
            getSupportActionBar().setTitle("History");
            currentFragment = "history";
        } else if (id == R.id.help) {

        } else if (id == R.id.about) {
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        if (fragment != null) {
            FragmentManager mgr = getFragmentManager();
            FragmentTransaction trns = mgr.beginTransaction();
            trns.replace(R.id.content_main, fragment, "current_fragment");
            trns.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    /*@Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Fragment currentFragment = getFragmentManager().findFragmentByTag("current_fragment");
        if (currentFragment != null && currentFragment.isVisible() && currentFragment instanceof UpcomingFragment) {
            outState.putString("current_fragment","upcoming");
        }
        else {
            FragmentManager mgr = getFragmentManager();
            FragmentTransaction trns = mgr.beginTransaction();
            trns.replace(R.id.content_main, new HomeFragment(), "home_fragment");
            trns.commit();
            getSupportActionBar().setTitle("Home");
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().putExtra("currentFragment", currentFragment);
        Log.i("MyTag",currentFragment+" onstop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("currentFragment")) {
            Log.i("MyTag","in if");
            currentFragment = getIntent().getExtras().getString("currentFragment");
            FragmentManager mgr = getFragmentManager();
            FragmentTransaction trns = mgr.beginTransaction();
            switch (currentFragment) {
                case "upcoming":
                    trns.replace(R.id.content_main, new UpcomingFragment());
                    trns.commit();
                    getSupportActionBar().setTitle("Upcoming Trips");
                    break;
                case "past":
                    trns.replace(R.id.content_main, new PastFragment());
                    trns.commit();
                    getSupportActionBar().setTitle("Past Trips");
                    break;
                case "history":
                    trns.replace(R.id.content_main, new HistoryFragment());
                    trns.commit();
                    getSupportActionBar().setTitle("History");
                    break;
                case "home":
                    trns.replace(R.id.content_main, new HomeFragment(), "home_fragment");
                    trns.commit();
                    getSupportActionBar().setTitle("Home");
                    break;

            }
        }
        Log.i("MyTag",currentFragment+" onresume");
    }
}
