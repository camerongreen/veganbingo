package org.camerongreen.veganbingo;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static String PACKAGE_NAME;

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String BUTTON_CLICKED_MESSAGE = "org.camerongreen.veganbingo.BUTTON_MESSAGE";
	public final static String[] choices = { "bacon", "preachy", "protein",
			"cheese", "cow", "plants", "teeth", "food", "natural", "humane",
			"eat", "notmuch", "what", "cant", "aspirational", "hitler" };
	public final static String[] colours = { "mygreen", "myblue", "mypink",
			"myyellow", "mypink", "myyellow", "mygreen", "myblue", "mygreen",
			"myblue", "mypink", "myyellow", "mypink", "myyellow", "mygreen",
			"myblue" };
	private MyPrefs prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PACKAGE_NAME = getPackageName();
		prefs = new MyPrefs(getSharedPreferences(MainActivity.PACKAGE_NAME,
				Context.MODE_PRIVATE));

		int gridSize = 4;
		int choicesCompleted = 0;
		
		GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				int place = (i * gridSize) + j;
				String tag = choices[place];
				ImageButton btn = new ImageButton(this);
				int buttonClicked = prefs.getIntPref(tag);
				int imageId = 0;
				if (buttonClicked >= 1) {
					choicesCompleted++;
					btn.setAlpha(175);
					imageId = getResources().getIdentifier(
							"@drawable/" + tag + "_done", "id",
							PACKAGE_NAME);
				} else {
					imageId = getResources().getIdentifier("@drawable/" + tag,
							"id", PACKAGE_NAME);
				}
				btn.setImageResource(imageId);
				int stringId = getResources().getIdentifier(
						"@string/" + tag + "_description", "id",
						PACKAGE_NAME);
				btn.setContentDescription(getResources().getString(stringId));
				int colourId = getResources().getIdentifier(
						"@color/" + colours[place], "id", PACKAGE_NAME);
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
			}

			TextView score = (TextView) findViewById(R.id.score);
			score.setText(choicesCompleted + "/" + choices.length);

			TextView started = (TextView) findViewById(R.id.started);
			long startedMilli = prefs.getLongPref("started");
			if (choicesCompleted == 0) {
				String not_started = getResources().getString(R.string.not_started_text);
				started.setText(not_started);
			} else {
				Date startedDate = new Date(startedMilli);
				String startedString = DateFormat.getDateTimeInstance().format(
						startedDate);
				started.setText(startedString);
			}
			TextView finished = (TextView) findViewById(R.id.finished);
			
			long finishedMilli = prefs.getLongPref("finished");
			if (choicesCompleted != choices.length) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
