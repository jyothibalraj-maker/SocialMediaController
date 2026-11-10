# Social Media Controller (Android / Java)

A small, parent-configured usage limiter. It adds up selected apps' foreground use during the current clock hour and, after five minutes, visibly blocks those apps until the next hour. Monitoring is not hidden: the parent must grant Usage Access and explicitly enable the app's Accessibility service in Android Settings. The service only watches selected package names.

## Build and setup

1. Open this repository in a recent Android Studio (Android Gradle Plugin 8.6.1 / Gradle 8.7 or newer).
2. Install/run the app on the child's device with the parent present.
3. Select the installed social apps to include.
4. Grant **Usage Access** and enable **Social Media Controller** under **Accessibility**. Both are system-managed, explicit user permissions. If either permission is off, enforcement does not work.

The limit is a combined five minutes across selected apps, per local clock hour. At the beginning of each new hour, usage resets. Android UsageStats can be delayed or approximate, so this is a best-effort family control, not a network firewall; it does not block background network traffic or guarantee that an app cannot be bypassed.

## Reports and SMS

The app displays current-hour use locally. It deliberately does not silently send SMS or read the device's phone number: Android does not reliably expose the line number, and unattended SMS access is restricted on many Android distributions/Play policies. To send a report, use Android's share/SMS flow and choose the recipient yourself. The app does not send notifications or hide its monitoring; the blocking screen is visible.

## Supported targets

The app offers an explicit allowlist of common social apps and only lists packages installed on the device. Add package IDs in `SocialApps.java` if needed. No internet access permission is requested, and the app does not intercept or modify network connectivity.
