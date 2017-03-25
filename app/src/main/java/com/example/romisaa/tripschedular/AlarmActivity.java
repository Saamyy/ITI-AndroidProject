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

    public String tripNameFromLngLat(String fullName){
        return fullName.substring(fullName.indexOf("#")+1,fullName.length());
    }

}
