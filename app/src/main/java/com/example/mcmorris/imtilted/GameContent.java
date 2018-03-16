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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Josh on 2/13/2018.
 * View class for the whole game
 * TODO: Implement more of the game, make more modularized
 */

public class GameContent extends SurfaceView implements SurfaceHolder.Callback {
    public MainThread thread;
    public Player player;
    public TiltManager tiltManager;
    public PathObject path;

    private double pathDisplacement;
    private int pathPeriod;
    private int pathDetail;
    private int pathWidth;
    private int pathSineOffset;
    private Paint pathPaint;

    private MainActivityListener listener;


    private int score = 0;
    private boolean playing = false;


    public GameContent(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        //thread = new MainThread(getHolder(), this);
        setFocusable(true);

        this.listener = null;

        tiltManager = new TiltManager(context);
        //tiltManager.start();

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
        Log.d(Constants.Tag, "onCreate called" );
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
        if(path.playerCollide(player)) {
            player.resetPos();
            Log.d(Constants.Tag, "Player dead in update");
            if(listener != null) {
                //Communicates to the MainActivity that the player has died
                listener.onPlayerDead(true);
                tiltManager.stop();
            }
        }
        if (player.getAlive() && playing)
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

    public int getScore() {
        return score;
    }


    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    //Utilizing much of https://guides.codepath.com/android/Creating-Custom-Listeners
    //This interface defines the type of messages I want to communicate to my owner
    public interface MainActivityListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onPlayerDead(Boolean dead);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setMainActivityListener(MainActivityListener listener) {
        this.listener = listener;
    }
}
