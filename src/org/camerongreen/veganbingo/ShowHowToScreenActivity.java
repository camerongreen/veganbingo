package org.camerongreen.veganbingo;

import android.os.Bundle;

/**
 * Hmm not much codewise I could say usefully, but whilst you are here reading this
 * here's some advice a wise person once gave me, never eat anything bigger than your
 * head
 * 
 * @author Cameron Green <i@camerongreen.org>
 */
public class ShowHowToScreenActivity extends BingoActivityAbstract {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_howto_screen);
		setupActionBar();
		
		convertTextToHtml(R.id.howto_content);
	}
}
