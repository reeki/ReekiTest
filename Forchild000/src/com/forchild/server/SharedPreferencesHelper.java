package com.forchild.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
	protected Context context;

	/**
	 * constructor method
	 * 
	 * @param the
	 *            context which uses this class
	 * */
	public SharedPreferencesHelper(Context context) {
		this.context = context;

	}

	/**
	 * Put the String variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param value
	 *            the value which will be stored
	 * */
	public void putString(String title, String value) {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(title, value);
		editor.commit();
	}

	/**
	 * Put the Integer variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param value
	 *            the value which will be stored
	 * */
	public void putInt(String title, int value) {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(title, value);
		editor.commit();
	}

	/**
	 * Put the Boolean variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param value
	 *            the value which will be stored
	 * */
	public boolean putBoolean(String title, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(title, value);
		return editor.commit();
	}
	
	/**
	 * Put the Float variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param value
	 *            the value which will be stored
	 * */
	public void putFloat(String title, float value) {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putFloat(title, value);
		editor.commit();
	}

	/**
	 * Get the String variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param defaultValue
	 *            the return value if there is not matched result.
	 * @return the value which is required, or else return defaultValue if there
	 *         is not matched result.
	 * */
	public String getString(String title, String defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"forchild", Context.MODE_PRIVATE);
		return preferences.getString(title, defaultValue);
	}

	/**
	 * Get the integer variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param defaultValue
	 *            the return value if there is not matched result.
	 * @return the value which is required, or else return defaultValue if there
	 *         is not matched result.
	 * */
	public int getInt(String title, int defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"forchild", Context.MODE_PRIVATE);
		return preferences.getInt(title, defaultValue);
	}

	/**
	 * Get the boolean variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param defaultValue
	 *            the return value if there is not matched result.
	 * @return the value which is required, or else return defaultValue if there
	 *         is not matched result.
	 * */
	public boolean getBoolean(String title, boolean defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"forchild", Context.MODE_PRIVATE);
		return preferences.getBoolean(title, defaultValue);
	}
	
	/**
	 * Get the boolean variable to the SharedPreferences
	 * 
	 * @param title
	 *            the key of the value
	 * @param defaultValue
	 *            the return value if there is not matched result.
	 * @return the value which is required, or else return defaultValue if there
	 *         is not matched result.
	 * */
	public float getFloat(String title, float defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"forchild", Context.MODE_PRIVATE);
		return preferences.getFloat(title, defaultValue);
	}
	
	public boolean putLong(String key, long value) {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		return editor.commit();
	}
	
	public long getLong(String title, long defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				"forchild", Context.MODE_PRIVATE);
		return preferences.getLong(title, defaultValue);
	}
	
	public boolean clear() {
		SharedPreferences preferences = context.getSharedPreferences("forchild",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		return editor.commit();
	}
	
}
