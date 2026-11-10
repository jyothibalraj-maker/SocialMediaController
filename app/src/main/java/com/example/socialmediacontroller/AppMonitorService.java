package com.example.socialmediacontroller;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.content.Intent;
import java.util.Set;

public class AppMonitorService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || event.getPackageName() == null) return;
        Set<String> targets = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE)
                .getStringSet(MainActivity.TARGETS, null);
        if (targets == null || !targets.contains(event.getPackageName().toString())) return;
        long used = UsageTracker.selectedUsageMillis(this, targets);
        if (used >= UsageTracker.LIMIT_MS) {
            Intent block = new Intent(this, LockoutActivity.class);
            block.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(block);
        }
    }

    @Override public void onInterrupt() { }
}
