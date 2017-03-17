package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ViewTrip extends AppCompatActivity {

    TextView sourceValue;
    TextView destinationValue;
    TextView date;
    TextView time;
    TextView status;
    TextView notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        Trip trip=(Trip) intent.getParcelableExtra("trip");
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

}
