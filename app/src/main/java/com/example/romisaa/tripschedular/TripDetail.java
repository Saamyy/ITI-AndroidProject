package com.example.romisaa.tripschedular;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TripDetail extends Activity {

    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        name= (TextView) findViewById(R.id.sourceValue);
        name.setText(getIntent().getStringExtra("name"));
        
    }
}
