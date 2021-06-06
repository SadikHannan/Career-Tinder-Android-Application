package com.softwaregiants.careertinder.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 2017-07-09.
 */

public class PreferenceManager {

    //region Global Variables
    private static PreferenceManager mPreferenceManager;
    private SharedPreferences mSharedPreferences;
    private static final String PREFS_NAME = "com.softwaregiants.careertinder";
    //endregion

    /*
     * Constructor
     * Create an object of PreferenceManager (this class) to manage AppPreferences
     */
    PreferenceManager(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /*
     * Function
     * @desc - Returns an instance of this class to allow method calls
     * @param Context - required to create PreferenceManager object
     * @return PreferenceManager
     */
    public static PreferenceManager getInstance(Context mContext) {
        mPreferenceManager = null;
        mPreferenceManager = new PreferenceManager(mContext);
        return mPreferenceManager;
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public double getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    public void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
    }

    public void remove(String key) {
        mSharedPreferences.edit().remove(key).commit();
    }

    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }

}
