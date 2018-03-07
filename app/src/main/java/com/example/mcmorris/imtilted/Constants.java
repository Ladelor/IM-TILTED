package com.example.mcmorris.imtilted;

import android.content.res.Resources;
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

    static int convertPxToDp(int pixels) {
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, pixels, displayMetrics );
    }
}
