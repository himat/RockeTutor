package me.tutor.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import me.tutor.opentok.CustomVideoRenderer;

public class OpenTokVideoRenderer extends Activity implements Session.Listener,
        Publisher.Listener, Subscriber.Listener {

    private static final String LOGTAG = "demo-customer-video-capturer";
   
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ArrayList<Stream> mStreams = new ArrayList<Stream>();
    private RelativeLayout publisherViewContainer;
    private RelativeLayout subscriberViewContainer;
    
    private String SESSION_ID = "1_MX40NDcwNDIzMn5-U2F0IE1hciAyMiAxODoyNzowNSBQRFQgMjAxNH4wLjM1OTc0OTYyfg";
    private String TOKEN = "T1==cGFydG5lcl9pZD00NDcwNDIzMiZzZGtfdmVyc2lvbj10YnJ1YnktdGJyYi12MC45MS4yMDExLTAyLTE3JnNpZz1kM2MyMDlhMTNiYjQ0NGRhYjJkZDljNGMyYzY5NDNjNGE0MjM3ODViOnJvbGU9cHVibGlzaGVyJnNlc3Npb25faWQ9MV9NWDQwTkRjd05ESXpNbjUtVTJGMElFMWhjaUF5TWlBeE9Eb3lOem93TlNCUVJGUWdNakF4Tkg0d0xqTTFPVGMwT1RZeWZnJmNyZWF0ZV90aW1lPTEzOTU1MzgwNzYmbm9uY2U9MC4xNDAzNzcyODYwMDE5MDY0NiZleHBpcmVfdGltZT0xMzk1NjI0NDIxJmNvbm5lY3Rpb25fZGF0YT0=";
    private String API_KEY;
    private Boolean SUBSCRIBE_TO_SELF = true;  
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_chat);

        publisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        subscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);

        mStreams = new ArrayList<Stream>();
        
        API_KEY = getResources().getString(R.string.API_KEY);
        
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

        if (isFinishing()) {
            if (mSession != null) {
                mSession.disconnect();
            }
        }
    }

    private void sessionConnect() {
        if (mSession == null) {
            mSession = new Session(this, SESSION_ID, this);
            mSession.connect(API_KEY, TOKEN);
        }
    }

    private void subscribeToStream(Stream stream) {
        mSubscriber = new Subscriber(OpenTokVideoRenderer.this, stream,
                OpenTokVideoRenderer.this);

        mSubscriber.setRenderer(new CustomVideoRenderer(this));

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
    public void connected(Session session) {

        if (mPublisher == null) {

            mPublisher = new Publisher(OpenTokVideoRenderer.this,
                    OpenTokVideoRenderer.this, "publisher");

            // use an external custom video renderer
            mPublisher.setRenderer(new CustomVideoRenderer(this));

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    320, 240);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                    RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                    RelativeLayout.TRUE);
            layoutParams.bottomMargin = dpToPx(8);
            layoutParams.rightMargin = dpToPx(8);

            // use the default SDK video renderer
            publisherViewContainer.addView(mPublisher.getView(), layoutParams);

            mSession.publish(mPublisher);

        }
    }

    @Override
    public void disconnected(Session arg0) {

        if (mPublisher != null) {
            publisherViewContainer.removeView(mPublisher.getRenderer()
                    .getView());
        }

        if (mSubscriber != null) {
            subscriberViewContainer.removeView(mSubscriber.getRenderer()
                    .getView());
        }

        mPublisher = null;
        mSubscriber = null;
        mStreams.clear();
        mSession = null;

    }

    @Override
    public void receivedStream(Session session, Stream stream) {
     
    	if (SUBSCRIBE_TO_SELF) {
            mStreams.add(stream);
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void droppedStream(Session session, Stream stream) {

        unsubscriberToStream(stream);
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
    public void addPublisher(Session session, PublisherKit publisher) {
        Log.i(LOGTAG, "The publisher starts streaming");
    }

    @Override
    public void removePublisher(Session session, PublisherKit publisher) {
        Log.i(LOGTAG, "The publisher stops streaming");

    }

    @Override
    public void changedCamera(PublisherKit publisher, int newCameraId) {
        Log.i(LOGTAG, "The publisher changed camera.");
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
    public void error(Session session, OpentokError exception) {
        Log.i(LOGTAG, "Session exception: " + exception.getMessage());
    }

    @Override
    public void connected(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Subscriber connected.");
    }

    public void disconnected(SubscriberKit subscriber) {
        Log.i(LOGTAG, "Subscriber disconnected.");
    }

    @Override
    public void error(SubscriberKit subscriber, OpentokError exception) {
        Log.i(LOGTAG, "Subscriber exception: " + exception.getMessage());
    }

    @Override
    public void videoDisabled(SubscriberKit subscriber) {
        Log.i(LOGTAG,
                "Video quality changed. It is disabled for the subscriber. ");

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
    public void videoDataReceived(SubscriberKit arg0) {
        Log.i(LOGTAG, "First frame received");
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
