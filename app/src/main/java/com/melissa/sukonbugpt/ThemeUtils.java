package com.melissa.sukonbugpt;

import android.content.res.Configuration;

public class ThemeUtils {

    public static boolean isDarkModeEnabled(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}