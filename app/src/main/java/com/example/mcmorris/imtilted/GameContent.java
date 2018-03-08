package com.example.mcmorris.imtilted;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * Created by Josh on 2/13/2018.
 * View class for the whole game
 * TODO: Implement more of the game, make more modularized
 */

public class GameContent extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    public Player player;
    public TiltManager tiltManager;
    public PathObject path;

    private double pathDisplacement;
    private int pathPeriod;
    private int pathDetail;
    private int pathWidth;
    private int pathSineOffset;
    private Paint pathPaint;

    private int score = 0;


    public GameContent(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        //thread = new MainThread(getHolder(), this);
        setFocusable(true);

        tiltManager = new TiltManager(context);
        tiltManager.start();

        Constants.resources = getResources();

        Constants.displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(Constants.displayMetrics);
        Constants.screenHeight = Constants.displayMetrics.heightPixels;
        Constants.screenWidth = Constants.displayMetrics.widthPixels;
        player = new Player(new Point((Constants.screenWidth / 2), Constants.screenHeight - Constants.convertPxToDp(150)), tiltManager);

        //Initialize path data
        pathDisplacement = 0;
        pathPeriod = 150;       //TODO make this depend on screen pixel count
        pathDetail = 1;
        pathWidth = (int) (Constants.screenWidth / 3.5);
        pathSineOffset = (int) (Constants.screenWidth / 10.0);

        //Initialize a new Paint instance to draw the path
        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setAntiAlias(true);

        path = new PathObject(pathDisplacement, pathPeriod, pathDetail, pathWidth, 0xff00ccff, pathSineOffset);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
        Canvas c = getHolder().lockCanvas();
        draw(c);
        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean stillOpen = true;
        while(stillOpen) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            stillOpen = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void update() {
        player.update();
        path.update();
        path.playerCollide(player);
        score += 1;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xff999999);

        path.draw(canvas);
        player.draw(canvas);
        Paint textPaint = new Paint();
        textPaint.setColor(0xffff0000);
        textPaint.setTextSize(70f);
        canvas.drawText("Score: " + score, 10, 70, textPaint);
    }
}
