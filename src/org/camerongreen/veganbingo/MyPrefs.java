package org.camerongreen.veganbingo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs extends Activity {

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

	public void putIntPref(String key, int value) {
		getEditor().putInt(makeKey(key), value);
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

	public int getIntPref(String key) {
		int pref_value = getSharedPrefs().getInt(makeKey(key), 0);
		return pref_value;
	}
	
	public int countPrefs(String[] choices) {
		int count = 0;
		for (int i = 0; i < choices.length; i++) {
			count += getIntPref(choices[i]);
		}
		return count;
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
