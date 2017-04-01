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
                    System.out.println(new DataBaseHandler(context.getApplicationContext()).changeDurationAndSpeed(trip.getId(),duration.getString("text"),avespeed));
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
