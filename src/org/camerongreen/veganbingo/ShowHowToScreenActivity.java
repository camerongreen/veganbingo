package org.camerongreen.veganbingo;

import android.os.Bundle;

public class ShowHowToScreenActivity extends BingoActivityAbstract {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_howto_screen);
		setupActionBar();
		
		convertTextToHtml(R.id.howto_content);
	}
}
