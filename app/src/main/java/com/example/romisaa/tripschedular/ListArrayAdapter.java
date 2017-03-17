package com.example.romisaa.tripschedular;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
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



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=convertView;
        ViewHolder viewHolder;
        if (rowView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE) ;
            rowView=inflater.inflate(R.layout.singlerow,parent,false);
            viewHolder=new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)rowView.getTag();
        }
         viewHolder.getTripName().setText(trips.get(position).getName());
        return rowView;
    }
}
