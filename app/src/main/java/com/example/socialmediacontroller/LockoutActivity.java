package com.example.socialmediacontroller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class LockoutActivity extends Activity {
    private final Handler handler = new Handler();
    private TextView message;
    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            long nextHour = UsageTracker.currentHourStart() + 60 * 60 * 1000L;
            long remaining = Math.max(0, nextHour - System.currentTimeMillis());
            message.setText("Social app time limit reached. Selected social apps are paused until the next hour.\n\nTime remaining: "
                    + (remaining / 60_000) + " min " + ((remaining / 1_000) % 60) + " sec\n\nYou can return to the home screen or use another app.");
            if (remaining > 0) handler.postDelayed(this, 1000);
            else finish();
        }
    };

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        message = new TextView(this);
        message.setTextSize(22);
        message.setPadding(32, 48, 32, 32);
        setContentView(message);
        ticker.run();
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(ticker);
        super.onDestroy();
    }
}
