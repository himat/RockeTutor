package me.tutor.app;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ArrayList<String> userList = new ArrayList<String>();
	private EditText usernameEditText;
	private EditText passwordEditText;
	private AnimationDrawable logoDrawable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		
		ImageView logoIV = (ImageView) findViewById(R.id.logoImageView);
		logoIV.setBackgroundResource(R.drawable.main_logo_anim);
		logoDrawable = (AnimationDrawable) logoIV.getBackground();
		logoDrawable.start();

		try {
			XmlResourceParser userParse = getResources().getXml(R.xml.users);
			int eventType = userParse.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					if (userParse.getName().equals("email") ) {
						eventType = userParse.next();
						userList.add(userParse.getText());
					}
				}
				eventType = userParse.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String inputUsername = usernameEditText.getText().toString();
				
				if (userList.contains(inputUsername))// || usernameEditText.getText().toString()==us2 || usernameEditText.getText().toString()==us3 || usernameEditText.getText().toString()==us4 || usernameEditText.getText().toString()==us5 || usernameEditText.getText().toString()==us6 || usernameEditText.getText().toString()==us7 )
				{
					Intent usersListIntent = new Intent(MainActivity.this, UsersListActivity.class);
					usersListIntent.putExtra(UsersListActivity.yourUserID, inputUsername);
					startActivity(usersListIntent);
				}
				else
				{
					Toast toast = Toast.makeText(MainActivity.this, "Invalid username", Toast.LENGTH_LONG);
					toast.show();
				}
			}
			
		});	
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
