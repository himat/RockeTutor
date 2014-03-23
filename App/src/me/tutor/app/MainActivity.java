package me.tutor.app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String us1;
	private String us2;
	private String us3;
	private String us4;
	private String us5;
	private String us6;
	private String us7;
	EditText usernameEditText;
	EditText passwordEditText; 
	Context context;
	CharSequence text;
	int duration;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		us1 = "davidmelvinjr@gmail.com";
		us2 = "vivekjain03@gmail.com";
		us3 = "anishjain123@live.com";
		us4 = "achu.totahali@gmail.com";
		us5 = "abhisheklingeneni@gmail.com";
		us6 = "himat@yahoo.com";
		us7 = "sandybisaria@gmail.com";
		
		 context = getApplicationContext();
		 text = "Invalid Username!";
		 duration = Toast.LENGTH_SHORT;
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		usernameEditText.getText().toString();
		
		
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
<<<<<<< HEAD
				Intent intent = new Intent(MainActivity.this, VideoChatActivity.class);
				startActivity(intent);
=======
				/*Intent videoChatIntent = new Intent(getApplicationContext(), OpenTokVideoRenderer.class);
				startActivity(videoChatIntent);*/
				Log.v("user",usernameEditText.getText().toString());
				Log.v("usermy",us1);
				if (usernameEditText.getText().toString().equals(us1))// || usernameEditText.getText().toString()==us2 || usernameEditText.getText().toString()==us3 || usernameEditText.getText().toString()==us4 || usernameEditText.getText().toString()==us5 || usernameEditText.getText().toString()==us6 || usernameEditText.getText().toString()==us7 )
				{
					Intent UsersOnline = new Intent(getApplicationContext(), UsersOnline.class);
					startActivity(UsersOnline);		
				}
				else
				{
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				
>>>>>>> updated website
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
