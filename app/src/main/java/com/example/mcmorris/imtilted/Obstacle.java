package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Josh on 3/15/2018.
 */

public class Obstacle implements GameObject {

    private Paint paint;
    Rect rect;
    private float speed;

    Obstacle(Rect startingRect, int color, float speed) {
        this.rect = startingRect;
        this.speed = speed;
        paint = new Paint();
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        rect.set(rect.left, (int)(rect.top + speed), rect.right, (int)(rect.bottom + speed));
    }
}
