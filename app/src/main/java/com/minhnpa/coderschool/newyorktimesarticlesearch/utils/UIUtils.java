package com.minhnpa.coderschool.newyorktimesarticlesearch.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by MINH NPA on 22 Oct 2016.
 */

public class UIUtils {
    public static int covertPixelToDp(int pixel, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return pixel * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
