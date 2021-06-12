package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        image = (ImageView)findViewById(R.id.logo);

        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        image.startAnimation(hyperspaceJump);
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),IntroActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
