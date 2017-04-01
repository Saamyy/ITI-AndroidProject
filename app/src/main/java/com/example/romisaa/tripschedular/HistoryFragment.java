package com.example.romisaa.tripschedular;


import android.app.Fragment;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements OnMapReadyCallback {


    MapView mapView;
    Singleton singleton;
    RequestQueue requestQueue;
    RootParser rootParser;
    ArrayList<Trip> trips;
    Double latitudeSource, longitudeSource, latitudeDest, longitudeDest;
    PolylineOptions lineOptions;
    private GoogleMap mMap;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("History Trips");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        singleton = Singleton.getInstance(getActivity().getApplicationContext());
        requestQueue = singleton.getRequestQueue();
        rootParser = new RootParser();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        trips = new DataBaseHandler(getActivity().getApplicationContext()).getallHistoryTrips();
        for (Trip trip : trips) {
//            getLatLngFromName(trip.getSource());
//            getLatLngFromName(trip.getDestination());
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lnglatFromName(trip.getSource()) + "&destination=" + lnglatFromName(trip.getDestination()) + "&key=AIzaSyBP0TBRYhcEWiIJhMM4GyoWWjWovszvGWk";
            draw(url);
        }
    }

//    public LatLng getLatLngFromName(String place) {
//        try {
//            List<Address> adresses = new Geocoder(getActivity().getApplicationContext()).getFromLocationName(place, 5);
//            LatLng latLng = new LatLng(adresses.get(0).getLatitude(), adresses.get(0).getLongitude());
//            return latLng;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void draw(String url) {
        final ArrayList<LatLng> points= new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success");
                System.out.println(response);
                System.out.println(rootParser.parse(response));
                List<List<HashMap<String, String>>> roots = rootParser.parse(response);
                if (roots!=null) {
                    // Traversing through all the routes
                    for (int i = 0; i < roots.size(); i++) {
                        // Fetching i-th route
                        List<HashMap<String, String>> path = roots.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            if (j == 0) {
                                latitudeSource = lat;
                                longitudeSource = lng;
                            }
                            if (j == path.size() - 1) {
                                latitudeDest = lat;
                                longitudeDest = lng;
                            }
                            System.out.println("Lat and lang    >" + lat + "    " + lng);
                            points.add(position);
                        }
                        // Adding all the points in the route to LineOptions
//                    lineOptions.addAll(points);
//                    lineOptions.width(8);
//                    lineOptions.color(new Random().nextInt()+100);
                        System.out.println("ana hena done gedan we kolo zy el fol");
                    }
                    mMap.addPolyline(new PolylineOptions().addAll(points).width(12).color(new Random().nextInt() + 100));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeSource, longitudeSource)));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeDest, longitudeDest)));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error   >" + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        singleton.addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        System.out.println("ana ready fel map");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.8206, 30.8025), 6));
    }

    public String lnglatFromName(String tripName){
        return tripName.substring(0,tripName.indexOf("#"));
    }

    public String tripNameFromLngLat(String fullName){
        return fullName.substring(fullName.indexOf("#")+1,fullName.length());
    }
}
