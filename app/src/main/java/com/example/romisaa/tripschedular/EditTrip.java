package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import static com.example.romisaa.tripschedular.R.id.dateValue;
import static com.example.romisaa.tripschedular.R.id.notesValue;
import static com.example.romisaa.tripschedular.R.id.sourceValue;
import static com.example.romisaa.tripschedular.R.id.statusValue;
import static com.example.romisaa.tripschedular.R.id.timeValue;

public class EditTrip extends AppCompatActivity {

    TextView sourceValue;
    TextView destinationValue;
    TextView date;
    TextView time;
    TextView status;
    TextView notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sourceValue=(TextView)findViewById(R.id.sourceValue);
        destinationValue=(TextView)findViewById(R.id.destinationValue);
        date=(TextView)findViewById(R.id.dateValue);
        time=(TextView)findViewById(R.id.timeValue);
        status=(TextView)findViewById(R.id.statusValue);
        notes=(TextView)findViewById(R.id.notesValue);


        sourceValue.setText(getIntent().getStringExtra("source"));
        destinationValue.setText(getIntent().getStringExtra("destination"));
        date.setText(getIntent().getStringExtra("date"));
        time.setText(getIntent().getStringExtra("time"));
        status.setText(getIntent().getStringExtra("status"));
        notes.setText(getIntent().getStringExtra("notes"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);

            }
        });
    }

}
