package org.camerongreen.veganbingo;

import org.camerongreen.veganbingo.R;
import org.camerongreen.veganbingo.ShowScreenActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	public final static String BUTTON_CLICKED = "org.camerongreen.veganbingo.BUTTON";
	public final static String BUTTON_CLICKED_MESSAGE = "org.camerongreen.veganbingo.BUTTON_MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void showScreen(View view) {
		Intent intent = new Intent(this, ShowScreenActivity.class);
		ImageButton button = (ImageButton) view;
		intent.putExtra(BUTTON_CLICKED, "" + button.getTag());
		intent.putExtra(BUTTON_CLICKED_MESSAGE, button.getContentDescription()
				.toString());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
