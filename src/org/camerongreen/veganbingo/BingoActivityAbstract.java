package org.camerongreen.veganbingo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Bingo is a kind of abstract concept to begin with really.  I had some repeat
 * code in my screen activities, really it should be one clever class but instead
 * I pulled most of the repeat code out into an abstract class
 * 
 * @author Cameron Green <i@camerongreen.org>
 *
 */
public abstract class BingoActivityAbstract extends Activity {

	public BingoActivityAbstract() {
		super();
	}

	/**
	 * Convert the text in a view to html.  This enables me to use the strings defs in XML
	 * and know they'll get converted automatically
	 * 
	 * @param viewId
	 */
	protected void convertTextToHtml(int viewId) {
		TextView textView = (TextView) findViewById(viewId);
		String text = textView.getText().toString();
		textView.setText(Html.fromHtml(text));
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}