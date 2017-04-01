package com.example.romisaa.tripschedular;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eslam on 17-Mar-17.
 */
public class Receiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;
        Trip trip = intent.getExtras().getParcelable("trip");

        if (intent.getStringExtra("flag").equals("done")) {

            //change status in db to done
            new DataBaseHandler(context.getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_DONE);

            //Starting The Map
            Uri uri = Uri.parse("google.navigation:q=" + trip.getDestination() + "&mode=d");
            Intent outIntent = new Intent(Intent.ACTION_VIEW, uri);
            outIntent.setPackage("com.google.android.apps.maps");
            outIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            setDurationAndSpeed(trip);
            context.startActivity(outIntent);


        }
        if (intent.getStringExtra("flag").equals("cancelled")) {

            //change status in db to cancelled
            new DataBaseHandler(context.getApplicationContext()).changeStatus(trip.getId(), Trip.STATUS_CANCELLED);
        }

    }

    public String lnglatFromName(String tripName){
        return tripName.substring(0,tripName.indexOf("#"));
    }

    public String tripNameFromLngLat(String fullName){
        return fullName.substring(fullName.indexOf("#")+1,fullName.length());
    }

    public void setDurationAndSpeed(final Trip trip){

        Singleton singleton=Singleton.getInstance(context.getApplicationContext());
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lnglatFromName(trip.getSource()) + "&destination=" + lnglatFromName(trip.getDestination()) + "&key=AIzaSyBP0TBRYhcEWiIJhMM4GyoWWjWovszvGWk";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success");
                System.out.println(response);
                JSONArray jRoutes;
                JSONArray jLegs;
                long distanceInmeters;
                long durationInSec;
                long avgSpeed;
                try {
                    jRoutes = response.getJSONArray("routes");
                    jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");
                    JSONObject jsonObject=jLegs.getJSONObject(0);
                    JSONObject distance=jsonObject.getJSONObject("distance");
                    JSONObject duration=jsonObject.getJSONObject("duration");
                    System.out.println(distance.getString("value")+"<<<<>>>>"+duration.getString("value"));
                    distanceInmeters=distance.getLong("value");
                    durationInSec=duration.getLong("value");
                    System.out.println(distanceInmeters+"<><><><>"+durationInSec);
                    avgSpeed=distanceInmeters/durationInSec;
                    System.out.println(avgSpeed);
                    System.out.println(new DataBaseHandler(context.getApplicationContext()).changeDurationAndSpeed(trip.getId(),durationInSec+"",avgSpeed+""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error   >" + error.getMessage());
                Toast.makeText(context.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        singleton.addToRequestQueue(jsonObjectRequest);
    }

}
