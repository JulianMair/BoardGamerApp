package de.iu.boardgame.feature_user.helpers;

import android.content.Context;

public class SessionManager {
    private static final String PREFS = "boardgame_prefs";
    private static final String KEY_USER_ID = "current_user_id";

    public static void setCurrentUserId(Context context, long userId){
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_USER_ID, userId)
                .apply();
    }

    public static long getCurrentUserId(Context context){
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getLong(KEY_USER_ID, -1L);
    }

    public static void clearCurrentUserId(Context context){
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_USER_ID)
                .apply();
    }
}
