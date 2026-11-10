package com.example.socialmediacontroller;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Process;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

final class UsageTracker {
    static final long LIMIT_MS = 5 * 60 * 1000L;

    static boolean hasUsageAccess(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    static long currentHourStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    static long selectedUsageMillis(Context context, Set<String> targets) {
        if (targets == null || targets.isEmpty() || !hasUsageAccess(context)) return 0;
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, currentHourStart(), System.currentTimeMillis());
        long total = 0;
        if (stats != null) {
            for (UsageStats item : stats) {
                if (targets.contains(item.getPackageName())) total += item.getTotalTimeInForeground();
            }
        }
        return total;
    }

    static String formatDuration(long millis) {
        long minutes = millis / 60_000;
        long seconds = (millis / 1_000) % 60;
        return minutes + " min " + seconds + " sec";
    }

    private UsageTracker() { }
}
