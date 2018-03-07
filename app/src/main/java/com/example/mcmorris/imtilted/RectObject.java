package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Daniel on 3/6/2018.
 * Defines rectangle data and behavior
 */

//Honestly I don't think we need a rectangle class--it's all self contained within path
    //UNLESS this is for the obstacles
public class RectObject implements GameObject {

    //Upper left coordinates
    private int x;
    private int y;

    //Dimensions
    private int width;
    private int height;

    //Rectangle paint
    private Paint rectPaint;

    //Default constructor
    public RectObject(){
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    //Specifying constructor
    public RectObject(int x, int y, int width, int height, int rectColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rectPaint = new Paint();
        rectPaint.setColor(rectColor);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), rectPaint);
    }

    @Override
    public void update(){
        //Not sure what's happening here

    }

}
