package org.camerongreen.veganbingo;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String BUTTON_CLICKED_MESSAGE = "org.camerongreen.veganbingo.BUTTON_MESSAGE";
	public final static String[] choices = { "bacon", "preachy", "protein",
			"cheese", "cow", "plants", "teeth", "food", "natural", "humane",
			"eat", "notmuch", "what", "cant", "aspirational", "hitler" };
	public final static String[] colours = { "mygreen", "myblue", "mypink",
			"myyellow", "mypink", "myyellow", "mygreen", "myblue", "mygreen",
			"myblue", "mypink", "myyellow", "mypink", "myyellow", "mygreen",
			"myblue" };
	private SharedPreferences sharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int gridSize = 4;
		int place = 0;
		int clicked = 0;

		GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				String tag = choices[place];
				ImageButton btn = new ImageButton(this);
				int buttonClicked = getIntPref(tag);
				int imageId = 0;
				if (buttonClicked == 1) {
					clicked++;
					btn.setAlpha(175);
					imageId = getResources().getIdentifier(
							"@drawable/" + tag + "_done", "id",
							getPackageName());
				} else {
					imageId = getResources().getIdentifier("@drawable/" + tag,
							"id", getPackageName());
				}
				btn.setImageResource(imageId);
				int stringId = getResources().getIdentifier(
						"@string/" + tag + "_description", "id",
						getPackageName());
				btn.setContentDescription(getResources().getString(stringId));
				int colourId = getResources().getIdentifier(
						"@color/" + colours[place], "id", getPackageName());
				btn.setBackgroundResource(colourId);
				btn.setTag(tag);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				params.rowSpec = GridLayout.spec(i);
				params.columnSpec = GridLayout.spec(j);
				btn.setLayoutParams(params);
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						ImageButton button = (ImageButton) view;
						Context context = view.getContext();
						Intent intent = new Intent(context,
								ShowScreenActivity.class);
						intent.putExtra(BUTTON_CLICKED, "" + button.getTag());
						intent.putExtra(BUTTON_CLICKED_MESSAGE, button
								.getContentDescription().toString());
						context.startActivity(intent);
					}
				});
				int dpValue = 80;
				float d = getResources().getDisplayMetrics().density;
				int dimen = (int) (dpValue * d);
				grid.addView(btn, dimen, dimen);
				++place;
			}

			TextView score = (TextView) findViewById(R.id.score);
			score.setText(clicked + "/" + choices.length);

			TextView started = (TextView) findViewById(R.id.started);
			long startedMilli = getLongPref("started");
			if (clicked == 0) {
				String not_started = getResources().getString(R.string.not_started_text);
				started.setText(not_started);
			} else {
				Date startedDate = new Date(startedMilli);
				String startedString = DateFormat.getDateTimeInstance().format(
						startedDate);
				started.setText(startedString);
			}
			TextView finished = (TextView) findViewById(R.id.finished);
			
			long finishedMilli = getLongPref("finished");
			if (clicked != 16) {
				String not_finished = getResources().getString(R.string.not_started_text);
				finished.setText(not_finished);
			} else {
				Date finishedDate = new Date(finishedMilli);
				String finishedString = DateFormat.getDateTimeInstance().format(
						finishedDate);
				finished.setText(finishedString);
			}
		}

	}

	private long getLongPref(String key) {
		String pref_key = getPackageName() + "." + key;
		long pref_value = getSharedPrefs().getLong(pref_key, 0l);
		return pref_value;
	}

	private int getIntPref(String key) {
		String pref_key = getPackageName() + "." + key;
		int pref_value = getSharedPrefs().getInt(pref_key, 0);
		return pref_value;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private SharedPreferences getSharedPrefs() {
		if (sharedPref == null) {
			sharedPref = this.getSharedPreferences(getPackageName(),
					Context.MODE_PRIVATE);
		}
		return sharedPref;
	}

}
