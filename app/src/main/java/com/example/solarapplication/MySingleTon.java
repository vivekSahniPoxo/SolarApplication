package com.example.solarapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleTon {
    private static MySingleTon mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private MySingleTon(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MySingleTon getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleTon(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
