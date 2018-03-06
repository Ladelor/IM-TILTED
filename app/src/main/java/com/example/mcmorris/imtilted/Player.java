package com.example.mcmorrisgray.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Josh on 2/27/2018.
 */

public class Player implements GameObject {

    public Point getPlayerPoint() {
        return playerPoint;
    }

    private Point playerPoint;
    private Paint playerPaint;
    private TiltManager tiltManager;
    private long frameTime;

    public Player(Point playerStart, int playerColor, TiltManager tiltManager) {
        this.playerPoint = playerStart;
        this.tiltManager = tiltManager;
        playerPaint = new Paint();
        playerPaint.setColor(playerColor);
    }

    //THIS NEEDS TO BE CALLED ON RESUME OR RIPPP PLAYER
    public void resetTime() {
        frameTime = -1;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(new Rect(playerPoint.x - 50, playerPoint.y - 50, playerPoint.x + 50, playerPoint.y + 50), playerPaint);
    }

    @Override
    public void update() {
        if (frameTime == -1)
            frameTime = System.currentTimeMillis();
        else {
            long elapsedTime = System.currentTimeMillis() - frameTime;
            frameTime = System.currentTimeMillis();
            if (tiltManager.getTilt() != null && tiltManager.getInitialTilt() != null) {
                //float roll = tiltManager.getTilt()[2] - tiltManager.getInitialTilt()[2];
                float roll = tiltManager.getTilt()[2];

                float speed = roll * 2f;
                playerPoint.x += speed * elapsedTime;
                if(playerPoint.x < 0)
                    playerPoint.x = 0;
                if(playerPoint.x > 900)
                    playerPoint.x = 900;
            }
        }
    }


}
