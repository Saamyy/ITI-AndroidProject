package com.example.romisaa.tripschedular;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {
    String sourceString;
    String destinationString;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
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
                new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_DONE);

                //TODO Replace Toast with Ehab's Map Actiivity
                Toast.makeText(AlarmActivity.this, "Go To Map With Source " + sourceString + " and Destination " + destinationString, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder builder = new Notification.Builder(AlarmActivity.this)
                        .setSmallIcon(R.color.colorPrimaryDark)
                        .setContentTitle("Trip Postponed")
                        .setContentText("You have a postponed trip to "+destinationString)
                        .setAutoCancel(true);

                //set status to postponed
                new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(),Trip.STATUS_POSTPONED);

                //TODO Intent Should be modified to open Ehab's Map Activity

//                Intent notificationIntent = new Intent(getApplicationContext(),Main2Activity.class);
//                notificationIntent.putExtra("jets",1);

//                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
//                builder.setContentIntent(pendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify((int)System.currentTimeMillis(), builder.build());

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status to cancelled.
                new DataBaseHandler(getApplicationContext()).changeStatus(trip.getId(),Trip.STATUS_CANCELLED);
                finish();
            }
        });
    }


}
