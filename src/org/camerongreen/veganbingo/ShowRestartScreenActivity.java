package org.camerongreen.veganbingo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ShowRestartScreenActivity extends Activity {
	private MyPrefs prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_restart_screen);
		setupActionBar();

		prefs = new MyPrefs(getSharedPreferences(MainActivity.PACKAGE_NAME,
				Context.MODE_PRIVATE));
		int count = prefs.countPrefs(MainActivity.choices);
		Button button = (Button) findViewById(R.id.restart_button);
		setRestartButtonDisplay(count > 0, button);
	}
	
	private void setRestartButtonDisplay(boolean enabled, Button button) {
		int icon = 0;
		icon = android.R.drawable.ic_delete;
		button.setEnabled(enabled);
		Drawable image = getResources().getDrawable(icon);
		image.setBounds(0, 0, 60, 60);
		button.setCompoundDrawables(image, null, null, null);
	}


	public void restart(View view) {
		prefs.clearPrefs(MainActivity.choices);
		Button button = (Button) view;
		setRestartButtonDisplay(false, button);
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
