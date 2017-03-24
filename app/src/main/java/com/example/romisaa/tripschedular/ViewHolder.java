package com.example.romisaa.tripschedular;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Samy-WorkStation on 3/17/2017.
 */

public class ViewHolder {
    View converView;
    TextView tripName;
    TextView tripDate;

    public ViewHolder(View converView) {
        this.converView = converView;
    }

    public TextView getTripName() {
        if (tripName==null)
            tripName=(TextView) converView.findViewById(R.id.tripName);
        return tripName;
    }

    public TextView getTripDate(){
        if(tripDate==null)
            tripDate=(TextView) converView.findViewById(R.id.tripDate);
        return tripDate;
    }
}
