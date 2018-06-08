package com.project.anonymousshrink.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asadmehran on 15/02/2018.
 */

public class PreferenceManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AnonymousShrink";
    private static final String IS_PROFILE_CREATE = "ProfileCreated";



    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setProfileCreated(boolean isFirstTime) {
        editor.putBoolean(IS_PROFILE_CREATE, isFirstTime);
        editor.commit();
    }

    public boolean isProfileCreated() {
        return pref.getBoolean(IS_PROFILE_CREATE,false);
    }


}
