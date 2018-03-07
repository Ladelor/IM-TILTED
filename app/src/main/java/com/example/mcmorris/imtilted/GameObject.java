package com.example.mcmorris.imtilted;

import android.graphics.Canvas;

/**
 * Created by Josh on 2/27/2018.
 * Base interface for any drawable in game
 * TODO:
 */

public interface GameObject {
    void draw(Canvas canvas);
    void update();
}