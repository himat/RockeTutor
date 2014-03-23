package me.tutor.app;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {
	private final int progressMax = 5000;
	
	private Handler mHandler = new Handler();
	
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mProgress = (ProgressBar) findViewById(R.id.splashProgressBar);
		mProgress.setMax(progressMax);
		
		Thread timer = new Thread()	{
			public void run() {
                long now = System.currentTimeMillis();
				while (System.currentTimeMillis() - now < progressMax) {
                    mProgressStatus = (int)(System.currentTimeMillis() - now); 

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }

				Intent openStartingPoint = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(openStartingPoint);
            }
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
