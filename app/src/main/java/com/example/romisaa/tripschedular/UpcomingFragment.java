package com.example.romisaa.tripschedular;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    String [ ]values={"samy","hhassan"};
    ArrayAdapter adapter;
    ListView mylistView;


    public UpcomingFragment() {
        // Required empty public constructor
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Upcoming Trips");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_upcoming, container, false);
        mylistView= (ListView) view.findViewById(R.id.tripNames);
        adapter=new ArrayAdapter(view.getContext(),R.layout.singlerow,R.id.textView,values);
        mylistView.setAdapter(adapter);

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //list view    row    element rkam kam
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(view.getContext(),ViewTrip.class);
                intent.putExtra("name",values[position]);
                startActivity(intent);
            }
        });
        return view;
    }

    public void startNavigation(Trip trip){

        // delete from array
        // change status in db to done
        Uri uri=Uri.parse("google.navigation:q="+trip.getDestination()+"&mode=d");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }


}
