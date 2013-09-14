package org.camerongreen.veganbingo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Vegan bingo main class.  My first android app and first bit of Java
 * in about five years so if you have any code based 
 * suggestions or improvements send away :)
 * 
 * @author Cameron Green <i@camerongreen.org>
 */
public class MainActivity extends Activity {
	public static String PACKAGE_NAME;

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String[] choices = { "bacon", "preachy", "protein",
			"cheese", "cow", "plants", "teeth", "food", "natural", "humane",
			"eat", "notmuch", "what", "cant", "aspirational", "hitler" };
	public final static String[] colours = { "mygreen", "myblue", "mypink",
			"myyellow", "mypink", "myyellow", "mygreen", "myblue" };
	private List<ImageButton> buttons = new ArrayList<ImageButton>();
	private MyPrefs prefs = null;
	private int gridSize = 4;
	private TextView timerText;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateTimerText();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PACKAGE_NAME = getPackageName();

		prefs = new MyPrefs(getSharedPreferences(MainActivity.PACKAGE_NAME,
				Context.MODE_PRIVATE));

		GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				int place = getPlace(i, j);
				String tag = choices[place];

				boolean buttonClicked = prefs.getBooleanPref(tag);

				ImageButton btn = makeButton(buttonClicked, i, j, tag);

				// place the button in the grid, kinda weird way to do it
				// don't see why the button should care where it is
				params.rowSpec = GridLayout.spec(i);
				params.columnSpec = GridLayout.spec(j);
				btn.setLayoutParams(params);

