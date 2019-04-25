package com.example.android.bluetoothcontrolhc_06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
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
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public DragNDriveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
    }

    public DragNDriveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this); // indicate we want this event overwrite in this class
        setOnTouchListener(this); // forces it to use our overwrite version
    }

    /*
    Define how the size of joyStick by manipulating the sizes into ratios
     */
    void setupDimensions(){
        centerX = getWidth()/2;     // get width size of xml view
        centerY = getHeight()/2;    //get height size of xml view
        baseRadius = Math.min(getWidth(), getHeight()) /3;  //base circle size base on xml view
        hatRadius = Math.min(getWidth(),getHeight()/5); //top circle size base on xml view

    }


    /*
    Find were to draw the hat using the newX, newY
     */
    private void drawJoystick(float newX, float newY){

        if(getHolder().getSurface().isValid()) {  //execute only whe SurfaceView is working
            Canvas mCanvas = this.getHolder().lockCanvas(); // get access of SurfaceView
            Paint newColors = new Paint(); // get access to change color

            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // set the color to be translucent of the hat
            newColors.setARGB(255, 45, 100, 12);// color for base

            mCanvas.drawCircle(centerX, centerY, baseRadius, newColors); // define circle properties for base


            newColors.setARGB(255, 255, 200, 12);//color for hat
            mCanvas.drawCircle(newX, newY, hatRadius, newColors); // define circle properties for hat
            getHolder().unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setupDimensions(); // get the right size to draw the canvas/SurfaceView
        drawJoystick(centerX, centerY);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v.equals(this)){ // make sure that only this view surface/DragNDrive is still being touched

            if (event.getAction() !=  event.ACTION_UP){  // if user lift finger reset hat or redraw hat under finger

            /*
            Get the displacment value of hat
            displacement = sqrt((x'-x) ^ 2 + (y'-y)^2)
             */
                float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));

                    if(displacement < baseRadius){

                        drawJoystick(event.getX(), event.getY()); // the hat is still inside the radius
                        joystickCallback.JoystickAction((event.getX() - centerX) /baseRadius, (event.getY() - centerY)/baseRadius,getId());

                    }
                    else{  //hat is outside the radius bring it back within scope

                        float ratio = baseRadius / displacement; // find the ratio of how far it is
                        float constrainedX = centerX + (event.getX() - centerX) * ratio;
                        float constrainedY = centerY + (event.getY() - centerY) * ratio;
                        drawJoystick(constrainedX, constrainedY);
                        joystickCallback.JoystickAction((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());

                    }

                drawJoystick(event.getX(), event.getY());

            }
            else // reset the hat to center
            {
                drawJoystick(centerX,centerY);
                joystickCallback.JoystickAction(0,0,getId());

            }

        }

        return true; //false prevents the onTouch method from receiving future touches.
    }

    public interface JoystickListener // this will be the interface class for callback messages

    {

        void JoystickAction(float xCoordinate, float yCoordinate, int controllerId);

    }
}
