package com.example.romisaa.tripschedular;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmActivity extends AppCompatActivity {
    String nameString;
    String destinationString;
    Trip trip;
    private Vibrator vib;
   private Ringtone r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        if (savedInstanceState==null) {
            try {

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        while (r.isPlaying()) {
                            vib.vibrate(500);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Button startButton = (Button) findViewById(R.id.button2);
        Button laterButton = (Button) findViewById(R.id.button3);
        Button cancelButton = (Button) findViewById(R.id.button4);
        TextView alarmTitle = (TextView) findViewById(R.id.alarmTitle);
        TextView alarmDesc = (TextView) findViewById(R.id.alarmDesc);

        trip = getIntent().getExtras().getParcelable("trip");
        nameString = trip.getName();
        destinationString = tripNameFromLngLat(trip.getDestination());

        alarmTitle.setText(nameString);
        alarmDesc.setText("It's time for your trip to "+destinationString);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set status to done
                r.stop();
                vib.cancel();
                new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_DONE);

                Uri uri=Uri.parse("google.navigation:q="+trip.getDestination()+"&mode=d");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                setDurationAndSpeed(trip);
                startActivity(intent);
                finish();
            }
        });

        laterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Changing Status to Postponed
                    r.stop();
                    vib.cancel();
                    new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(),Trip.STATUS_POSTPONED);


                    //Setting The Notification
                    Intent contentIntent = new Intent(getApplicationContext(),Receiver.class);
                    contentIntent.putExtra("flag","done");
                    contentIntent.putExtra("trip",trip);
                    PendingIntent contentPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),trip.getId(),contentIntent,PendingIntent.FLAG_CANCEL_CURRENT);

                    Intent deleteIntent = new Intent(getApplicationContext(),Receiver.class);
                    deleteIntent.putExtra("flag","cancelled");
                    deleteIntent.putExtra("trip",trip);
                    PendingIntent deletePendingIntent = PendingIntent.getBroadcast(getApplicationContext(),trip.getId()+1,deleteIntent,PendingIntent.FLAG_CANCEL_CURRENT);


                    Notification.Builder builder = new Notification.Builder(AlarmActivity.this)
                            .setSmallIcon(R.color.colorPrimaryDark)
                            .setContentTitle("Trip Postponed")
                            .setContentText("You have a postponed trip to "+destinationString)
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .setContentIntent(contentPendingIntent)
                            .setDeleteIntent(deletePendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.notify(trip.getId(), builder.build());
                    finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status to cancelled.
                r.stop();
                vib.cancel();
                new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(),Trip.STATUS_CANCELLED);
                finish();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //3ashan mantfdehsh :D
        outState.putString("aywaa","hassa");
    }

    public String lnglatFromName(String tripName){
        return tripName.substring(0,tripName.indexOf("#"));
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

}
