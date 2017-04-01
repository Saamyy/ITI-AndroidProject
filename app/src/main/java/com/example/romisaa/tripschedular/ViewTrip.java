package com.example.romisaa.tripschedular;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewTrip extends AppCompatActivity {

    TextView sourceValue;
    TextView destinationValue;
    TextView name;
    TextView date;
    TextView time;
    TextView status;
    TextView duration;
    TextView speed;
    TextView notes;
    Trip trip;
    TextView notesHeader;
    LinearLayout notesdata;
    Calendar calendar;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        trip=(Trip) intent.getParcelableExtra("trip");

        name=(TextView)  findViewById(R.id.nameValue);
        name.setText(trip.getName());
        sourceValue=(TextView)findViewById(R.id.sourceValue);
        sourceValue.setText(tripNameFromLngLat(trip.getSource()));
        System.out.println(trip.getStatus());

        destinationValue=(TextView)findViewById(R.id.destinationValue);
        destinationValue.setText(tripNameFromLngLat(trip.getDestination()));

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

        duration= (TextView) findViewById(R.id.durationValue);
        duration.setText(trip.getDuration());

        speed= (TextView) findViewById(R.id.speedValue);
        speed.setText(trip.getAveSpeeed()+" km/h");

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
                textView.setPadding(20, 0, 20, 5);
                textView.setSingleLine(false);  //TODO Check
//                textView.setTextColor(Color.argb(255,255,68,68));
                textView.setTextColor(Color.BLACK);
                textView.setInputType(InputType.TYPE_CLASS_TEXT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().hasExtra("past"))
            return false;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getIntent().hasExtra("past"))
            return false;

        DataBaseHandler dataBaseHandler=new DataBaseHandler(getApplicationContext());
        int id = item.getItemId();

        if (id == R.id.start_now) {

            Uri uri=Uri.parse("google.navigation:q="+trip.getDestination()+"&mode=d");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            dataBaseHandler.changeStatus(trip.getId(),"done");
            setDurationAndSpeed(trip);
            TaskManager.getInstance(getApplicationContext()).deleteTask(trip.getId());
            finishAffinity();
            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeIntent);
            startActivity(intent);


        }
        if (id == R.id.mark_done) {

            dataBaseHandler.changeStatus(trip.getId(),"done");
            TaskManager.getInstance(getApplicationContext()).deleteTask(trip.getId());
            finish();
            // finish();
        }
        if (id == R.id.assign_back_trip) {

            Calendar returnCalendar = Calendar.getInstance();
            returnCalendar.set(Calendar.HOUR_OF_DAY,returnCalendar.get(Calendar.HOUR_OF_DAY)+2);
            Trip roundTrip = new Trip();
            roundTrip.setId((int) System.currentTimeMillis()+1);
            roundTrip.setName(trip.getName()+" - Return");
            roundTrip.setSource(trip.getDestination());
            roundTrip.setDestination(trip.getSource());
            ArrayList<Notes> returnTripNotes= new ArrayList<>();
            Notes note = new Notes();
            note.setContent(trip.getName()+" - Return");
            returnTripNotes.add(note);
            roundTrip.setNotes(returnTripNotes);
            roundTrip.setDate(returnCalendar.getTimeInMillis());
            roundTrip.setStatus("upcoming");
            dataBaseHandler.addTrip(roundTrip);
            TaskManager.getInstance(getApplicationContext()).setTask(roundTrip);

            finish();
            //finish();

        }
        if (id == R.id.delete) {

            int tripId=trip.getId();
            dataBaseHandler.deleteTrip(tripId);

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            TaskManager.getInstance(getApplicationContext()).deleteTask(tripId);
            finish();
            //finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public String tripNameFromLngLat(String fullName){
        return fullName.substring(fullName.indexOf("#")+1,fullName.length());
    }

    public void setDurationAndSpeed(final Trip trip){

        Singleton singleton=Singleton.getInstance(getApplicationContext());
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lnglatFromName(trip.getSource()) + "&destination=" + lnglatFromName(trip.getDestination()) + "&key=AIzaSyBP0TBRYhcEWiIJhMM4GyoWWjWovszvGWk";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success");
                System.out.println(response);
                JSONArray jRoutes;
                JSONArray jLegs;
                String timInHours="0";
                String timInMin="0";
                try {
                    jRoutes = response.getJSONArray("routes");
                    jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");
                    JSONObject jsonObject=jLegs.getJSONObject(0);
                    JSONObject distance=jsonObject.getJSONObject("distance");
                    JSONObject duration=jsonObject.getJSONObject("duration");
                    System.out.println(distance.getString("text")+"<<<<>>>>"+duration.getString("text"));
                    String dist=distance.getString("text").substring(0,distance.getString("text").indexOf("k"));
                    if (duration.getString("text").contains("hours")){
                        timInMin=duration.getString("text").substring(duration.getString("text").indexOf("s")+1,duration.getString("text").indexOf("m"));
                        timInHours=duration.getString("text").substring(0,duration.getString("text").indexOf("h"));

                    }
                    else{
                        timInMin=duration.getString("text").substring(0,duration.getString("text").indexOf("m"));
                        timInHours="0";
                    }
                    System.out.println(dist+"<><><><>"+timInHours+"<><><><>"+timInMin);
                    float speed= Float.parseFloat(dist.replace(",",".")) / (Float.parseFloat(timInHours) + Float.parseFloat(timInMin)/60) ;
                    String avespeed=String.valueOf(speed);
                    System.out.println(avespeed);
                    System.out.println(new DataBaseHandler(getApplicationContext()).changeDurationAndSpeed(trip.getId(),duration.getString("text"),avespeed));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error   >" + error.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        singleton.addToRequestQueue(jsonObjectRequest);
    }



    public String lnglatFromName(String tripName){
        return tripName.substring(0,tripName.indexOf("#"));
    }

}
