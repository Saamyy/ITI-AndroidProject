package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class ViewTrip extends AppCompatActivity {

    TextView sourceValue;
    TextView destinationValue;
    TextView name;
    TextView date;
    TextView time;
    TextView status;
    TextView notesHeader;
    LinearLayout notesdata;
    Calendar calendar;
    Trip trip;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
  //      Notes note = getIntent().getExtras().getParcelable("note");
//        Log.i("MyTag",note.getContent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
         trip=(Trip) intent.getParcelableExtra("trip");

        name=(TextView)  findViewById(R.id.nameValue);
        name.setText(trip.getName());
        sourceValue=(TextView)findViewById(R.id.sourceValue);
        sourceValue.setText(trip.getSource());
        System.out.println(trip.getStatus());

        destinationValue=(TextView)findViewById(R.id.destinationValue);
        destinationValue.setText(trip.getDestination());

        date=(TextView)findViewById(R.id.dateValue);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(trip.getDate()));
        date.setText(dateString);
        System.out.println("l gy mn view "+trip.getDate()+" " );

        time=(TextView)findViewById(R.id.timeValue);
        calendar=Calendar.getInstance();
        calendar.setTimeInMillis(trip.getDate());
        calendar.get(Calendar.HOUR_OF_DAY);
        String am_pm = ((calendar.get(Calendar.AM_PM)) == Calendar.AM) ? "am" : "pm";
        time.setText(calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+ " "+am_pm);

        status=(TextView)findViewById(R.id.statusValue);
        status.setText(trip.getStatus());

        notesHeader=(TextView)findViewById(R.id.notes);
        notesdata= (LinearLayout) findViewById(R.id.notesData);
        if (trip.getNotes().size()==0)
        {
            notesHeader.setTextSize(20);
            notesHeader.setText("There's no notes for that trip");

        }
        else {
            for (int i = 0; i < trip.getNotes().size(); i++)
            {
                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(lparams);
                textView.setSingleLine(false);  //TODO Check
                textView.setTextColor(0xff000000);
                textView.setInputType(InputType.TYPE_CLASS_TEXT);
                textView.setTextSize(20);

                textView.setText(trip.getNotes().get(i).getContent());

            //Create Horizontal View
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                notesdata.addView(textView);
             }

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(intent.hasExtra("past"))
        {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EditTrip.class);
                intent.putExtra("trip",trip);
                startActivity(intent);
            }
        });
    }

}
