package net.golbarg.engtoper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    public static final String TAG = SplashScreenActivity.class.getName();
    SharedPreferences pref;

    boolean isActive = true;

    ProgressBar progressLoading;
    TextView txtStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        progressLoading = findViewById(R.id.progress_loading);
        txtStatus = findViewById(R.id.txt_status);
        txtStatus.setText("");

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {

                    showWaiting();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();

                } catch (Exception e) {
                    finish();
                    e.printStackTrace();
                }
            }

            private void showWaiting() {
                int waitTime = 0;

                while(waitTime <= 2) {
                    try{
                        sleep(1000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    waitTime += 1;
                }
            }
        };
        splashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            isActive = false;
        }
        return true;
    }

}
