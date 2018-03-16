package com.example.mcmorris.imtilted;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Josh on 3/6/2018.
 * Class with useful info and methods things throughout the app may need
 * TODO: Add anything else worth adding
 */

class Constants {
    static int screenWidth;
    static int screenHeight;
    static DisplayMetrics displayMetrics;
    static Resources resources;
    static String highScore = "hS";
    static String Tag = "IM_TILTED";

    static int convertPxToDp(int pixels) {
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, pixels, displayMetrics );
    }

    static boolean collidePointRect(Point point, Rect rect) {
        if (rect.bottom >= point.y && rect.top <= point.y)
            if (rect.left <= point.x && rect.right >= point.x)
                return true;
        return false;
    }

    static boolean collidePointCircle(Point point, Point circlePoint, float radius) {
        if (Math.sqrt(Math.pow(point.x - circlePoint.x, 2) + Math.pow(point.y - circlePoint.y, 2)) < radius)
            return true;
        return false;
    }
}
