package org.camerongreen.veganbingo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String BUTTON_CLICKED_MESSAGE = "org.camerongreen.veganbingo.BUTTON_MESSAGE";
	public final static String[] choices = { "bacon", "hitler", "protein",
			"cheese", "cow", "plants", "teeth", "food", "natural", "humane",
			"eat", "notmuch", "what", "cant", "aspirational", "preachy" };
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

		GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				String tag = choices[place];
				ImageButton btn = new ImageButton(this);
				int stringId = getResources().getIdentifier(
						"@string/" + tag + "_description", "id",
						getPackageName());
				btn.setContentDescription(getResources().getString(stringId));
				int imageId = getResources().getIdentifier("@drawable/" + tag,
						"id", getPackageName());
				btn.setImageResource(imageId);
				int colourId = getResources().getIdentifier(
						"@color/" + colours[place], "id", getPackageName());
				btn.setBackgroundResource(colourId);
				btn.setTag(tag);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				params.rowSpec = GridLayout.spec(i);
				params.columnSpec = GridLayout.spec(j);
				btn.setLayoutParams(params);
				int buttonClicked = getIntPref(tag);
				if (buttonClicked == 1) {
					btn.setAlpha(70);
				}
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
		}

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
