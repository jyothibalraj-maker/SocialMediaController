package com.example.socialmediacontroller;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {
    static final String PREFS = "family_controls";
    static final String TARGETS = "selected_packages";
    private final Set<String> selected = new HashSet<>();
    private LinearLayout root;
    private TextView status;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        selected.addAll(getSharedPreferences(PREFS, MODE_PRIVATE).getStringSet(TARGETS, new HashSet<>()));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);
        setContentView(root);
        render();
    }

    private void render() {
        root.removeAllViews();
        TextView title = new TextView(this);
        title.setText("Social Media Controller\n\nChoose apps to include in a shared 5-minute limit per clock hour.");
        title.setTextSize(20);
        root.addView(title);

        TextView usage = new TextView(this);
        long used = UsageTracker.selectedUsageMillis(this, selected);
        usage.setText("\nThis hour: " + UsageTracker.formatDuration(used) + " / 5 min\n");
        root.addView(usage);

        status = new TextView(this);
        status.setText("Usage Access: " + (UsageTracker.hasUsageAccess(this) ? "granted" : "not granted"));
        root.addView(status);
        Button usageButton = new Button(this);
        usageButton.setText("Open Usage Access settings");
        usageButton.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
        root.addView(usageButton);

        Button accessibilityButton = new Button(this);
        accessibilityButton.setText("Open Accessibility settings (enable this app)");
        accessibilityButton.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(accessibilityButton);

        TextView appsTitle = new TextView(this);
        appsTitle.setText("\nSelected apps (changes save immediately):");
        root.addView(appsTitle);
        PackageManager pm = getPackageManager();
        for (String packageName : SocialApps.KNOWN.keySet()) {
            try {
                ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
                String label = pm.getApplicationLabel(info).toString();
                CheckBox check = new CheckBox(this);
                check.setText(label);
                check.setChecked(selected.contains(packageName));
                check.setOnCheckedChangeListener((button, checked) -> {
                    if (checked) selected.add(packageName); else selected.remove(packageName);
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putStringSet(TARGETS, new HashSet<>(selected)).apply();
                    render();
                });
                root.addView(check, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } catch (PackageManager.NameNotFoundException ignored) { }
        }

        Button refresh = new Button(this);
        refresh.setText("Refresh usage");
        refresh.setOnClickListener(v -> render());
        root.addView(refresh);

        Button sms = new Button(this);
        sms.setText("Share this-hour report by SMS");
        sms.setOnClickListener(v -> {
            String report = "Social media usage this hour: " + UsageTracker.formatDuration(
                    UsageTracker.selectedUsageMillis(this, selected)) + " / 5 min.";
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(android.net.Uri.parse("smsto:"));
            intent.putExtra("sms_body", report);
            startActivity(intent);
        });
        root.addView(sms);
    }

    @Override protected void onResume() {
        super.onResume();
        if (root != null) render();
    }
}
