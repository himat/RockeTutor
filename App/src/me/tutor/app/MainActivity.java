package me.tutor.app;

import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<String> userList;
	private EditText usernameEditText;
	private EditText passwordEditText; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);

		userList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.users_list)));
		
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
