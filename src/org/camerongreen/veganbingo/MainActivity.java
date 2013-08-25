package org.camerongreen.veganbingo;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static String PACKAGE_NAME;

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String[] choices = { "bacon", "preachy", "protein",
			"cheese", "cow", "plants", "teeth", "food", "natural", "humane",
			"eat", "notmuch", "what", "cant", "aspirational", "hitler" };
	public final static String[] colours = { "mygreen", "myblue", "mypink",
			"myyellow", "mypink", "myyellow", "mygreen", "myblue" };
	private MyPrefs prefs = null;
	private int gridSize = 4;
	private int buttonSizeDp = 80;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PACKAGE_NAME = getPackageName();

		prefs = new MyPrefs(getSharedPreferences(MainActivity.PACKAGE_NAME,
				Context.MODE_PRIVATE));

		GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				int place = getPlace(i, j);
				String tag = choices[place];

				boolean buttonClicked = prefs.getBooleanPref(tag);

				ImageButton btn = makeButton(buttonClicked, i, j, tag);

				float d = getResources().getDisplayMetrics().density;
				int dimen = (int) (buttonSizeDp * d);
				grid.addView(btn, dimen, dimen);
			}

			updateStats();
		}

	}

	private void updateStats() {
		int choicesCompleted = prefs.countPrefs(choices);

		showScore(choicesCompleted);
		showStarted(choicesCompleted);
		showFinished(choicesCompleted);
	}

	private int getPlace(int i, int j) {
		int place = (i * gridSize) + j;
		return place;
	}

	private ImageButton makeButton(boolean buttonClicked, int i, int j,
			String tag) {
		ImageButton btn = new ImageButton(this);

		int place = getPlace(i, j);

		int stringId = getResources().getIdentifier(
				"@string/" + tag + "_description", "id", PACKAGE_NAME);
		btn.setContentDescription(getResources().getString(stringId));

		int colourId = getResources()
				.getIdentifier("@color/" + colours[place % colours.length],
						"id", PACKAGE_NAME);
		btn.setBackgroundResource(colourId);

		btn.setTag(tag);

		setButtonImage(buttonClicked, btn);

		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.rowSpec = GridLayout.spec(i);
		params.columnSpec = GridLayout.spec(j);
		btn.setLayoutParams(params);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ImageButton button = (ImageButton) view;
				Context context = view.getContext();
				Intent intent = new Intent(context, ShowScreenActivity.class);
				intent.putExtra(BUTTON_CLICKED, "" + button.getTag());
				context.startActivity(intent);
			}
		});
		btn.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				ImageButton button = (ImageButton) view;
				String tag = (String) button.getTag();

				BingoState bingo = new BingoState(prefs);
				boolean done = bingo.toggleDone(tag);

				setButtonImage(done, button);

				updateStats();
				return true;
			}
		});

		return btn;
	}

	private void setButtonImage(boolean buttonDone, ImageButton btn) {
		String tag = (String) btn.getTag();
		String imageIdString;

		if (buttonDone) {
			btn.setAlpha(175);
			imageIdString = "@drawable/" + tag + "_done";
		} else {
			btn.setAlpha(255);
			imageIdString = "@drawable/" + tag;
		}

		int imageId = getResources().getIdentifier(imageIdString, "id",
				PACKAGE_NAME);
		btn.setImageResource(imageId);
	}

	private void showFinished(int choicesCompleted) {
		TextView finished = (TextView) findViewById(R.id.finished);

		long finishedMilli = prefs.getLongPref("finished");
		if (choicesCompleted != choices.length) {
			String not_finished = getResources().getString(
					R.string.not_started_text);
			finished.setText(not_finished);
		} else {
			Date finishedDate = new Date(finishedMilli);
			String finishedString = DateFormat.getDateTimeInstance().format(
					finishedDate);
			finished.setText(finishedString);
		}
	}

	private void showStarted(int choicesCompleted) {
		TextView started = (TextView) findViewById(R.id.started);
		long startedMilli = prefs.getLongPref("started");

		if (choicesCompleted == 0) {
			String not_started = getResources().getString(
					R.string.not_started_text);
			started.setText(not_started);
		} else {
			Date startedDate = new Date(startedMilli);
			String startedString = DateFormat.getDateTimeInstance().format(
					startedDate);
			started.setText(startedString);
		}
	}

	private void showScore(int choicesCompleted) {
		TextView score = (TextView) findViewById(R.id.score);
		score.setText(choicesCompleted + "/" + choices.length);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.about:
			intent.setClass(this, ShowAboutScreenActivity.class);
			this.startActivity(intent);
			return true;
		case R.id.howto:
			intent.setClass(this, ShowHowToScreenActivity.class);
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}

}
