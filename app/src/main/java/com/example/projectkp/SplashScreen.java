package com.example.projectkp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.loginregister.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    public static int SPLASH_SCREEN = 3000;
    Animation first_anim, second_anim;
    ImageView main_logo;
    TextView main_title, second_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        first_anim = AnimationUtils.loadAnimation(this, R.anim.first_animation);
        second_anim = AnimationUtils.loadAnimation(this, R.anim.second_animation);

        main_logo = findViewById(R.id.main_logo);
        main_title = findViewById(R.id.main_title);
        second_title = findViewById(R.id.second_title);

        main_logo.setAnimation(first_anim);
        main_title.setAnimation(second_anim);
        second_title.setAnimation(second_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashScreen.this, LoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(main_logo, "main_logo");
                pairs[1] = new Pair<View, String>(main_title, "main_title");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                startActivity(loginIntent, options.toBundle());
                finish();
            }
        }, SPLASH_SCREEN);
    }
}