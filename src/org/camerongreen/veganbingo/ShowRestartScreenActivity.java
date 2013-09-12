package org.camerongreen.veganbingo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Allow the user to restart, which effectly means just removing all of their
 * preferences
 * 
 * @author Cameron Green <i@camerongreen.org>
 */
public class ShowRestartScreenActivity extends BingoActivityAbstract {
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
		
		convertTextToHtml(R.id.restart_text);
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



}
