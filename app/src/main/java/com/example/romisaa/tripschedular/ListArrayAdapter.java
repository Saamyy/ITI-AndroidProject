package com.example.romisaa.tripschedular;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Samy-WorkStation on 3/17/2017.
 */

public class ListArrayAdapter  extends ArrayAdapter{

    List <Trip> trips;
    Context context;

    public ListArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        trips=objects;
        this.context=context;

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=convertView;
        ViewHolder viewHolder;
        if (rowView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE) ;
            rowView=inflater.inflate(R.layout.singlerow2,parent,false);
            viewHolder=new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)rowView.getTag();
        }
         viewHolder.getTripName().setText(trips.get(position).getName());
         viewHolder.getTripDate().setText(dateToString(trips.get(position).getDate()));
        switch (trips.get(position).getStatus()) {
            case Trip.STATUS_CANCELLED:
                viewHolder.getImage().setImageResource(R.drawable.trip_cancelled);
                break;
            case Trip.STATUS_POSTPONED:
                viewHolder.getImage().setImageResource(R.drawable.trip_postponed);
                break;
            case Trip.STATUS_DONE:
                viewHolder.getImage().setImageResource(R.drawable.trip_done);
                break;
        }
        return rowView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String dateToString(long dateInLong){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInLong);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy - h:mm a");

        Calendar today = Calendar.getInstance();
        if(calendar.get(Calendar.YEAR)==today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH)==today.get(Calendar.DAY_OF_MONTH) ){
            format = new SimpleDateFormat(" - h:mm a");
            return("Today "+format.format(calendar.getTime()));
        }

        if(calendar.get(Calendar.YEAR)==today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH)+1==today.get(Calendar.DAY_OF_MONTH) ){
            format = new SimpleDateFormat(" - h:mm a");
            return("Yesterday "+format.format(calendar.getTime()));
        }
        if(calendar.get(Calendar.YEAR)==today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH)-1==today.get(Calendar.DAY_OF_MONTH) ){
            format = new SimpleDateFormat(" - h:mm a");
            return("Tomorrow "+format.format(calendar.getTime()));
        }

        return format.format(calendar.getTime());
    }

}
