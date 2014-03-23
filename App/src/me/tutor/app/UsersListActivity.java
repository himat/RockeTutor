package me.tutor.app;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UsersListActivity extends ListActivity {

	public static final String userID = "USER_NAME";
	public static final String yourUserID = "YOUR_USER_NAME";
	private String yourUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_list);
		
		Intent intent = getIntent();
		yourUsername = intent.getStringExtra(yourUserID);
		
		ArrayList<String> users = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.users_list)));
		users.remove(yourUsername);
		String[] cleanUsers = users.toArray(new String[users.size()]);
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.user_list_item, cleanUsers));
		
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent videoIntent = new Intent(UsersListActivity.this, VideoChatActivity.class);
				videoIntent.putExtra(userID, ((TextView)view).getText().toString());
				startActivity(videoIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users_list, menu);
		return true;
	}

}