				int dimen = screenWidth / gridSize;
				grid.addView(btn, dimen, dimen);
				buttons.add(btn);
			}
		}

		startTimer();
		updateStats();
	}

	/**
	 * Call this on each tick, check the state of started and finished
	 * and display the timer accordingly.
	 * 
	 * A little inefficient to keep calling if the user is either not started
	 * or has finished, but this whole handler thing is a little sketchy to me
	 * so until I read a book or something, will leave it as is, ie working well
	 */
	protected void updateTimerText() {
		long finished = prefs.getLongPref("finished");
		long endTime;

		if (finished == 0l) {
			endTime = System.currentTimeMillis();
		} else {
			endTime = finished;
		}

		long started = prefs.getLongPref("started");
		long elapsed_seconds = started > 0l ? (endTime - started) / 1000 : 0;
		long hours = elapsed_seconds / (60 * 60);
		elapsed_seconds = elapsed_seconds % (60 * 60);
		long minutes = elapsed_seconds / 60;
		elapsed_seconds = elapsed_seconds % 60;

		String retime = String.format("%02d:%02d:%02d", hours, minutes,
				elapsed_seconds);

		timerText.setText(retime);
	}

	/**
	 * This just initialises the timer object
	 */
	protected void startTimer() {
		timerText = (TextView) findViewById(R.id.timer);
	}

	@Override
	public void onStart() {
		super.onStart();

		Thread background = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(1000);
						handler.sendMessage(handler.obtainMessage());
					}
				} catch (Exception e) {
					Log.v("Error", e.toString());
				}
			}
		});

		background.start();
	}

	/**
	 * Cycles through the buttons, synchronizing their state
	 * with that stored in the users preferences.  
	 */
	protected void updateButtons() {
		for (int i = 0; i < choices.length; i++) {
			boolean clicked = prefs.getBooleanPref(choices[i]);
			ImageButton button = (ImageButton) findViewById(i);
			setButtonImage(clicked, button);
		}
	}

	/**
	 * Androidy thing...sure this will get me in trouble at some point
	 */
	public void onResume() {
		super.onResume();
		updateButtons();
		updateStats();
	}

	/**
	 * Update all the text bits based on the current preferences
	 */
	private void updateStats() {
		int choicesCompleted = prefs.countPrefs(choices);

		showScore(choicesCompleted);
		showStarted(choicesCompleted);
		showFinished(choicesCompleted);
	}

	/**
	 * Simple function to turn 2d x and y into place in array
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	private int getPlace(int row, int column) {
		int place = (row * gridSize) + column;
		return place;
	}

	/**
	 * Makes one of the image buttons, displaying it in the grid 
	 * and using the clicked state to decide on image to show
	 * 
	 * @param buttonClicked
	 * @param row
	 * @param column
	 * @param tag
	 * @return
	 */
	private ImageButton makeButton(boolean buttonClicked, int row, int column,
			String tag) {
		int place = getPlace(row, column);

		ImageButton btn = new ImageButton(this);
		btn.setTag(tag);
		// all a bit mysterious ids in Android, the system assigns them automatically
		// but apparently it won't override any I set here...so just set it to place which 
		// should be unique and unchanging
		btn.setId(place);

		int stringId = getResources().getIdentifier(
				"@string/" + tag + "_description", "yid", PACKAGE_NAME);
		btn.setContentDescription(getResources().getString(stringId));

		int colourId = getResources()
				.getIdentifier("@color/" + colours[place % colours.length],
						"id", PACKAGE_NAME);
		btn.setBackgroundResource(colourId);

		setButtonImage(buttonClicked, btn);

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

	/**
	 * Either set the buttons image to done or the default image
	 * depending on whether the user has marked as done
	 * 
	 * @param buttonDone
	 * @param btn
	 */
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

	/**
	 * Display the finished text and time if the user has finished
	 * 
	 * @param choicesCompleted
	 */
	private void showFinished(int choicesCompleted) {
		TextView finished = (TextView) findViewById(R.id.finished);
		TextView finishedLabel = (TextView) findViewById(R.id.finished_text);
		
		long finishedMilli = prefs.getLongPref("finished");
		
		if (choicesCompleted != choices.length) {
			finishedLabel.setVisibility(View.GONE);
			finished.setVisibility(View.GONE);
		} else {
			Date finishedDate = new Date(finishedMilli);
			finishedLabel.setVisibility(View.VISIBLE);
			finished.setVisibility(View.VISIBLE);
			String finishedString = DateFormat.getDateTimeInstance().format(
					finishedDate);
			finished.setText(finishedString);
			showFinish();
		}
	}

	/**
	 * Do something lame when the user has finished.  Actually I wanted to do
	 * something cool but did what I could
	 */
	private void showFinish() {
		setHeaderText(getResources().getString(R.string.app_name) + "!!!");

		throbHeaderText();

		Animation min = AnimationUtils.loadAnimation(this, R.animator.fini);

		for (int i = 0; i < choices.length; i++) {
			ImageButton button = (ImageButton) findViewById(i);
			button.startAnimation(min);
		}
	}

	/**
	 * Display the started text and time
	 * 
	 * @param choicesCompleted
	 */
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

	/**
	 * Show the users current "score"
	 * 
	 * @param choicesCompleted
	 */
	private void showScore(int choicesCompleted) {
		String scoreText = getString("score_text");
		setHeaderText(scoreText + ": " + choicesCompleted + "/"
				+ choices.length);
	}

	/**
	 * Generic helper to get string by name
	 * 
	 * @param resourceName
	 */
	private String getString(String resourceName) {
		int stringId = getResources().getIdentifier("@string/" + resourceName,
				"id", getPackageName());
		String message = getResources().getString(stringId);
		return message;
	}

	/**
	 * Method to set the header text, used for score and finished anims
	 * 
	 * @param text
	 */
	private void setHeaderText(String text) {
		TextView header = (TextView) findViewById(R.id.main_text);
		header.setText(text);
	}

	/**
	 * Not as rude as it sounds
	 */
	private void throbHeaderText() {
		TextView header = (TextView) findViewById(R.id.main_text);
		Animation throb = AnimationUtils.loadAnimation(this, R.animator.throb);
		header.startAnimation(throb);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Do the menu stuff, passing off to different screens and
	 * activities
	 */
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
		case R.id.restart:
			intent.setClass(this, ShowRestartScreenActivity.class);
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
