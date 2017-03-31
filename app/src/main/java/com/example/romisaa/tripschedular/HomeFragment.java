package com.example.romisaa.tripschedular;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

//import android.support.v4.app.Fragment;


public class HomeFragment extends Fragment {

    LinearLayout l1;
    LinearLayout l2;
    LinearLayout l3;
    LinearLayout l4;
    Fragment fragment;

    FragmentManager mgr;
    FragmentTransaction trns;
    Singleton singleton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mgr = getFragmentManager();
        trns = mgr.beginTransaction();
        l1 = (LinearLayout) view.findViewById(R.id.l1);
        l2 = (LinearLayout) view.findViewById(R.id.l2);
        l3 = (LinearLayout) view.findViewById(R.id.l3);
        l4 = (LinearLayout) view.findViewById(R.id.l4);
        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.textanim);
        l1.setAnimation(anim2);
        l2.setAnimation(anim2);
        l3.setAnimation(anim2);
        l4.setAnimation(anim2);

        singleton = Singleton.getInstance(getActivity());
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new UpcomingFragment();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upcoming");
                ((MainActivity) getActivity()).currentFragment = "upcoming";
                trns.replace(R.id.content_main, fragment);
                trns.commit();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PastFragment();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Past");
                ((MainActivity) getActivity()).currentFragment = "past";
                trns.replace(R.id.content_main, fragment);
                trns.commit();
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HistoryFragment();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
                ((MainActivity) getActivity()).currentFragment = "history";
                trns.replace(R.id.content_main, fragment);
                trns.commit();
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder;
                final DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity());
                final Gson gson = new Gson();
                ;
                alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setMessage("Are you sure that you want to sync?");
                alertBuilder.setTitle("Syncing");
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Trip> trips = dataBaseHandler.getallTrips();
                        final String tripJson = gson.toJson(trips);

                        String url = "http://samybackend.herokuapp.com/SynchServlet";
                        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("success " + response);
                                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.getMessage());
                                Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("trips", tripJson);
                                params.put("email", sharedPreferences.getString("email", null));
                                return params;
                            }
                        };
                        singleton.addToRequestQueue(stringRequest);
                    }
                });

                alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });

        return view;
    }

}
