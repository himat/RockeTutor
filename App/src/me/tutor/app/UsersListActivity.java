package me.tutor.app;

import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.Log;
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
	public static final String apikey = "API_KEY";
	public static final String sessionid = "SESSION_ID";
	public static final String token = "TOKEN";
	public static final String yourUserID = "YOUR_USER_NAME";
	private String yourUsername;
	
	private ArrayList<String> users;
	private ArrayList<String> names;
	private ArrayList<String> apikeys;
	private ArrayList<String> sessionids;
	private ArrayList<String> tokens;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_list);
		
		Intent intent = getIntent();
		yourUsername = intent.getStringExtra(yourUserID);
		
		users  = new ArrayList<String>();
		names  = new ArrayList<String>();
		apikeys = new ArrayList<String>();
		sessionids = new ArrayList<String>();
		tokens = new ArrayList<String>();
		
		try {
			XmlResourceParser userParse = getResources().getXml(R.xml.users);
			int eventType = userParse.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					if (userParse.getName().equals("email") ) {
						eventType = userParse.next();
						if (!userParse.getText().equals(yourUsername)) {
							users.add(userParse.getText());
							eventType = userParse.next(); //END_TAG
							eventType = userParse.next(); //START_TAG
							eventType = userParse.next(); //TEXT
							names.add(userParse.getText());

							eventType = userParse.next(); //END_TAG
							eventType = userParse.next(); //START_TAG
							eventType = userParse.next(); //TEXT
							apikeys.add(userParse.getText());

							eventType = userParse.next(); //END_TAG
							eventType = userParse.next(); //START_TAG
							eventType = userParse.next(); //TEXT
							sessionids.add(userParse.getText());

							eventType = userParse.next(); //END_TAG
							eventType = userParse.next(); //START_TAG
							eventType = userParse.next(); //TEXT
							tokens.add(userParse.getText());
						}
					}
				}
				eventType = userParse.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.user_list_item, users));
		
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent videoIntent = new Intent(UsersListActivity.this, VideoChatActivity.class);
				int index = users.indexOf(((TextView)view).getText().toString());
				videoIntent.putExtra(userID, names.get(index));
				videoIntent.putExtra(apikey, apikeys.get(index));
				videoIntent.putExtra(sessionid, sessionids.get(index));
				videoIntent.putExtra(token, tokens.get(index));
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
