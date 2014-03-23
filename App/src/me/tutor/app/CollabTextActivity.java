package me.tutor.app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class CollabTextActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 WebView webview = new WebView(this);
		 setContentView(webview);
		 webview.loadUrl("http://rocketutor.com/fireEdit.html");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collab_text, menu);
		return true;
	}

}
