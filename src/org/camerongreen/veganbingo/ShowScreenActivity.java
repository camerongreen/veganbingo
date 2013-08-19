package org.camerongreen.veganbingo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private SharedPreferences sharedPref = null;
	
	private SharedPreferences getSharedPrefs() {
		if (sharedPref == null) {
			sharedPref = this.getSharedPreferences(getPackageName(),
					Context.MODE_PRIVATE);
		}
		return sharedPref;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_screen);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();

		String button_clicked = intent
				.getStringExtra(MainActivity.BUTTON_CLICKED);
		String button_clicked_message = intent
				.getStringExtra(MainActivity.BUTTON_CLICKED_MESSAGE);

		int imgId = getResources().getIdentifier("@drawable/" + button_clicked,
				"id", getPackageName());
		ImageView imageView = (ImageView) findViewById(R.id.main_icon);
		imageView.setImageResource(imgId);

		int stringId = getResources().getIdentifier(
				"@string/" + button_clicked + "_main", "id", getPackageName());
		String mainTextVal = getResources().getString(stringId);
		TextView mainText = (TextView) findViewById(R.id.main_content);
		mainText.setText(mainTextVal.replace("\\\n",
				System.getProperty("line.separator")));

		TextView headingText = (TextView) findViewById(R.id.clickedButtonMessage);
		headingText.setText(button_clicked_message);

		int pref_value = getIntPref(button_clicked);

		Button doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setTag(button_clicked);

		setDoneButtonDisplay(pref_value, doneButton);
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

	private int getIntPref(String key) {
		String pref_key = getPackageName() + "." + key;
		int pref_value = getSharedPrefs().getInt(pref_key, 0);
		return pref_value;
	}

	public void toggleDone(View view) {
		Button button_clicked = (Button) view;
		String tag = (String) button_clicked.getTag();
		String pref_key = getPackageName() + "." + tag;
		int pref_value = getIntPref(tag);

		SharedPreferences.Editor editor = getSharedPrefs().edit();
		if (pref_value != 0) {
			editor.remove(pref_key);
			setDoneButtonDisplay(0, button_clicked);
		} else {
			editor.putInt(pref_key, 1);
			setDoneButtonDisplay(1, button_clicked);
			checkForStartOrComplete();
		}
		editor.commit();
	}
	
	public void checkForStartOrComplete() {
		// go through and see if either first preference in which
		// case we store a date stamp of now
		
		// else if all completed, we set date stamp of completed and do stuff
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
