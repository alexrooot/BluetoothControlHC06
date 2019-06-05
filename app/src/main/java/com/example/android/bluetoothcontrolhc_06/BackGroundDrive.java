package com.example.android.bluetoothcontrolhc_06;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BackGroundDrive implements DragNDriveView.JoystickListener {
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


    public void sayLog(Context context, BluetoothSPP BT , String drive) {
        Log.e(LOG_TAG, "We are in Background going to start BT");
        mContext = context;



        for (int i = 0; i < 10      ; i++) {
            BT.send("1", true);

            Log.e(LOG_TAG, "We are sending the message for the next time "+i);
            try {
                Thread.sleep(3000);
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


        if (yCoordinate < 0 && (xCoordinate < 0.25 && xCoordinate < -0.25)) {
            bluetooth.send("1 ,", true); // True
        } else if (yCoordinate < 0 && (xCoordinate > 0.25 && xCoordinate < 0.75)) {
            bluetooth.send("2 ,", true); // True/Right
        } else if (yCoordinate < 0 && (xCoordinate < -0.25 && xCoordinate < -0.75)) {
            bluetooth.send("3 ,", true);
        } else if (xCoordinate < -0.75) {
            bluetooth.send("4", true);
        } else if (xCoordinate > 0.75) {
            bluetooth.send("5", true);
        } else if (yCoordinate > 0 && (xCoordinate > 0.25 && xCoordinate < 0.75)) {
            bluetooth.send("6 ,", true);
        } else if (yCoordinate > 0 && (xCoordinate < -0.25 && xCoordinate < -0.75)) {
            bluetooth.send("7", true);
        } else if (yCoordinate > 0 && (xCoordinate < -0.25 && xCoordinate < -0.75)) {
            bluetooth.send("8", true);
        }
    }
}