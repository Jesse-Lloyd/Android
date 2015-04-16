package com.example.activities;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Hint;
import com.example.testapp.Quantity;
import com.example.testapp.R;
import com.example.testapp.Recipe;
import com.example.testapp.Step;
import com.example.testapp.Steptimer;
import com.example.testapp.TimeSelectDialog;

public class RecipeSteps extends Activity implements TextToSpeech.OnInitListener
{
	private DatabaseHandler db = new DatabaseHandler(this);
	private Recipe recipe;
	private int cusTimer;
	private final int MY_DATA_CHECK_CODE = 100;
	private static int stepNum;
	private TextView stepTv,hintTv;
	private ImageView iv;
	private VideoView vv;
	private String customTimerdetails ="custom timer";;
	private static AlertDialog dialog;
	private static ArrayList<Steptimer> timers = new ArrayList<Steptimer>();
	private TextToSpeech mTts;
	private static boolean download;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(getIntent().getSerializableExtra("currentStep")!=null)
			stepNum = (Integer) getIntent().getSerializableExtra("currentStep");
		else
			stepNum=0;
		download = true;	
		setContentView(R.layout.step_layout);
		stepTv = (TextView) findViewById(R.id.step_display_view);
		hintTv = (TextView) findViewById(R.id.hint_display);
	
