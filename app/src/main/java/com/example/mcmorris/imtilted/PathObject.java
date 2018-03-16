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
import java.util.concurrent.CopyOnWriteArrayList;

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

    //The whole path is just a ton of rectangles
    //Makes it look good and easy to deal with
    private ArrayList<PathRect> pathRects = new ArrayList<>();

    private int centerScreen = Constants.screenWidth / 2;

    //This is garbage. To get the initial path loaded, start this at screenheight
    //Go down to 0 then go into negatives to get the sine to keep working
    private int yPosition;

    //Used to get color to go back and forth
    private int colorSwitch = -1;

    private Random random = new Random();

    //List with all the obstacles
    //This weird class helps it not throw errors when removing obstacles in the for loop
    private CopyOnWriteArrayList<Obstacle> pathObstacles = new CopyOnWriteArrayList<>();

    //Relatively arbitrary values to determine difficulty of the obstacles
    private int obstacleSize;
    private int obstacleFrequency;

    void setPlaying(boolean playing) {
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
            //Sine is very picky, getting it to look clean takes alot
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
        //The code changing the color
        //A bit rough in the end but its fun
        pathColor += colorSwitch;
        if (pathColor % 256  == 0) {
            colorSwitch *= -1;
            //Through in obstacle difficulty here because it works
            obstacleFrequency /= 1.1;
            //obstacleSize += 2;
            if (colorSwitch == -1)
                pathColor -= 2;
            else
                pathColor += 2;
        }

        //variable to deal with drawing multiple rects a frame
        float offset = pathSpeed;

        for (int i = 0; i < pathRects.size(); i++) {
            pathRects.get(i).update(pathSpeed);

            //If a pathRect is off the screen, create new ones
            if(pathRects.get(i).pathRect.top > Constants.screenHeight) {
                float pathCenter = centerScreen + (float)(pathSineOffset * Math.sin(((double) yPosition / pathPeriod)) + pathDisplacement);
                pathRects.set(i, new PathRect(new RectF(pathCenter - pathWidth, offset, pathCenter
                        + pathWidth, offset + pathDetail), pathColor));

                //Obstacle spawning
                //TODO: Better obstacle spanwing/manipulation
                if (playing) {
                    if (random.nextInt(obstacleFrequency) % obstacleFrequency == 1) {
                        int obstaclePos = random.nextInt((2 * pathWidth) - obstacleSize);
                        pathObstacles.add(new Obstacle(new Rect((int) pathCenter - pathWidth + obstaclePos, 0 - obstacleSize,
                                (int) (pathCenter + obstaclePos - pathWidth + obstacleSize), 0), 0xffff0099));
                    }
                }

                yPosition -= pathDetail;
                offset -= pathDetail;
            }
        }

        for(Obstacle obstacle : pathObstacles)
            obstacle.update(pathSpeed);
    }

    private void resetObstacles() {
        pathObstacles.clear();
        obstacleSize = 60;
        obstacleFrequency = 200;
    }

    Boolean playerCollide(Player player){
        //Slight optimization, find rect player is on vertically, then check horizontal
        for (PathRect pathRect : pathRects)
            if (pathRect.pathRect.bottom >= player.getPlayerPoint().y && pathRect.pathRect.top <= player.getPlayerPoint().y)
                if (pathRect.pathRect.left >= player.getPlayerPoint().x || pathRect.pathRect.right <= player.getPlayerPoint().x)
                    return playerDead(player);

        for (Obstacle obstacle : pathObstacles) {

            //Quick optimization for if the bottom of the obstacle is higher than the top of the player
            if (obstacle.rect.bottom < player.getPlayerPoint().y - player.radius)
                continue;
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

            //Delete the obstacle if its offscreen
            if ( obstacle.rect.top > Constants.screenHeight)
                pathObstacles.remove(obstacle);
        }
        //There was no collision
        return false;
    }

    private boolean playerDead(Player player) {
        player.setAlive(false);
        Log.d(Constants.Tag, "ded");
        resetObstacles();
        return true;
    }
}