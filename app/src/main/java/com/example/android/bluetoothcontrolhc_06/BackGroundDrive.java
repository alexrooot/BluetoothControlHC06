package com.example.android.bluetoothcontrolhc_06;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BackGroundDrive {
    String LOG_TAG = BackGroundDrive.class.getSimpleName();
    // 1 = true   2 = true-right  3 = true-left  4 = Right   5 = Left   6 = Reverse   7 = Reverse-Right
    // 8 = Reverse-Left   9 = STOP    0 = AI
    String ON = "1";
    String OFF = "9";
    BluetoothSPP bluetooth;
    private Context mContext; //Declare your own context

    Button connect;
    Button on;
    Button off;

    public BackGroundDrive(Context context) {
        mContext = context;
    }


    public void sayLog(Context context, BluetoothSPP BT){
        Log.e(LOG_TAG, "We are in Background going to start BT");
        mContext = context;
        for ( int i = 0 ; i < 50 ; i ++){
            BT.send("1", true);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void initializeBT() {
        Log.e(LOG_TAG, "InitializeBT ");
        Log.e(LOG_TAG, "new BluetoothSPP(mContext) Did Success ");

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(mContext, "Bluetooth is not available", Toast.LENGTH_SHORT).show();

        }

    }


}
