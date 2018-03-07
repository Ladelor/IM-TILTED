package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Daniel on 3/6/2018.
 * Defines Path data and behavior
 */

public class PathObject implements GameObject {

    private Paint pathPaint;

    //Determines the horizontal displacement of the sine wave
    private double pathDisplacement;

    //Determines period of sine wave.
    private int pathPeriod;

    //Determines pixelation amount of the path ** Don't be 0
    private int pathDetail;

    //Determines width of rectangles to be used in path
    //Good default = 1/5 of screen width
    private int pathWidth;

    //Pixel height of screen
    private int screenHeight;

    //Determines how big sine wave gets by multiplying it by the tiny sine wave
    //Good default is screen width / 20
    private int pathSineOffset;

    //Determines speed at which path moves
    private double pathSpeed;


    PathObject(double pathDisplacement, int pathPeriod, int pathDetail, int pathWidth, Paint pathPaint, int screenHeight, int pathSineOffset) {
        //TODO input validation?
        this.pathDisplacement = pathDisplacement;
        this.pathPeriod = pathPeriod;
        this.pathDetail = pathDetail;
        this.pathWidth = pathWidth;
        this.pathPaint = pathPaint;
        this.screenHeight = screenHeight;
        this.pathSineOffset = pathSineOffset;
        pathSpeed = 5;
    }


    @Override
    public void draw(Canvas canvas){
        for (int xCoord = 0; xCoord < screenHeight + 10; xCoord += pathDetail) {       //+10 is to make sure it covers the bottom of the screen
            //Double in front of x allows y to change smoothly
            double yCoord = pathSineOffset * (Math.sin((((double) xCoord) / pathPeriod) + pathDisplacement));

            //TODO--make this width more dynamic--so 2 and 5 are not hardcoded
            canvas.drawRect((float) ((2 * pathWidth) + yCoord), xCoord, (float) ((5 * pathWidth) + yCoord), xCoord + pathPeriod, pathPaint);
        }
    }
    @Override
    public void update(){
        pathDisplacement -= pathSpeed / pathPeriod;    //Arbitrarily determined--Needs to fixed/adjustable for higher values of pathDetail--is currently broken
        if (pathDisplacement <= (-0.01 * (screenHeight * (pathSpeed / 5)))) {
            //Increases speed the longer we go
            //TODO make frames less jerky at high speeds, figure out how to reduce exponential speed increase.
            pathSpeed += 5;
            //pathPeriod += 5;  //Would like to implement this, but its too jerky at slow speeds
            Log.d("PathObject", "Speed: " + pathSpeed);
        }
        Log.d("PathObject", "Height: " + screenHeight + ", pathDisplacement: " + pathDisplacement);
    }
}
