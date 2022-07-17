package com.shaily.crickSimple.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class SharedPreferencesHandler {

	public static String KEY_NAME = "KEY_NAME";
	public static String KEY_OVER = "KEY_OVER";
	public static String KEY_MODE = "KEY_MODE";
	public static String KEY_CHALLENGE = "KEY_CHALLENGE";
	public static String KEY_URI = "KEY_URI";
	public static String KEY_CUSTOM_URI = "KEY_CUSTOM_URI";
	public static String KEY_ZERO_URI = "KEY_ZERO_URI";
	public static String KEY_ONE_URI = "KEY_ONE_URI";
	public static String KEY_TWO_URI = "KEY_TWO_URI";
	public static String KEY_FOUR_URI = "KEY_FOUR_URI";
	public static String KEY_SIX_URI = "KEY_SIX_URI";
	public static String KEY_OUT_URI = "KEY_OUT_URI";
	public static String KEY_WIDE_URI = "KEY_WIDE_URI";


	public enum Modes{
		single,
		Challenge,
		Team
	}
	public static SharedPreferences getSharedPreferences(Context ctx) {
		return PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	public static void setStringValues(Context ctx, String key,
			String DataToSave) {
		Editor editor = getSharedPreferences(ctx).edit();
		editor.putString(key, DataToSave);
		editor.commit();
	}

	public static String getStringValues(Context ctx, String key) {
		return getSharedPreferences(ctx).getString(key, null);
	}

	public static void setIntValues(Context ctx, String key, int DataToSave) {
		Editor editor = getSharedPreferences(ctx).edit();
		editor.putInt(key, DataToSave);
		editor.commit();
	}

	public static int getIntValues(Context ctx, String key) {
		return getSharedPreferences(ctx).getInt(key, 0);
	}
	
	public static void setBooleanValues(Context ctx, String key, Boolean DataToSave) {
		Editor editor = getSharedPreferences(ctx).edit();
		editor.putBoolean(key, DataToSave);
		editor.commit();
	}

	public static boolean getBooleanValues(Context ctx, String key) {
		return getSharedPreferences(ctx).getBoolean(key, false);
	}
	
	public static long getLongValues(Context ctx, String key) {
		return getSharedPreferences(ctx).getLong(key, 0L);
	}
	
	public static void setLongValues(Context ctx, String key, Long DataToSave) {
		Editor editor = getSharedPreferences(ctx).edit();
		editor.putLong(key, DataToSave);
		editor.commit();
	}

	public static void setFloatValues(Context ctx, String key, Float DataToSave) {
		Editor editor = getSharedPreferences(ctx).edit();
		editor.putFloat(key, DataToSave);
		editor.commit();
	}

	public static float getFloatValues(Context ctx, String key) {
		return getSharedPreferences(ctx).getFloat(key, 0f);
	}
}