package com.example.romisaa.tripschedular;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Eslam on 17-Mar-17.
 */

public class TaskManager {
    static Context mContext;
    static TaskManager mTaskManager;
    private TaskManager(Context context){
        mContext = context.getApplicationContext();
    }

    public static TaskManager getInstance(Context context){
        if(mTaskManager == null)
            mTaskManager = new TaskManager(context);
        return mTaskManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTask(Trip trip){
        Intent intent = new Intent(mContext,AlarmActivity.class);
        intent.putExtra("trip",trip);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,trip.getId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP,trip.getDate(),pendingIntent);
    }

    public void deleteTask(int tripId){
        Intent intent = new Intent(mContext, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,tripId,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
