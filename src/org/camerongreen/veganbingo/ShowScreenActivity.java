package org.camerongreen.veganbingo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_screen);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		
		String button_clicked = intent.getStringExtra(MainActivity.BUTTON_CLICKED);
		String button_clicked_message = intent.getStringExtra(MainActivity.BUTTON_CLICKED_MESSAGE);
		
		int imgId = getResources().getIdentifier("@drawable/" + button_clicked, "id", getPackageName());
		ImageView imageView = (ImageView) findViewById(R.id.main_icon);
		imageView.setImageResource(imgId);

		int stringId = getResources().getIdentifier("@string/" + button_clicked + "_main", "id", getPackageName());
		String mainTextVal = getResources().getString(stringId);
		TextView mainText = (TextView) findViewById(R.id.main_content);
		mainText.setText(mainTextVal.replace("\\n", System.getProperty("line.separator")));

		TextView headingText = (TextView) findViewById(R.id.clickedButtonMessage);
		headingText.setText(button_clicked_message);		
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
