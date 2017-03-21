package com.example.romisaa.tripschedular;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Romisaa on 3/19/2017.
 */

public class Singleton {
    private RequestQueue requestQueue;
    private static Singleton singleton;
    private Context context;
    private Request request;

    private Singleton(Context context) {
        this.context = context;
        this.requestQueue=getRequestQueue();
    }
    public static synchronized Singleton getInstance(Context context)
    {
        if (singleton==null)
            singleton=new Singleton(context);
        return singleton;
    }
    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
        {
            Cache cache=new DiskBasedCache(context.getCacheDir(),1024*1024*3);
            Network network=new BasicNetwork(new HurlStack());
            requestQueue= new RequestQueue(cache,network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public void addToRequestQueue (Request request)
    {
        requestQueue.add(request);
    }

}
