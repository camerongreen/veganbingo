package org.camerongreen.veganbingo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowScreenActivity extends Activity {
	private MyPrefs prefs = null; // needs to work not only on oncreate
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_screen);
		// Show the Up button in the action bar.
		setupActionBar();
		
		prefs = new MyPrefs(getSharedPreferences(MainActivity.PACKAGE_NAME,
				Context.MODE_PRIVATE));

		Intent intent = getIntent();

		// which button was clicked?
		String button_clicked = intent
				.getStringExtra(MainActivity.BUTTON_CLICKED);
		setImageView(R.id.main_icon, button_clicked);

		// add the tag to the done button so we can set prefs
		// when they click it later
		Button doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setTag(button_clicked);
		// which icon should we show, done or not done
		int pref_value = prefs.getIntPref(button_clicked);
		setDoneButtonDisplay(pref_value, doneButton);
		
		String button_clicked_message = getString(button_clicked + "_description");
		setTextView(R.id.clickedButtonMessage, button_clicked_message);

		String mainTextVal = getString(button_clicked + "_main");
		setTextView(R.id.main_content, mainTextVal.replace("\\\n",
				System.getProperty("line.separator")));
		
	}

	private void setImageView(int viewId, String resourceName) {
		int imgId = getResources().getIdentifier("@drawable/" + resourceName,
				"id", getPackageName());
		ImageView imageView = (ImageView) findViewById(viewId);
		imageView.setImageResource(imgId);
	}

	private void setTextView(int id, String text) {
		TextView tv = (TextView) findViewById(id);
		tv.setText(text);
	}

	private String getString(String resourceName) {
		int stringId = getResources().getIdentifier(
				"@string/" + resourceName, "id", getPackageName());
		String message = getResources().getString(stringId);
		return message;
	}

	private void setDoneButtonDisplay(int pref_value, Button doneButton) {
		int icon = 0;
		if (pref_value != 0) {
			icon = android.R.drawable.btn_star_big_on;
			doneButton.setText(getResources().getString(
					R.string.done_button_uncheck));
		} else {
			icon = android.R.drawable.btn_star_big_off;
			doneButton.setText(getResources().getString(
					R.string.done_button_check));
		}
		Drawable image = getResources().getDrawable(icon);
		image.setBounds(0, 0, 60, 60);
		doneButton.setCompoundDrawables(image, null, null, null);
	}

	public void toggleDone(View view) {
		Button button_clicked = (Button) view;
		String tag = (String) button_clicked.getTag();
		int pref_value = prefs.getIntPref(tag);

		int count = prefs.countPrefs(MainActivity.choices);
		if (pref_value != 0) {
			prefs.remove(tag);
			setDoneButtonDisplay(0, button_clicked);
			// if there is only one option and we are removing it
			// here, remove the started timer
			if (count == 1) {
				prefs.remove("started");
			}
			// we can never be finished if we are removing one
			prefs.remove("finished");
		} else {
			// go...
			if (count == 0) {
				doStart();
			}
			prefs.putIntPref(tag, 1);
			setDoneButtonDisplay(1, button_clicked);
			if ((count + 1) == MainActivity.choices.length) {
				doComplete();
			}
		}
	}

	public void doStart() {
		prefs.putLongPref("started", System.currentTimeMillis());
	}

	public void doComplete() {
		prefs.putLongPref("Finished", System.currentTimeMillis());
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
