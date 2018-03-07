package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;

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


    PathObject(double pathDisplacement, int pathPeriod, int pathDetail, int pathWidth, Paint pathPaint, int screenHeight, int pathSineOffset) {
        //TODO input validation?
        this.pathDisplacement = pathDisplacement;
        this.pathPeriod = pathPeriod;
        this.pathDetail = pathDetail;
        this.pathWidth = pathWidth;
        this.pathPaint = pathPaint;
        this.screenHeight = screenHeight;
        this.pathSineOffset = pathSineOffset;
    }


    @Override
    public void draw(Canvas canvas){
        for (int xCoord = 0; xCoord < screenHeight + 10; xCoord += pathDetail) {       //+10 is to make sure it covers the bottom of the screen
            //Double in front of x allows y to change smoothly
            double yCoord = pathSineOffset * (Math.sin((((double) xCoord) / pathPeriod) + pathDisplacement));

            //TODO--make this width more dynamic--so 2 and 3 are not hardcoded
            canvas.drawRect((float) ((2 * pathWidth) + yCoord), xCoord, (float) ((3 * pathWidth) + yCoord), xCoord + pathPeriod, pathPaint);

        }

    }

    @Override
    public void update(){
        pathDisplacement -= 5.0 / pathPeriod;    //Arbitrarily determined--Needs to fixed/adjustable for higher values of pathDetail--is currently broken
    }
}
