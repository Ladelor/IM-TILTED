package com.example.mcmorris.imtilted;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by Josh on 2/13/2018.
 * Class to override base Java Thread
 * TODO: Handle frame rate stuff
 */

public class MainThread extends Thread {
    public static final int FPS = 30;
    private final SurfaceHolder surfaceHolder;
    private GameContent gameContent;
    private boolean running;
    private static Canvas canvas;

    // This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.



    MainThread(SurfaceHolder surfaceHolder, GameContent gameContent) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameContent = gameContent;
        // set null or default listener or accept as argument to constructor

    }

    void setRunning(boolean running) {
        this.running = running;
    }

    //TODO: This is where frame rate controls could be added
    @Override
    public void run() {
        //long startTime;
        //This is the game loop that repeats until the game is closed
        while(running) {
            //startTime = System.nanoTime();
            try {
                canvas = this.surfaceHolder.lockCanvas();
                //This is where update and draw are called for GameContent
                synchronized (surfaceHolder) {
                    this.gameContent.update();
                    this.gameContent.draw(canvas);
                }
            //Hope this never happens
            } catch(Exception e) { e.printStackTrace(); }
            finally {
                if(canvas != null)
                {
                      try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}