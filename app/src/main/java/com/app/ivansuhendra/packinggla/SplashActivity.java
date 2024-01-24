package com.app.ivansuhendra.packinggla;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends AppCompatActivity {
    private ImageView ivLogo;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.iv_logo);
        tvName = findViewById(R.id.tv_name);

        // Set initial properties for ivLogo and tvName
        ivLogo.setAlpha(0f);
        tvName.setAlpha(0f);

        // Create ObjectAnimator for ivLogo
        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        logoAnimator.setDuration(1000); // Set the duration in milliseconds

        // Create ObjectAnimator for tvName
        ObjectAnimator nameAnimator = ObjectAnimator.ofFloat(tvName, "alpha", 0f, 1f);
        nameAnimator.setDuration(1000); // Set the duration in milliseconds

        // Create an AnimatorSet to play animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(logoAnimator, nameAnimator);

        // Start the animation when the activity is created
        animatorSet.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                } finally {
                    if (Build.VERSION.SDK_INT > 22) {
                        Dexter.withActivity(SplashActivity.this)
                                .withPermissions(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.READ_SMS,
                                        Manifest.permission.RECEIVE_SMS,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION

                                ).withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        next();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();

                    } else {
                        next();
                    }
                }
            }
        }.start();
    }

    private void next() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}