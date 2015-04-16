package com.example.testapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Steptimer 
{
	Timer timer;
	Context ctx;
	Activity act;
	Step step;
	int timeLeft,maxTime;
	boolean running;
	
	public Steptimer(int time,Context ctx, Activity act,Step step)
	{
		this.timeLeft=time;
		this.maxTime=time;
		this.ctx = ctx;
		this.act = act;
		this.running = true;
		this.step=step;
		setTimer();
	}
	public Step getStep()
	{
		return step;
	}
	private void setTimer()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run() 
			{	
				act.runOnUiThread(new Runnable()
				{
					@Override
					public void run() 
					{
						if(timeLeft==0)
						{
							cancelTimer();
							running=false;
							playAlarm();
						}
						else
							timeLeft -=1;
						
					}
				});
			}
		}, 0, 1000);
	}
	public void cancelTimer()
	{
		timer.cancel();
		timer.purge();
	}
	public int getTimeMinsLeft()
	{
		int minutes = (int) Math.floor(timeLeft/60);
		return minutes;
	}
	public int getTimeSecsLeft()
	{
		int seconds = timeLeft%60;
		return seconds;
	}
	public boolean getIsRunning()
	{
		return running;
	}
	public void playAlarm()
	{
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Ringtone r = RingtoneManager.getRingtone(ctx, notification);
		r.play();
	    alarmDialog(r);
	}
	private void alarmDialog(final Ringtone r)
	{
		LinearLayout ll = new LinearLayout(ctx);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);

		final AlertDialog.Builder timerExpired = new AlertDialog.Builder(act);
		timerExpired.setCancelable(false);
		timerExpired.setTitle("Reminder");
		TextView title = new TextView(ctx);
		TextView details= new TextView(ctx);
		TextView hints;
		TextView notice = new TextView(ctx);
		notice.setTextSize(18);
		title.setTextSize(20);
		details.setTextSize(18);
		if(step.getStepNo()!=0)
		{
			title.setText("The timer for step "+step.getStepNo()+" has concluded this steps details were");
			details.setText(step.getDetails());
			ll.addView(title,layoutParams);
			ll.addView(details,layoutParams);
			for(Hint hint : step.getHints())
			{
				hints = new TextView(ctx);
				hints.setTextSize(16);
				hints.setText("Hint: "+hint.getHintNo()+" "+hint.getDetails());
				ll.addView(hints,layoutParams);
			}	
		}
		else
		{
			title.setText("This is a Custom Timer");
			details.setText(step.getDetails());
			ll.addView(title,layoutParams);
			ll.addView(details,layoutParams);
		}
		notice.setText("Please turn off any cooking devices and remove any food this alarm relates to, remember safety first :-)");
		ll.addView(notice,layoutParams);
		timerExpired.setView(ll);
		timerExpired.setNeutralButton("OK",new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				r.stop();
				d.cancel();
			}
		});
		AlertDialog dialog = timerExpired.create();
		dialog.show();
	}
}
