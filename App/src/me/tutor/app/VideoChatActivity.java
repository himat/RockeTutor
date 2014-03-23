package me.tutor.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

/**
 * This application demonstrates the basic workflow for getting started with the
 * OpenTok 2.0 Android SDK. For more information, see the README.md file in the
 * samples directory.
 */
public class VideoChatActivity extends Activity implements Session.Listener,
        Publisher.Listener, Subscriber.Listener {
    
	private final String LOGTAG = "video-chat-activity";
	
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ArrayList<Stream> mStreams;
    private RelativeLayout publisherViewContainer;
    private RelativeLayout subscriberViewContainer;
    private TextView otherUserView;
    
    private String SESSION_ID;
    private String TOKEN;
    private String API_KEY;
    private Boolean SUBSCRIBE_TO_SELF = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_chat);

        publisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        subscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
        otherUserView = (TextView) findViewById(R.id.otherUserTextView);
        
        Button collabButton = (Button) findViewById(R.id.collaborativeTextButton);
        collabButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VideoChatActivity.this, CollabTextActivity.class);
				startActivity(intent);
			}
        	
        });
        
        Intent intent = getIntent();
        
        otherUserView.setText("You are now chatting with: " + intent.getStringExtra(UsersListActivity.userID));
        
        API_KEY = intent.getStringExtra(UsersListActivity.apikey);
        
        SESSION_ID = intent.getStringExtra(UsersListActivity.sessionid);
        
        TOKEN = intent.getStringExtra(UsersListActivity.token);

        mStreams = new ArrayList<Stream>();

        sessionConnect();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mSession != null) {
            mSession.disconnect();
        }
        finish();
    }

    private void sessionConnect() {
        if (mSession == null) {
            mSession = new Session(VideoChatActivity.this, SESSION_ID, VideoChatActivity.this);
            mSession.connect(API_KEY, TOKEN);
        }
    }

    @Override
    public void connected(Session session) {
        Log.i(LOGTAG, "Connected to the session.");
        if (mPublisher == null) {
            mPublisher = new Publisher(VideoChatActivity.this, VideoChatActivity.this, "publisher");
            mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    320, 240);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                    RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                    RelativeLayout.TRUE);
            layoutParams.bottomMargin = dpToPx(8);
            layoutParams.rightMargin = dpToPx(8);
            publisherViewContainer.addView(mPublisher.getView(), layoutParams);

            mSession.publish(mPublisher);
        }
    }

    @Override
    public void disconnected(Session session) {
        if (mPublisher != null) {
            publisherViewContainer.removeView(mPublisher.getView());
        }

        if (mSubscriber != null) {
            subscriberViewContainer.removeView(mSubscriber.getView());
        }

        mPublisher = null;
        mSubscriber = null;
        mStreams.clear();
        mSession = null;
    }

    private void subscribeToStream(Stream stream) {
        mSubscriber = new Subscriber(VideoChatActivity.this, stream, VideoChatActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels, getResources()
                        .getDisplayMetrics().heightPixels);
        subscriberViewContainer.addView(mSubscriber.getView(), layoutParams);

        mSession.subscribe(mSubscriber);
    }

    private void unsubscriberToStream(Stream stream) {
        mStreams.remove(stream);
        if (mSubscriber.getStream().getStreamId().equals(stream.getStreamId())) {
            subscriberViewContainer.removeView(mSubscriber.getView());
            mSubscriber = null;
            if (!mStreams.isEmpty()) {
                subscribeToStream(mStreams.get(0));
            }
        }
    }

    @Override
    public void error(Session session, OpentokError exception) {
        Log.i(LOGTAG, "Session exception: " + exception.getMessage());
    }

    @Override
    public void receivedStream(Session session, Stream stream) {
        
    	if (!SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void droppedStream(Session session, Stream stream) {
        if (mSubscriber != null) {
            unsubscriberToStream(stream);
        }
    }

    @Override
    public void streamCreated(PublisherKit publisher, Stream stream) {
    	if (SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
    	}
    }

    @Override
    public void streamDestroyed(PublisherKit publisher, Stream stream) {
    	if ((SUBSCRIBE_TO_SELF && mSubscriber != null)) {
            unsubscriberToStream(stream);
        }
    }

    @Override
    public void connected(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Subscriber connected.");
    }
    
    @Override
    public void disconnected(SubscriberKit arg0) {
        Log.i(LOGTAG, "Subscriber disconnected.");
    }

    @Override
    public void error(SubscriberKit subscriber, OpentokError exception) {
        Log.i(LOGTAG, "Subscriber exception: " + exception.getMessage());
    }

    @Override
    public void changedCamera(PublisherKit publisher, int newCameraId) {
        Log.i(LOGTAG, "The publisher changed camera.");
    }

    @Override
    public void addPublisher(Session session, PublisherKit publisher) {
        Log.i(LOGTAG, "The publisher starts streaming");
    }

    @Override
    public void removePublisher(Session session, PublisherKit publisher) {
        Log.i(LOGTAG, "The publisher stops streaming");
    }

    @Override
    public void error(PublisherKit publisher, OpentokError exception) {
        Log.i(LOGTAG, "Publisher exception: " + exception.getMessage());
    }

    @Override
    public void connectionCreated(Session session, Connection connection) {
        Log.i(LOGTAG, "New client connected to the session.");
    }

    @Override
    public void connectionDestroyed(Session session, Connection connection) {
        Log.i(LOGTAG, "A client disconnected from the session.");
    }

    @Override
    public void videoDisabled(SubscriberKit subscriber) {
        Log.i(LOGTAG,
                "Video quality changed. It is disabled for the subscriber.");
    }

    @Override
    public void videoDataReceived(SubscriberKit arg0) {
        Log.i(LOGTAG, "First frame received");
    }

    /**
     * Converts dp to real pixels, according to the screen density.
     * 
     * @param dp
     *            A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    private int dpToPx(int dp) {
        double screenDensity = this.getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }

    @Override
    public void onSignal(Session session, String type, String signal,
            Connection connection) {
        Log.i(LOGTAG, "Signal received");

    }

    @Override
    public void streamChangeHasAudio(Session session, Stream stream,
            int audioEnabled) {
        Log.i(LOGTAG, "Stream audio changed");

    }

    @Override
    public void streamChangeHasVideo(Session session, Stream stream,
            int videoEnabled) {
        Log.i(LOGTAG, "Stream video changed");

    }

    @Override
    public void streamChangeVideoDimensions(Session session, Stream stream,
            int width, int height) {
        Log.i(LOGTAG, "Stream video dimensions changed");

    }

}