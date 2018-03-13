package com.example.mcmorris.imtilted;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mplayB;
    private TextView mhighScoreTV;
    private TextView mhighScoreDisplayTV;
    private int highScore;


    private GameContent gameContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main );

        gameContent = findViewById(R.id.GameContent);


        mplayB = findViewById(R.id.playB);
        mhighScoreTV = findViewById(R.id.highScoreTV);
        mhighScoreDisplayTV = findViewById(R.id.highScoreDisplayTV);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        highScore = sharedPref.getInt(Constants.highScore, highScore);
        mhighScoreDisplayTV.setText("" + highScore);

        mplayB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplayB.setVisibility(View.GONE);
                mhighScoreTV.setVisibility(View.GONE);
                mhighScoreDisplayTV.setVisibility(View.GONE);

                //gameContent.setVisibility(View.VISIBLE);
                gameContent.tiltManager.start();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gameContent.tiltManager.stop();
        highScore = (highScore > gameContent.getScore() ? highScore : gameContent.getScore());
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.highScore, highScore);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameContent.player.reset();

        mplayB.setVisibility(View.VISIBLE);
        mhighScoreTV.setVisibility(View.VISIBLE);
        mhighScoreDisplayTV.setVisibility(View.VISIBLE);
        gameContent.setVisibility(View.VISIBLE);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        highScore = sharedPref.getInt(Constants.highScore, highScore);
        mhighScoreDisplayTV.setText("" + highScore);

    }


}
