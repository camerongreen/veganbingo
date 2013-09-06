package org.camerongreen.veganbingo;

import android.os.Bundle;

public class ShowAboutScreenActivity extends BingoActivityAbstract {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_about_screen);
		setupActionBar();
		
		convertTextToHtml(R.id.about_content);
	}
		
}
