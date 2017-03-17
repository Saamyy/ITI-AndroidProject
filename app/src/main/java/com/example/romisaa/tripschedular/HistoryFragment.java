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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    MapView mapView;
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
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        mapView= (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                drawOnMap(googleMap,new DataBaseHandler(getActivity().getApplicationContext()).getallHistoryTrips());
                mapView.onResume();
            }
        });
    return view;
    }

    public LatLng getLatLngFromName(String place){
        try {
            List<Address> adresses=new Geocoder(getActivity().getApplicationContext()).getFromLocationName(place,5);
            LatLng latLng=new LatLng(adresses.get(0).getLatitude(),adresses.get(0).getLongitude());
            return latLng;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void drawOnMap(GoogleMap map, ArrayList<Trip> trips){

        System.out.println("size="+trips.size());

        for (int i=0;i<trips.size();i++){
            map.addMarker(new MarkerOptions().position(getLatLngFromName(trips.get(i).getSource())).title("Source"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5, -0.1),50));
            map.addMarker(new MarkerOptions().position(getLatLngFromName(trips.get(i).getDestination())).title("destination"));
            map.addPolyline(new PolylineOptions().add(getLatLngFromName(trips.get(i).getSource()), getLatLngFromName(trips.get(i).getDestination())).width(5).color((int) Math.round(Math.random()*100)));
        }
    }

}
