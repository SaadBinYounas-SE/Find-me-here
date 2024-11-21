package com.example.findmehere;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    View anim_L, anim_N, anim_underline1, anim_underline2;
    Animation animation_vertical, animation_horizontal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        init();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    public void init()
    {
        anim_L=findViewById(R.id.anim_L);
        anim_N=findViewById(R.id.anim_N);
        anim_underline1=findViewById(R.id.anim_underline1);
        anim_underline2=findViewById(R.id.anim_underline2);

        animation_vertical= AnimationUtils.loadAnimation(this,R.anim.vertical);
        animation_horizontal=AnimationUtils.loadAnimation(this,R.anim.horizontal);

        anim_L.startAnimation(animation_vertical);
        anim_N.startAnimation(animation_vertical);
        anim_underline1.startAnimation(animation_horizontal);
        anim_underline2.startAnimation(animation_horizontal);
    }
}

