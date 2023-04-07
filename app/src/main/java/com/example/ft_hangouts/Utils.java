package com.example.ft_hangouts;

import android.app.Activity;
import android.content.Intent;

public class Utils {
    private static int sTheme;

    public final static int THEME_BASIC = 0;
    public final static int THEME_ORANGE = 1;

    public static void changeToTheme(Activity activity) {
        if (sTheme == THEME_BASIC) {
            sTheme = THEME_ORANGE;
        }
        else {
            sTheme = THEME_BASIC;
        }
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_BASIC:
                sTheme = THEME_BASIC;
                activity.setTheme(R.style.Theme_Basic);
                break;
            case THEME_ORANGE:
                sTheme = THEME_ORANGE;
                activity.setTheme(R.style.Theme_Orange);
                break;
        }
    }
}