		iv= (ImageView) findViewById(R.id.imageView1);
		vv= (VideoView) findViewById(R.id.videoView1);vv.setVisibility(View.VISIBLE);vv.setFocusable(true);
        MediaController mc=new MediaController(this);
        vv.setMediaController(mc);      
		recipe =  db.getRecipe((Integer) getIntent().getSerializableExtra("pickedRecipe"));	
		displayStep();
	}	
	private ArrayList<String> getIngredients()
	{
		ArrayList<String> toReturn = new ArrayList<String>();
		for( Quantity ing : recipe.getQus())
		{
			toReturn.add(ing.getIngName());
		}
		return toReturn;
	}
	private void ingList(ArrayList<String> ingredients)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		
		AlertDialog.Builder recIngList = new AlertDialog.Builder(this);
		recIngList.setTitle("Ingredients used in this recipe");
		for(String name : ingredients)
		{
			TextView tv = new TextView(this);
			tv.setText(name);
			tv.setTextSize(18);
			ll.addView(tv,layoutParams);
		}
		recIngList.setView(ll);
		recIngList.setNegativeButton("close", null);
		
		AlertDialog dialog = recIngList.create();
		dialog.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.step_layout_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		GeneralDialogs temp = new GeneralDialogs(this);
		switch(item.getItemId())
		{
    		case android.R.id.home:
    			exitDialog();
    			return true;
			case R.id.step_help:
				temp.help(3);
				return true;
			case R.id.fullscreenAction:
				Intent intent = new Intent(this, Fullscreen.class);
				intent.putExtra("path", "android.resource://com.example.testapp/drawable/"+recipe.getStep(stepNum).getMedia());
				startActivity(intent);
				return true;
			case R.id.about:
				temp.about();
				return true;
			case R.id.timerAction:
				createTimer();
				return true;
			case R.id.ingredientlistAction:
				ingList(getIngredients());
				return true;
			case R.id.speak:
				textToSpeechIntent();
				return true;
			case R.id.stopSpeaking:
				mTts.stop();
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void createTimer()
	{
		final TimeSelectDialog newTimer = new TimeSelectDialog();
		newTimer.show(getFragmentManager(), "timer");
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run() 
			{	
				runOnUiThread(new Runnable()
				{
					@Override
					public void run() 
					{
						if(newTimer.getTimeInSeconds() != 0)
						{
							Step step = new Step();
							if(customTimerdetails.compareTo("custom timer")==0)
							{	
								if(cusTimer==0)
								{
									setCustomTimerDetails();
									cusTimer=1;
								}	
							}
							else
							{
								step.setTimer(newTimer.getTimeInSeconds());
								step.setDetails("this timer was created by you for you to "+customTimerdetails);
								cusTimer=0;
								customTimerdetails = "custom timer";
								Steptimer stepTimer = new Steptimer(newTimer.getTimeInSeconds(),getBaseContext(),RecipeSteps.this,step);
								timers.add(stepTimer);
								timer.cancel();
								timer.purge();
								Button getT = (Button) findViewById(R.id.active_times);
								getT.setEnabled(true);
							}	
						}
					}
				});
			}
		},0, 1000);
	}
	private boolean setCustomTimerDetails()
	{
		 AlertDialog.Builder timerDetails = new AlertDialog.Builder(this);
		 timerDetails.setTitle("Why are you createing this timer?");
		 timerDetails.setCancelable(false);
		 final EditText et = new EditText(this);
		 et.setHint("enter what this timer is reminding you to do here");
		 timerDetails.setPositiveButton("Okay", new DialogInterface.OnClickListener()
		 {
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				customTimerdetails = et.getText().toString();
			}
		 });
		 timerDetails.setView(et);
		AlertDialog dialog = timerDetails.create();
		dialog.show();
		return true;
	}
	private void displayStep()
	{
		ViewGroup rl = (ViewGroup) findViewById(R.id.gallerylayout);
		//ViewGroup steprl = (ViewGroup) findViewById(R.id.steplayout);
		Button startTimer = (Button) findViewById(R.id.start_times);
		ArrayList<Hint> currentHints = recipe.getStep(stepNum).getHints();
		
		boolean contin=true;

		if(timers.size()>=1)
		{
			Button getT = (Button) findViewById(R.id.active_times);
			getT.setEnabled(true);
			getT.setTextColor(getResources().getColor(R.color.yellow));
			getT.setBackgroundResource(R.drawable.blackbutton);
			createTimerDialog();
		}
		//***************************************************************************
		else
		{
			Button getT = (Button) findViewById(R.id.active_times);
			getT.setEnabled(false);
			getT.setTextColor(getResources().getColor(R.color.errorColor));
			getT.setBackgroundResource(R.drawable.greybutton);
		}
		//***************************************************************************
		for(int i=0;i<timers.size();i++)
		{
			if(timers.get(i).getStep().getStepNo()==recipe.getStep(stepNum).getStepNo())
			{
				startTimer.setVisibility(1);
				startTimer.setEnabled(false);
				contin=false;
				startTimer.setTextColor(getResources().getColor(R.color.errorColor));
				startTimer.setBackgroundResource(R.drawable.greybutton);
			}
		}
		if(contin)
		{	
			if(recipe.getStep(stepNum).getTimer()<=0)
			{
				startTimer.setVisibility(1);
				startTimer.setEnabled(false);
				startTimer.setTextColor(getResources().getColor(R.color.errorColor));
				startTimer.setBackgroundResource(R.drawable.greybutton);
			}
			else	
			{
				startTimer.setVisibility(0);
				startTimer.setEnabled(true);
				startTimer.setTextColor(getResources().getColor(R.color.yellow));
				startTimer.setBackgroundResource(R.drawable.blackbutton);
				Toast.makeText(this, "There is a new Timer available touch start timer to start it", Toast.LENGTH_LONG).show();
			}
		}

		//steprl.removeView(hintTv);
		stepTv.setText("Step "+(stepNum+1)+"\n"+recipe.getStep(stepNum).getDetails());
		if(currentHints.size() >0)
		{
			String hints ="Hints \n"+currentHints.get(0).getDetails()+"\n";
			if(currentHints.size()>1)
			{
				for(int i=1;i<currentHints.size();i++)
				{
					hints = hints+currentHints.get(i).getDetails()+"\n";
				}
			}
			hintTv.setText(hints);
			//steprl.addView(hintTv);
		}
		else
		{
			String hints ="Hints \n"+"No hints for this step";
			hintTv.setText(hints);
			//steprl.addView(hintTv);
		}
			iv.setImageResource(0);
			if(recipe.getStep(stepNum).isImageInternal())
			{
				rl.removeAllViews();
				String v = "video";
				if(recipe.getStep(stepNum).getMedia().startsWith(v.substring(0, 4)))
				{
					
					Uri uri = Uri.parse("android.resource://com.example.testapp/drawable/"+recipe.getStep(stepNum).getMedia());
					vv.setVideoURI(uri);					
					vv.setBackgroundColor(Color.TRANSPARENT);
					vv.setBackgroundColor(Color.TRANSPARENT);
					rl.addView(vv);
					vv.start();
				}
				else
				{
					iv.setImageResource(getResources().getIdentifier(recipe.getStep(stepNum).getMedia(), "drawable", getPackageName()));
					rl.addView(iv);
				}
			}
			else
			{	
				rl.removeAllViews();
				String v = "video";
				if(recipe.getStep(stepNum).getMedia().startsWith(v.substring(0, 4)))
				{
					Uri uri = Uri.parse("data/data/com.example.testapp/files/"+recipe.getStep(stepNum).getMedia());
					vv.setVideoURI(uri);					
					vv.setBackgroundColor(Color.TRANSPARENT);
					vv.setBackgroundColor(Color.TRANSPARENT);
					rl.addView(vv);
					vv.start();
				}
				else
				{
					if(recipe.getStep(stepNum).getMedia().compareTo("") == 0)
					{
						iv.setImageResource(getResources().getIdentifier("noimage", "drawable", getPackageName()));
					}
					else
					{
						File filepath = new File(this.getFilesDir().getPath()+"/"+recipe.getStep(stepNum).getMedia());
						Bitmap image = BitmapFactory.decodeFile(filepath.toString());
						int resize = (int) (image.getHeight() * (512.0 / image.getWidth()));
						Bitmap scaledImage = Bitmap.createScaledBitmap(image, 512, resize, true);
						iv.setImageBitmap(scaledImage);
					}
					rl.addView(iv);
				}
				
			}
		//}
			textToSpeechIntent();
	}
	public void nextStep(View view)
	{
		if(stepNum < recipe.getSteps().size()-1)
		{
			stepNum=stepNum+1;
			this.getIntent().putExtra("currentStep", stepNum);
			displayStep();
		}
		else
		{
			if(timers.isEmpty())
				endOfSteps();
			else
				Toast.makeText(this, "Your not finished yet there are still active timers", Toast.LENGTH_LONG).show();
		}
		
	}
	public void previousStep(View view)
	{
		if(stepNum > recipe.getSteps().size()-recipe.getSteps().size())
		{
			stepNum=stepNum-1;
			this.getIntent().putExtra("currentStep", stepNum);
			displayStep();
		}
		
	}
	//*********************************************************************
	private void textToSpeechIntent()
	{
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent,MY_DATA_CHECK_CODE);
	}
	//-------------------------------------------------------------------
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if (requestCode == MY_DATA_CHECK_CODE) 
	    {
	        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) 
	        {
	            mTts = new TextToSpeech(this, this);
	        }
	        else 
	        {
	            // missing data, install it
	        	confirmInstall();
	        }
	    }
	}
	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------
	private void confirmInstall()
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder install = new AlertDialog.Builder(this);
		install.setTitle("Text to speech data missing");
		install.setCancelable(false);
		TextView title = new TextView(this);
		TextView subTitle = new TextView(this);
		title.setText("your device is missing the needed components to run text to speech do you want to install them now?");
		title.setTextSize(20);
		subTitle.setText("this will be downloaded from the android market");
		ll.addView(title,layoutParams);
		ll.addView(subTitle,layoutParams);
		install.setView(ll);
		install.setPositiveButton("Yes",new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
	            Intent installIntent = new Intent();
	            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            startActivity(installIntent);
			}
		});
		install.setNeutralButton("No not right now",null);
		install.setNegativeButton("No, Never show this message again during this session", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				download=false;
			}
		});
		AlertDialog dialog = install.create();
		
		if(download)
			dialog.show();
		
	}
	//---------------------------------------------------------------------------
	@Override
	public void onInit(int arg0)
	{
		if(!vv.isPlaying())
		{
			mTts.speak(recipe.getStep(stepNum).getDetails(), TextToSpeech.QUEUE_FLUSH, null);
			for(Hint hint : recipe.getStep(stepNum).getHints())
			{
				mTts.speak(hint.getDetails(),TextToSpeech.QUEUE_ADD, null);
			}
		}
	}
	
	//*********************************************************************
	// from here onwards everything is to do with the timers of steps
	
	public void getTimers(View view)
	{
		createTimerDialog();
		dialog.show();
	}
	public void setTimer(View view)
	{
		Button getT = (Button) findViewById(R.id.active_times);
		getT.setEnabled(true);
		
		Steptimer stepTimer = new Steptimer(recipe.getStep(stepNum).getTimer(),this.getBaseContext(),this,recipe.getStep(stepNum));
		timers.add(stepTimer);
		
		createTimerDialog();
		displayStep();
	}
	private void createTimerDialog()
	{
		final LinearLayout ll = new LinearLayout(getBaseContext());
		ll.setOrientation(1);
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);

		final AlertDialog.Builder timersDialog = new AlertDialog.Builder(this);
		timersDialog.setTitle("Active Timers");	

		final Timer runner = new Timer();
		runner.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run() 
			{	
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						for(int i=0;i<timers.size();i++)
						{
							if(!timers.get(i).getIsRunning())
								timers.remove(i);
						}
						
						ll.removeAllViews();
						TextView timeLeft;
						TextView timerStepDetails;
						TextView timerStepHints;
						for(int i=0;i<timers.size();i++)
						{
							timerStepDetails=new TextView(getBaseContext());
							timerStepDetails.setTextSize(18);
							timerStepDetails.setText(timers.get(i).getStep().getDetails());
							ll.addView(timerStepDetails,layoutParams);
							
							if(timers.get(i).getStep().getHints().size()>0)
							{
								Step tempStep =timers.get(i).getStep();
								for(int t=0;t<tempStep.getHints().size();t++)
								{
									timerStepHints=new TextView(getBaseContext());
									timerStepHints.setTextSize(18);
									timerStepHints.setText(tempStep.getHint(t).getDetails());
									ll.addView(timerStepHints,layoutParams);
								}
							}
							timeLeft = new TextView(getBaseContext());
							timeLeft.setText(timeRemaining(i));
							ll.addView(timeLeft,layoutParams);
						}
						timersDialog.setView(ll);
					}
				});
			}
		},0,1000);
		
		timersDialog.setNegativeButton("close",  new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				d.cancel();
				runner.cancel();
			}
		});	
		ll.removeAllViews();
		TextView timeLeft;
		TextView timerStepDetails;
		TextView timerStepHints;
		for(int i=0;i<timers.size();i++)
		{
			timerStepDetails=new TextView(this);
			timerStepDetails.setTextSize(18);
			timerStepDetails.setText(timers.get(i).getStep().getDetails());
			ll.addView(timerStepDetails,layoutParams);
			
			if(timers.get(i).getStep().getHints().size()>0)
			{
				Step tempStep =timers.get(i).getStep();
				for(int t=0;t<tempStep.getHints().size();t++)
				{
					timerStepHints=new TextView(this);
					timerStepHints.setTextSize(18);
					timerStepHints.setText(tempStep.getHint(t).getDetails());
					ll.addView(timerStepHints,layoutParams);
				}
			}		
			timeLeft = new TextView(this);
			timeLeft.setText(timeRemaining(i));
			ll.addView(timeLeft,layoutParams);
		}
		timersDialog.setView(ll);
		dialog = timersDialog.create();
	}
	private String timeRemaining(int i)
	{
		String time;
		if(timers.get(i).getTimeMinsLeft()>1)
		{
			if(timers.get(i).getTimeSecsLeft()>1)
			{
				time = "Time Remaining: "+Integer.toString(timers.get(i).getTimeMinsLeft())+" minutes and "
						+ Integer.toString(timers.get(i).getTimeSecsLeft())+" seconds.";
			}
			else
			{
				time = "Time Remaining: "+Integer.toString(timers.get(i).getTimeMinsLeft())+" minutes and "
						+ Integer.toString(timers.get(i).getTimeSecsLeft())+" second.";
			}
		}
		else
		{
			if(timers.get(i).getTimeSecsLeft()>1)
			{
				time = "Time Remaining: "+Integer.toString(timers.get(i).getTimeMinsLeft())+" minute and "
						+ Integer.toString(timers.get(i).getTimeSecsLeft())+" seconds.";
			}
			else
			{
				time = "Time Remaining: "+Integer.toString(timers.get(i).getTimeMinsLeft())+" minute and "
						+ Integer.toString(timers.get(i).getTimeSecsLeft())+" second.";
			}
		}
		return time;
	}
	@Override
	public void onBackPressed() 
	{
		if(stepNum==0)
			exitDialog();
		else
		{
			stepNum=stepNum-1;
			displayStep();
		}
	}
	private void exitDialog()
	{
	    new AlertDialog.Builder(this)
        .setTitle("Closing Activity")
        .setMessage("Are you sure you want to stop cooking?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) 
        {
        	 new AlertDialog.Builder(RecipeSteps.this)
 	        .setTitle("Reminder")
 	        .setMessage("Please turn off any cooking devices and remove any food, remember safety first :-)")
 	        .setPositiveButton("OK",new DialogInterface.OnClickListener()
 	        {
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					for(Steptimer temp : timers)
					{
						temp.cancelTimer();
					}
					finish();
				}
 	        }).show();
        	
        }

    }).setNegativeButton("No", null).show();
	}
