package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Daniel on 3/6/2018.
 * Defines Path data and behavior
 */

public class PathObject implements GameObject {

    private int pathColor;

    //Determines the horizontal displacement of the sine wave
    private double pathDisplacement;

    //Determines period of sine wave.
    private int pathPeriod;

    //Determines pixelation amount of the path ** Don't be 0
    private int pathDetail;

    //Determines width of rectangles to be used in path
    //Good default = 1/5 of screen width
    private int pathWidth;

    //Determines how big sine wave gets by multiplying it by the tiny sine wave
    //Good default is screen width / 20
    private int pathSineOffset;

    //Determines speed at which path moves
    private float pathSpeed = 10f;

    private ArrayList<PathRect> pathRects = new ArrayList<>();

    private int centerScreen = Constants.screenWidth / 2;

    private int yPosition;

    private int colorSwitch = -1;

    private Random random = new Random();

    private ArrayList<Obstacle> pathObstacles = new ArrayList<>();

    private int obstacleSize;
    private int obstacleFrequency;

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    private boolean playing = false;

    PathObject(double pathDisplacement, int pathPeriod, int pathDetail, int pathWidth, int pathColor, int pathSineOffset) {
        //TODO input validation?
        this.pathDisplacement = pathDisplacement;
        this.pathPeriod = pathPeriod;
        this.pathDetail = pathDetail;
        this.pathWidth = pathWidth;
        this.pathColor = pathColor;
        this.pathSineOffset = pathSineOffset;

        for(yPosition = Constants.screenHeight; yPosition >= 0; yPosition -= pathDetail)
        {
            int pathCenter = centerScreen + (int)(this.pathSineOffset * Math.sin(((double) yPosition / this.pathPeriod)) + this.pathDisplacement);
            pathRects.add(new PathRect(new RectF(pathCenter - this.pathWidth, yPosition, pathCenter + this.pathWidth,
                    yPosition + this.pathDetail), this.pathColor));
        }

        resetObstacles();
    }

    @Override
    public void draw(Canvas canvas) {
        for (PathRect pathRect : pathRects)
            pathRect.draw(canvas);
        for(Obstacle obstacle : pathObstacles)
            obstacle.draw(canvas);
    }

    @Override
    public void update () {
        float offset = pathSpeed;
        pathColor += 1 * colorSwitch;
        if (pathColor % 256  == 0) {
            colorSwitch *= -1;
            obstacleFrequency /= 1.1;
            //obstacleSize += 2;
            if (colorSwitch == -1)
                pathColor -= 2;
            else
                pathColor += 2;
        }

        for (int i = 0; i < pathRects.size(); i++) {
            pathRects.get(i).update(pathSpeed);

            if(pathRects.get(i).pathRect.top > Constants.screenHeight) {
                float pathCenter = centerScreen + (float)(pathSineOffset * Math.sin(((double) yPosition / pathPeriod)) + pathDisplacement);
                pathRects.set(i, new PathRect(new RectF((float)(pathCenter - pathWidth), (float)offset, (float)(pathCenter
                        + pathWidth), (float)(offset + pathDetail)), pathColor));

                if (playing) {
                    if (random.nextInt(obstacleFrequency) % obstacleFrequency == 1) {
                        int obstaclePos = random.nextInt((2 * pathWidth) - obstacleSize);
                        pathObstacles.add(new Obstacle(new Rect((int) pathCenter - pathWidth + obstaclePos, 0 - obstacleSize,
                                (int) (pathCenter + obstaclePos - pathWidth + obstacleSize), 0), 0xffff0099, pathSpeed));
                    }
                }

                yPosition -= pathDetail;
                offset -= pathDetail;
            }

        }

        for(Obstacle obstacle : pathObstacles)
            obstacle.update();
    }

    void resetObstacles() {
        pathObstacles.clear();
        obstacleSize = 60;
        obstacleFrequency = 300;
    }

    Boolean playerCollide(Player player){
        //Slight optimization, find rect player is on vertically, then check horizontal
        for (PathRect pathRect : pathRects)
            if (pathRect.pathRect.bottom >= player.getPlayerPoint().y && pathRect.pathRect.top <= player.getPlayerPoint().y)
                if (pathRect.pathRect.left >= player.getPlayerPoint().x || pathRect.pathRect.right <= player.getPlayerPoint().x)
                    return playerDead(player);

        for (Obstacle obstacle : pathObstacles) {
            //Bottom-left of obstacle to player
            if ( Constants.collidePointCircle(new Point(obstacle.rect.left, obstacle.rect.bottom), player.getPlayerPoint(), player.radius))
                return playerDead(player);

            //Bottom-right of obstacle to player
            if ( Constants.collidePointCircle(new Point(obstacle.rect.right, obstacle.rect.bottom), player.getPlayerPoint(), player.radius))
                return playerDead(player);

            //Not checking top corners because its so unlikely for player to collide there

            //Left of player to Obstacle
            if ( Constants.collidePointRect(new Point((int)(player.getPlayerPoint().x - player.radius), player.getPlayerPoint().y), obstacle.rect))
                return playerDead(player);

            //Right of player to Obstacle
            if ( Constants.collidePointRect(new Point((int)(player.getPlayerPoint().x + player.radius), player.getPlayerPoint().y), obstacle.rect))
                return playerDead(player);
        }
        return false;
    }

    boolean playerDead(Player player) {
        player.setAlive(false);
        Log.d(Constants.Tag, "ded");
        resetObstacles();
        return true;
    }
}
