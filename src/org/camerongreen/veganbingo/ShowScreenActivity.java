package org.camerongreen.veganbingo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class shows the pages/screens for each of the individual activities
 * and gives the user a button which they can set their preference on
 * 
 * @author Cameron Green <i@camerongreen.org>
 */
public class ShowScreenActivity extends BingoActivityAbstract {
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
		boolean pref_value = prefs.getBooleanPref(button_clicked);
		setDoneButtonDisplay(pref_value, doneButton);

		String button_clicked_message = getString(button_clicked
				+ "_description");
		setTextView(R.id.clickedButtonMessage, button_clicked_message);

		String rulesTextVal = getString(button_clicked + "_rules");
		setTextView(R.id.rules, rulesTextVal);

		String mainTextVal = getString(button_clicked + "_main");
		setTextView(
				R.id.main_content,
				mainTextVal.replace("\\\n",
						System.getProperty("line.separator")));

	}

	/**
	 * helper method to make setting image view easier
	 * 
	 * @param viewId
	 * @param resourceName
	 */
	private void setImageView(int viewId, String resourceName) {
		int imgId = getResources().getIdentifier("@drawable/" + resourceName,
				"id", getPackageName());
		ImageView imageView = (ImageView) findViewById(viewId);
		imageView.setImageResource(imgId);
	}

	/**
	 * helper method to make setting text view easier
	 * 
	 * @param viewId
	 * @param text
	 */
	private void setTextView(int id, String text) {
		TextView textView = (TextView) findViewById(id);
		textView.setText(Html.fromHtml(text));
	}

	/**
	 * Helper method to get a string
	 * 
	 * @param resourceName
	 * @return
	 */
	private String getString(String resourceName) {
		int stringId = getResources().getIdentifier("@string/" + resourceName,
				"id", getPackageName());
		String message = getResources().getString(stringId);
		return message;
	}

	/**
	 * Display the button differently depending on whether the user has 
	 * completed the task or otherwise
	 * 
	 * @param done
	 * @param doneButton
	 */
	private void setDoneButtonDisplay(boolean done, Button doneButton) {
		int icon = 0;
		if (done) {
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

	/**
	 * Set the users preference to done if not done, or 
	 * not done if done :)
	 * 
	 * @param view
	 */
	public void toggleDone(View view) {
		Button button = (Button) view;
		String tag = (String) button.getTag();

		BingoState bingo = new BingoState(prefs);
		boolean done = bingo.toggleDone(tag);
		setDoneButtonDisplay(done, button);
	}

}
