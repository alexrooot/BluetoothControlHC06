package com.example.android.bluetoothcontrolhc_06;

import android.app.Activity;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements DragNDriveView.JoystickListener, LoaderManager.LoaderCallbacks<Cursor>{
    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID = 1;

    private LoaderManager lm;

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    // 1 = true   2 = true-right  3 = true-left  4 = Right   5 = Left   6 = Reverse   7 = Reverse-Right
// 8 = Reverse-Left   9 = STOP    0 = AI
    final String ON     = "1";
    final String OFF    = "9";
    String connection = "FALSE";

    BluetoothSPP bluetooth;

    Button connect;
    Button on;
    Button off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        DragNDriveView dragView = new DragNDriveView(this);
        setContentView(R.layout.activity_main);

        bluetooth = new BluetoothSPP(this);

        connect = (Button) findViewById(R.id.connect);
        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connected to " + name);
            }

            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
            }

            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(ON, true);
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(OFF, true);
            }
        });


             lm = getLoaderManager();
           // lm.initLoader(LOADER_ID, null, mCallbacks); // starts the loader at found in the end of this file


    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }


    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
            connection = "TRUE";
            Log.e("MainActivity", "Connection string value is " + connection);
            lm.initLoader(LOADER_ID, null, this);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
       // */
    }
/*
    @Override
    public void JoystickAction(float xCoordinate, float yCoordinate, int controllerId) {
       Log.e("Right Joystick ", "X : " + xCoordinate + " Y : " + yCoordinate);
        String sideString = Float.toString(xCoordinate);
        System.out.println(sideString + " x first string");
        String subSideSting = sideString.substring(0, 3);
        System.out.println(subSideSting + " x second string");


        String fowardOrReverse = Float.toString((yCoordinate));
        System.out.println(fowardOrReverse + "y firt string");
        String subFowardOrRevers = fowardOrReverse.substring(0, 3); // when y is - sub = .
        System.out.println(subFowardOrRevers + " y second string");
       // Log.e("Joystick converted", "x is : " + subSideSting + " y is : " +subFowardOrRevers );


        if ( yCoordinate <0 && (xCoordinate < 0.25  && xCoordinate <-0.25)){
            bluetooth.send("1 ,",true); // True
            }else if (yCoordinate < 0 && (xCoordinate > 0.25 && xCoordinate < 0.75)){
            bluetooth.send("2 ,", true); // True/Right
            }else if ( yCoordinate < 0 && (xCoordinate <-0.25 && xCoordinate < -0.75)) {
            bluetooth.send("3 ,", true);
        }   else if ( xCoordinate < -0.75){
            bluetooth.send("4",true);
            }else if ( xCoordinate > 0.75 ){
            bluetooth.send("5",true);
            }else if ( yCoordinate > 0 && ( xCoordinate > 0.25 && xCoordinate < 0.75)) {
            bluetooth.send("6 ,", true);
            }else if (yCoordinate > 0 && (xCoordinate <-0.25 && xCoordinate < -0.75 )) {
            bluetooth.send("7", true);
            }else if ( yCoordinate > 0  && (xCoordinate <-0.25 && xCoordinate < -0.75)){
            bluetooth.send("8",true);

        String directionFR = subFowardOrRevers.substring(0,1);
        System.out.println(directionFR + directionFR.length() + " first char of y");

        String directionLR = subSideSting.substring(0,1);
        System.out.println( directionLR +directionLR.length() + " first char of X");

        if (directionFR == "0" && directionLR == "0" ) {
            String temFR = subFowardOrRevers.substring(1, 2);
            String temLR = subSideSting.substring(1, 2);
            bluetooth.send("6",true);

            //Reverse
            bluetooth.send(temFR+ " "+ temLR +" REV", true);
            Log.e("Bluetooth nagative both", temFR + " " + temLR + " REV");

        }
         if
         (directionLR == "-" && directionFR != "-") {
            String temLR = subSideSting.substring(1, 2);
            bluetooth.send(subFowardOrRevers + " " + temLR + " FOW", true);
            Log.e("Bluetooth negative", subFowardOrRevers + " " + temLR + " FOW");

        }  if (directionLR != "-" && directionFR == "-") {
            String tempFR = subFowardOrRevers.substring(1, 2);
            bluetooth.send(tempFR + " " + subSideSting + " REV", true);
            Log.e("Bluetooth side negative", tempFR + " " + subSideSting + " REV");
        } else {
            String tempY = subFowardOrRevers.substring(2, 3);
            String tempx = subSideSting.substring(2,3);
            bluetooth.send(subFowardOrRevers + " " + subSideSting + " FOW", true);
            Log.e("Sending Bluetooth", tempY + " " + tempx + " FOW");




    }


}*/



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e("MaiActivity", "Loader started in main activity");
        return new CoordinatesBTLoader( this, connection );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void JoystickAction(float xCoordinate, float yCoordinate, int controllerId) {

    }
}