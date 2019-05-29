package com.example.android.bluetoothcontrolhc_06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

//use Surfaceview utilities  surfaceholder.callback to notify class of specific events View.ontouchlistener idicate that its touched
                            //also generate their overwrite methods
public class DragNDriveView extends SurfaceView implements SurfaceHolder.Callback , View.OnTouchListener{


    private float centerX, centerY, baseRadius, hatRadius; // surfaceView location variables
    private JoystickListener joystickCallback; // pass in callback messenger for view listener

    public DragNDriveView(Context context) {
        super(context);
        Log.e("DragNDrive", "DragNDrive first constructor");
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public DragNDriveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("DragNDrive", "DragNDrive first constructor");
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public DragNDriveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("DragNDrive", "DragNDrive first constructor");
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    /*
    Define how the size of joyStick by manipulating the sizes into ratios
     */
    void setupDimensions(){
        Log.e("DragNDrive", "setupDimensions function started");
        centerX = getWidth()/2;     // get width size of xml view
        centerY = getHeight()/2;    //get height size of xml view
        baseRadius = Math.min(getWidth(), getHeight()) /3;  //base circle size base on xml view
        hatRadius = Math.min(getWidth(),getHeight()/5); //top circle size base on xml view
        Log.e("DragNDrive", "setupDimensions function completed");
    }


    /*
    Find were to draw the hat using the newX, newY
     */
    private void drawJoystick(float newX, float newY){
        Log.e("DrivNDrive", "funtion draJoystick stateed");
        if(getHolder().getSurface().isValid()) {  //execute only whe SurfaceView is working
            Log.e("DriveNDrive", "drawJoyStick if getHolder().getSurface().isValid()");
            Canvas mCanvas = this.getHolder().lockCanvas(); // get access of SurfaceView
            Log.e("DriveNDrive", "drawJoystick mCanvas  created");
            Paint newColors = new Paint(); // get access to change color
            Log.e("DriveNDrive", "drawJoystick newColors created");

            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // set the color to be translucent of the hat
            Log.e("DragNDrive", "drawJoustick mCanvas setup completed");
            newColors.setARGB(255, 45, 100, 12);// color for base
            Log.e("DragNDrive", "drawJoystick color setup complete");

            mCanvas.drawCircle(centerX, centerY, baseRadius, newColors); // define circle properties for base
            Log.e("DragNDrive", "mCanvas draw circle base location complete");


            newColors.setARGB(255, 255, 200, 12);//color for hat
            Log.e("DragNDrive", "drawJoystick new color set for hat complete");
            mCanvas.drawCircle(newX, newY, hatRadius, newColors); // define circle properties for hat
            Log.e("DragNDrive", "drawJoystick mCanvas draw hat circle location complete");
            getHolder().unlockCanvasAndPost(mCanvas);
            Log.e("DragNDrive", "drawJoystick getHolder.... I think we just made the surfaceView ready");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("DragNDrive", "overwrite surfaceCreated");
        setupDimensions(); // get the right size to draw the canvas/SurfaceView
        Log.e("DragNDrive", "setupDimension fuction call complete");
        drawJoystick(centerX, centerY);
        Log.e("DragNDrive ", "drawJoystick fucntion call complete");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("DragNDrive", "surfacechange ovewrite started");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("DragNDrive", "surfaceHolder overwrite stated");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v.equals(this)){ // make sure that only this view surface/DragNDrive is still being touched
            Log.e("DragNDrive", "onTouch if v.quals this view is being touched");

            if (event.getAction() !=  event.ACTION_UP){  // if user lift finger reset hat or redraw hat under finger
                Log.e("DragNDrop" ,"onTouch if getAction user let go of view");

            /*
            Get the displacment value of hat
            displacement = sqrt((x'-x) ^ 2 + (y'-y)^2)
             */
                float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));

                    if(displacement < baseRadius){

                        drawJoystick(event.getX(), event.getY()); // the hat is still inside the radius
                        joystickCallback.JoystickAction((event.getX() - centerX) /baseRadius, (event.getY() - centerY)/baseRadius,getId());
                        Log.e("DragNDriveView", "onTouch if hat is inside base");
                    }
                    else{  //hat is outside the radius bring it back within scope

                        float ratio = baseRadius / displacement; // find the ratio of how far it is
                        float constrainedX = centerX + (event.getX() - centerX) * ratio;
                        float constrainedY = centerY + (event.getY() - centerY) * ratio;
                        drawJoystick(constrainedX, constrainedY);
                        joystickCallback.JoystickAction((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());
                        Log.e("DragNDrop", "onTouch if hat is outside base");

                    }

                drawJoystick(event.getX(), event.getY());
                    Log.e("DragNDrive", "onTouch  event.getAction() !=  We are touching the view go to drawstick" );

            }
            else // reset the hat to center
            {
                drawJoystick(centerX,centerY);
                joystickCallback.JoystickAction(0,0,getId());
                Log.e("DragNDrive", "done with onTouch It is Reset to center");


            }

        }

        return true; //false prevents the onTouch method from receiving future touches.
    }

    public interface JoystickListener // this will be the interface class for callback messages
    {

        void JoystickAction(float xCoordinate, float yCoordinate, int controllerId);



    }
}
