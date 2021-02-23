package com.example.customviewimple;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jackandphantom.circularimageview.CircleImage;
import com.jackandphantom.circularimageview.RoundedImage;


/**
 * Created by willy on 16/12/12.
 */

public class WaveActivity extends AppCompatActivity {

    private CircleImage imageView;
    private WaveTest waveView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
       imageView =  findViewById(R.id.image);
        waveView3 =  findViewById(R.id.wave_view);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(70,70);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER;

        waveView3.setOnWaveAnimationListener(new WaveTest.OnWaveAnimationListener() {
            @Override
            public void onWaveAnimate(float v) {
                lp.width = 170;
                lp.height = 170;
                lp.setMargins(0,0,0,(int)v+30);
                imageView.setLayoutParams(lp);
            }
        });
    }
}
