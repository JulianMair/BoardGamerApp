package de.iu.boardgame.logging;

import android.util.Log;

public final class AppLog {

    private static final String PREFIX = "BGA";

    private AppLog() {}

    public static void d(String tag, String msg) { Log.d(PREFIX + "-" + tag, msg); }
    public static void i(String tag, String msg) { Log.i(PREFIX + "-" + tag, msg); }
    public static void w(String tag, String msg) { Log.w(PREFIX + "-" + tag, msg); }

    public static void e(String tag, String msg, Throwable t) {
        Log.e(PREFIX + "-" + tag, msg, t);
    }
}
