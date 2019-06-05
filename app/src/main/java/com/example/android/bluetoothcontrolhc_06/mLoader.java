package com.example.android.bluetoothcontrolhc_06;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

class mLoader extends AsyncTaskLoader<String> {
    Context mContext;
    BluetoothSPP BT;
    String mON;
    public mLoader(Context contex, BluetoothSPP bluetooth, String drive) {
        super(contex);
        mContext = contex;
        BT = bluetooth;
        MainActivity mMA = new MainActivity();
        mON = drive;

    }

    @Override
    public String loadInBackground() {
        Log.e("mLoader", "Hello " + mContext);
        BackGroundDrive mBackGroundDrive = new BackGroundDrive(mContext);
        mBackGroundDrive.sayLog(mContext,BT, mON);
        return "Hello";
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
