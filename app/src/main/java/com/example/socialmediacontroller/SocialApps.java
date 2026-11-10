package com.example.socialmediacontroller;

import java.util.LinkedHashMap;
import java.util.Map;

final class SocialApps {
    static final Map<String, String> KNOWN = new LinkedHashMap<>();
    static {
        KNOWN.put("com.instagram.android", "Instagram");
        KNOWN.put("com.zhiliaoapp.musically", "TikTok");
        KNOWN.put("com.ss.android.ugc.trill", "TikTok (alternate)");
        KNOWN.put("com.facebook.katana", "Facebook");
        KNOWN.put("com.facebook.lite", "Facebook Lite");
        KNOWN.put("com.twitter.android", "X");
        KNOWN.put("com.snapchat.android", "Snapchat");
        KNOWN.put("com.reddit.frontpage", "Reddit");
        KNOWN.put("com.pinterest", "Pinterest");
        KNOWN.put("com.linkedin.android", "LinkedIn");
    }
    private SocialApps() { }
}
