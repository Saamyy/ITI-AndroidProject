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
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {
    String sourceString;
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

        trip = getIntent().getExtras().getParcelable("trip");

        sourceString = trip.getSource();
        destinationString = trip.getDestination();

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

}
