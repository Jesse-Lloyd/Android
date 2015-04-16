package com.example.activities;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.example.testapp.R;
import com.example.activities.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Fullscreen extends Activity {

	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;
	private VideoView contentView;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		contentView = (VideoView)findViewById(R.id.fullScreen);
		contentView.setVideoPath((String) (getIntent().getSerializableExtra("path")));
		contentView.start();
		
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() 
		{
			int mControlsHeight;
			int mShortAnimTime;
	
			@Override
			//@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) 
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
				{
					if (mControlsHeight == 0) 
					{
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) 
					{
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} 
				else 
				{
					controlsView.setVisibility(visible ? View.VISIBLE: View.GONE);
				}
				if (visible && AUTO_HIDE) 
					{
						delayedHide(AUTO_HIDE_DELAY_MILLIS);
					}
			}
		});
		contentView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				if (TOGGLE_ON_CLICK)
				{
					mSystemUiHider.toggle();
				} 
				else 
				{
					mSystemUiHider.show();
				}
			}
		});
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		delayedHide(100);
	}
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() 
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) 
		{
			if (AUTO_HIDE) 
			{
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() 
	{
		@Override
		public void run()
		{
			mSystemUiHider.hide();
		}
	};
	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	public void toggle(View view)
	{
		if(contentView.isPlaying())
			contentView.pause();
		else
			contentView.start();
	}
}
