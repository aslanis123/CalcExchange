package com.example.dimtz.calcexchange;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by dimtz on 8/11/2017.
 */

public class ConnectionSnigleton {
    private static ConnectionSnigleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private ConnectionSnigleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();


    }

    public static synchronized ConnectionSnigleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConnectionSnigleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
