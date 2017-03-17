package com.example.romisaa.tripschedular;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.http.conn.ConnectTimeoutException;

/**
 * Created by Eslam on 17-Mar-17.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Trip trip = intent.getExtras().getParcelable("trip");

        if (intent.getStringExtra("flag").equals("done")) {

            //change status in db to done
            new DataBaseHandler(context.getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_DONE);

            //Starting The Map
            Uri uri = Uri.parse("google.navigation:q=" + trip.getDestination() + "&mode=d");
            Intent outIntent = new Intent(Intent.ACTION_VIEW, uri);
            outIntent.setPackage("com.google.android.apps.maps");
            outIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(outIntent);


        }
        if (intent.getStringExtra("flag").equals("cancelled")) {

            //change status in db to cancelled
            new DataBaseHandler(context.getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_CANCELLED);
        }

    }

}