//----------------------------------------------------------------------------
	private void endOfSteps()
	{
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;

		AlertDialog.Builder enjoy = new AlertDialog.Builder(this);
		enjoy.setCancelable(false);
		enjoy.setTitle("Congratulations");
		TextView tvMsg = new TextView(this);
		tvMsg.setTextSize(20);
		tvMsg.setText("Well done you have finished :-) get your fork and knife and enjoy your perfect meal");
		ImageView iv = new ImageView(this);
		
		if(recipe.getStep(stepNum).isImageInternal())
		{
			iv.setImageResource(getResources().getIdentifier(recipe.getStep(stepNum).getMedia(), "drawable", getPackageName()));
			Log.d("yes", "Image internal");
		}
		else
		{	
			Log.d("no", "Image external");
			if(recipe.getStep(stepNum).getMedia().compareTo("") == 0)
			{
				iv.setImageResource(getResources().getIdentifier("noimage", "drawable", getPackageName()));
			}
			else
			{
				File filepath = new File(this.getFilesDir().getPath()+"/"+recipe.getImg());
				Bitmap image = BitmapFactory.decodeFile(filepath.toString());
				int resize = (int) (image.getHeight() * (512.0 / image.getWidth()));
				Bitmap scaledImage = Bitmap.createScaledBitmap(image, 512, resize, true);
				iv.setImageBitmap(scaledImage);
			}
		}
		ll.addView(tvMsg,layoutParams);
		ll.addView(iv,layoutParams);
		enjoy.setView(ll);
		enjoy.setPositiveButton("I Most Definitely Will", null);
		
		AlertDialog dialog = enjoy.create();
		dialog.show();
	}
}
