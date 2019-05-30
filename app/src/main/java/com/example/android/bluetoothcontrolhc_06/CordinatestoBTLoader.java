package com.example.android.bluetoothcontrolhc_06;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CordinatestoBTLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = CordinatestoBTLoader.class.getSimpleName();

    private String extra;

    public CordinatestoBTLoader(@NonNull Context context) {
        super(context);
    }
    @Nullable
    @Override
    public Object loadInBackground() {

        return null;
    }
}
