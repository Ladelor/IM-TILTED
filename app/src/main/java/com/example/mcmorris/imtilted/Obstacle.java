package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Josh on 3/15/2018.
 * Code for an obstacle in the game
 * TODO: Make them more interesting and dynamic
 */

public class Obstacle implements GameObject {

    //Information for each obstacle
    private Paint paint;
    Rect rect;
    //private float speed;

    Obstacle(Rect startingRect, int color) {
        this.rect = startingRect;
        //this.speed = speed;
        paint = new Paint();
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() { }

    void update(float speed) {
        rect.bottom += speed;
        rect.top += speed;
        //rect.set(rect.left, (int)(rect.top + speed), rect.right, (int)(rect.bottom + speed));
    }
}
