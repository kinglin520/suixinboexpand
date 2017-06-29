package com.sharewisesc.inbroadheart.ui.startpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sharewisesc.inbroadheart.R;
import com.sharewisesc.inbroadheart.ui.main.MainActivity;

import java.lang.ref.SoftReference;

/**
 * wenlin
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        if (savedInstanceState == null) {
            new Handler().postDelayed(new HandlerLogin(this), 1000);
        }
    }

    private static class HandlerLogin implements Runnable {
        private final SoftReference<SplashActivity> sfActivity;

        public HandlerLogin(SplashActivity loginActivity) {
            this.sfActivity = new SoftReference<>(loginActivity);
        }

        @Override
        public void run() {
            if (sfActivity.get() != null) {
                SplashActivity activity = sfActivity.get();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
}
