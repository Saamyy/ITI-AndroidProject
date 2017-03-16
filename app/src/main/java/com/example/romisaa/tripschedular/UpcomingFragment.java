package com.example.romisaa.tripschedular;

import android.app.Fragment;
import android.content.Intent;
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

    String [ ]values={" Giza ","Nasr City "," 6 October "," Haram "};
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

}

/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mylistView= (ListView) findViewById(R.id.listView);
        adapter=new ArrayAdapter(getApplicationContext(),R.layout.singlerow,R.id.textView6,values);
        mylistView.setAdapter(adapter);
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //list view    row    element rkam kam
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),"youClicked"+(String) parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }
        });
    }



*/