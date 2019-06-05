package com.example.android.bluetoothcontrolhc_06;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

class mLoader extends AsyncTaskLoader<String> {
    public mLoader(Context contex) {
        super(contex);
    }

    @Override
    public String loadInBackground() {
        Log.e("mLoader", "Hello");
        return "Hello";
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
