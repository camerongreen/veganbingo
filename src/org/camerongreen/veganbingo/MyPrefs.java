package org.camerongreen.veganbingo;

import android.content.SharedPreferences;

public class MyPrefs {

	private SharedPreferences sharedPref = null;
	private SharedPreferences.Editor editor = null;
	
	public MyPrefs(SharedPreferences param) {
			sharedPref = param;
	}
	
	public void putLongPref(String key, long value) {
		getEditor().putLong(makeKey(key), value);
		getEditor().commit();
	}

	private String makeKey(String key) {
		String pkg = MainActivity.PACKAGE_NAME;
		
		return pkg + "." + key;
	}

	public void putBooleanPref(String key, boolean value) {
		getEditor().putBoolean(makeKey(key), value);
		getEditor().commit();
	}

	public void remove(String key) {
		getEditor().remove(makeKey(key));
		getEditor().commit();
	}

	public long getLongPref(String key) {
		long pref_value = getSharedPrefs().getLong(makeKey(key), 0l);
		return pref_value;
	}

	public boolean getBooleanPref(String key) {
		boolean pref_value = getSharedPrefs().getBoolean(makeKey(key), false);
		return pref_value;
	}
	
	public int countPrefs(String[] choices) {
		int count = 0;
		for (int i = 0; i < choices.length; i++) {
			count += getBooleanPref(choices[i]) ? 1 : 0;
		}
		return count;
	}

	public void clearPrefs(String[] choices) {
		for (int i = 0; i < choices.length; i++) {
			remove(choices[i]);
		}
		remove("started");
		remove("finished");
	}

	private SharedPreferences.Editor getEditor() {
		if (editor == null) {
			editor = getSharedPrefs().edit();
		}
		return editor;
	}

	private SharedPreferences getSharedPrefs() {
		return sharedPref;
	}

}
