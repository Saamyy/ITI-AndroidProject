package com.example.romisaa.tripschedular;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastFragment extends Fragment {


    DataBaseHandler handler;
    ArrayList<Trip> pastTrips;
    SwipeMenuListView mylistView;
    ListArrayAdapter adapter;
    int listpostion;

    public PastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new DataBaseHandler(getActivity());
        pastTrips=new ArrayList<>();
        pastTrips=handler.getallHistoryTrips();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Past Trips");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_past, container, false);
        mylistView= (SwipeMenuListView) view.findViewById(R.id.pastTrips);
        adapter=new ListArrayAdapter(view.getContext(),R.layout.singlerow2,R.id.tripName,pastTrips);
        mylistView.setAdapter(adapter);

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //list view    row    element rkam kam
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(view.getContext(),ViewTrip.class);
                intent.putExtra("past","past");
                intent.putExtra("trip",pastTrips.get(position));

                startActivity(intent);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem startItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                startItem.setBackground(new ColorDrawable(Color.rgb(50, 205, 50)));
                // set item width
                startItem.setWidth((200));
                // set item title
                startItem.setTitle("Reverse");
                // set item title fontsize
                startItem.setTitleSize(18);
                startItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(startItem);


                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth((200));
                // set item title
                deleteItem.setTitle("Delete");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);

                // set a icon
                //     deleteItem.setIcon(R.drawable.age);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mylistView.setMenuCreator(creator);
        mylistView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        mylistView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                listpostion=position;

            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        mylistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // reverse
                        // l object m3ak ya abo 5al eml l enta 3ayzo fi
                        Trip out= (Trip) mylistView.getItemAtPosition(listpostion);
                        Trip trip=new Trip();
                        trip.setName("Round Trip of "+out.getName());
                        trip.setSource(out.getDestination());
                        trip.setDuration((long) 0);
                        trip.setDestination(out.getSource());
                        trip.setStatus("done");
                        trip.setDate(Calendar.getInstance().getTimeInMillis());
                        handler.addTrip(trip);
                        startNavigation(trip);
                        //Toast.makeText(getActivity(),out.getName(),Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete


                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Do you Really want to delete that trip ");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Trip deleted= (Trip) mylistView.getItemAtPosition(listpostion);
                                        handler.deleteTrip(deleted.getId());
                                        pastTrips.remove(listpostion);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.show();
                        // data.removeView(data.getAdapter().getView(listpostion,null,data));

                        // Toast.makeText(getApplicationContext(),"delete done",Toast.LENGTH_SHORT).show();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
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
