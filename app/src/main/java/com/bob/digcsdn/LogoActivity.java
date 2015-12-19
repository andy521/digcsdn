package com.bob.digcsdn;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import android.os.Handler;

public class LogoActivity extends Activity {
    private ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        img_logo = (ImageView) findViewById(R.id.img_logo);

        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(img_logo, "alpha", 0f, 1f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(img_logo, "rotation", 0f, 480f);//透明度渐变动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(img_logo, "scaleX", 0,1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(img_logo, "scaleY", 0,1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(fadeInOut).with(scaleX).with(scaleY);
        animatorSet.setDuration(2000);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
                        finish();
                    }
                }, 300);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
