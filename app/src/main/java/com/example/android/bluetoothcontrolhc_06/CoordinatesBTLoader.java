package com.example.android.bluetoothcontrolhc_06;

import android.content.AsyncTaskLoader;
import android.support.annotation.Nullable;
import android.util.Log;

public class CoordinatesBTLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = CoordinatesBTLoader.class.getSimpleName();

    private String btConnectionOk;

    public CoordinatesBTLoader(String connected){
        super(null);

        btConnectionOk = connected;
    }


    @Nullable
    @Override
    public Object loadInBackground() {
        while (btConnectionOk == "TRUE" ) {
            System.out.println("We are in background tread");
            Log.e("CoordinateBTLoader", "Forced to start on btConnectionol" + btConnectionOk);
        }
        return null;
    }
}