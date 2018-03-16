package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Josh on 3/6/2018.
 * This is what the path is made of
 */

class PathRect implements GameObject {

    //xPosition will always be center screen (for now)
    RectF pathRect;
    private Paint pathPaint;

    PathRect(RectF pathRect, int color) {
        this.pathRect = pathRect;
        pathPaint = new Paint();
        pathPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(pathRect, pathPaint);
    }

    @Override
    public void update() {
    }

    void update(float pathSpeed) {
        pathRect.bottom += pathSpeed;
        pathRect.top += pathSpeed;
        //pathRect.set(pathRect.left, pathRect.top + pathSpeed, pathRect.right, pathRect.bottom + pathSpeed);
    }
}
