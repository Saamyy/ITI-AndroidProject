package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewTrip extends AppCompatActivity {

    TextView sourceValue;
    TextView destinationValue;
    TextView date;
    TextView time;
    TextView status;
    TextView notes;
    Trip trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        trip=(Trip) intent.getParcelableExtra("trip");
        sourceValue=(TextView)findViewById(R.id.sourceValue);
        sourceValue.setText(trip.getSource());
        destinationValue=(TextView)findViewById(R.id.destinationValue);
        destinationValue.setText(trip.getDestination());
        date=(TextView)findViewById(R.id.dateValue);
        time=(TextView)findViewById(R.id.timeValue);
        status=(TextView)findViewById(R.id.statusValue);
        status.setText(trip.getStatus());
        notes=(TextView)findViewById(R.id.notesValue);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EditTrip.class);

                intent.putExtra("source",sourceValue.getText().toString());
                intent.putExtra("destination",destinationValue.getText().toString());
                intent.putExtra("date",date.getText().toString());
                intent.putExtra("time",time.getText().toString());
                intent.putExtra("status",status.getText().toString());
                intent.putExtra("notes",notes.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        DataBaseHandler dataBaseHandler=new DataBaseHandler(getApplicationContext());
        int id = item.getItemId();

        if (id == R.id.start_now) {

            Uri uri=Uri.parse("google.navigation:q="+trip.getDestination()+"&mode=d");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            dataBaseHandler.changeStatus(trip.getId(),"done");
            startActivity(intent);

        }
        if (id == R.id.mark_done) {

            dataBaseHandler.changeStatus(trip.getId(),"done");
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
           // finish();
        }
        if (id == R.id.assign_back_trip) {

            Calendar returnCalendar = Calendar.getInstance();
            returnCalendar.set(Calendar.HOUR_OF_DAY,returnCalendar.get(Calendar.HOUR_OF_DAY)+2);
            Trip roundTrip = new Trip();
            roundTrip.setId((int) System.currentTimeMillis()+1);
            roundTrip.setName( "Return trip of "+trip.getName());
            roundTrip.setSource(trip.getDestination());
            roundTrip.setDestination(trip.getSource());
            ArrayList<Notes> returnTripNotes= new ArrayList<>();
            Notes note = new Notes();
            note.setContent("Return trip of "+trip.getName());
            returnTripNotes.add(note);
            roundTrip.setNotes(returnTripNotes);
            roundTrip.setDate(returnCalendar.getTimeInMillis());
            roundTrip.setStatus("upcoming");
            dataBaseHandler.addTrip(roundTrip);

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //finish();

        }
        if (id == R.id.delete) {

            int tripId=trip.getId();
            dataBaseHandler.deleteTrip(tripId);

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
