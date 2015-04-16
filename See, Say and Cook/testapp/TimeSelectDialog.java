package com.example.testapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class TimeSelectDialog extends DialogFragment
{
	int timeInSeconds;
	String formatedTime="No Timer";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		LinearLayout ll = new LinearLayout(getActivity());
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(80, 30, 30, 30);
		 
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Set Timer");
	    final NumberPicker hours = new NumberPicker(getActivity());
	    hours.setMaxValue(24);
	    hours.setMinValue(0);
	    final NumberPicker minutes = new NumberPicker(getActivity());
	    minutes.setMaxValue(59);
	    minutes.setMinValue(0);
	    final NumberPicker seconds = new NumberPicker(getActivity());
	    seconds.setMaxValue(59);
	    seconds.setMinValue(0);
	    ll.addView(hours,layoutParams);
	    ll.addView(minutes,layoutParams);
	    ll.addView(seconds,layoutParams);
	    builder.setView(ll);
	    
	    builder.setPositiveButton("Add Timer", new DialogInterface.OnClickListener() 
	    {
	    	 public void onClick(DialogInterface dialog, int id) 
	    	 {
	    		 setTime(hours.getValue(),minutes.getValue(),seconds.getValue());
	    		 dialog.cancel();
	    	 }
	    });
	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	    	 public void onClick(DialogInterface dialog, int id) 
	    	 {
	    		 dialog.cancel();
	    	 }
	    });
	    AlertDialog dialog = builder.create();
	    return dialog;
	}
	private void setTime(int hour,int minute,int second)
	{
		 int converted = hour*60+minute;
		 timeInSeconds = converted*60+second;
		 formatedTime = "The current timers length is. "+hour+":"+minute+":"+second;
	}
	public int getTimeInSeconds()
	{
		return timeInSeconds;
	}
	public String getTime()
	{
		return formatedTime;
	}
}
