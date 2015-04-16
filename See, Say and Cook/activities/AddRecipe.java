package com.example.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Category;
import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Hint;
import com.example.testapp.Ingredient;
import com.example.testapp.Quantity;
import com.example.testapp.R;
import com.example.testapp.Recipe;
import com.example.testapp.Step;
import com.example.testapp.TimeSelectDialog;

public class AddRecipe extends Activity implements OnMenuItemClickListener
{
	private DatabaseHandler db = new DatabaseHandler(this);
	private final int SELECT_PHOTO = 100;
	private int whereFrom,stepPos,hintSelection,stepTimer;
	private static String recImgName,recImgDir;
	private static ArrayList<String> stepImgName = new ArrayList<String>();
	private static ArrayList<String> stepImgDir = new ArrayList<String>();
	private static ArrayList<Ingredient> chosenIngredients = new ArrayList<Ingredient>();
	private static ArrayList<String> qType = new ArrayList<String>();
	private static ArrayList<Float> amount = new ArrayList<Float>();	
	private static ArrayList<Step> steps = new ArrayList<Step>();
	private static ArrayList<Hint> stepHints = new ArrayList<Hint>();
	private AlertDialog.Builder existingIng; 
//--------------------------------------------------------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		existingIng = new AlertDialog.Builder(this);
		setContentView(com.example.testapp.R.layout.add_recipe);	
		hintSelection=-1;
		createIngredientRelatedD();
		
		if(recImgDir !=null)
			if(recImgDir.compareTo("")<0)
				getFile(recImgDir);
		
		addStepsToView();
		addIngredientToView();
	}
	//----------------------------------------------------	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.add_recipe_menu, menu);
		    return true;
		}	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			GeneralDialogs temp = new GeneralDialogs(this);
			switch(item.getItemId())
			{
				case R.id.about:
					temp.about();
				return true;
				case R.id.add_recipe_help:
					temp.help(4);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
//------------------------------------------------------------------------------------------------------------------------	
	//checks if the recipe name is already in use
	
	private boolean checkNameToDb(String recipeName)
	{
		for(int i=0;i<db.getAllRecipes().size()-1;i++)
		{
			if(recipeName.toLowerCase(Locale.US).compareTo(db.getRecipe(i+1).getName().toLowerCase(Locale.US))==0)
				return false;
		}
		return true;
	}
//------------------------------------------------------------------------------------------------------------------------
	private void selectCategory(final Recipe recipe)
	{
		final ArrayList<Category> cats = db.getCategoryList();
		final ArrayList<Integer> chosenCategorys = new ArrayList<Integer>();
	
		AlertDialog.Builder categories = new AlertDialog.Builder(this);
		categories.setTitle("What Group will this recipe belong to?");
		categories.setCancelable(false);
		
		ArrayList<String> catNames = new ArrayList<String>();
		
		for(Category temp : cats)
			catNames.add(temp.getName());
		
		CharSequence[] items = catNames.toArray(new CharSequence[catNames.size()]);
	    boolean[] itemsChecked = new boolean[items.length];
	    
		categories.setMultiChoiceItems(items,itemsChecked,new DialogInterface.OnMultiChoiceClickListener() 
		{
			@Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			{
				 if(isChecked)
				 {
					 chosenCategorys.add(cats.get(which).getID());
				 }
				 else
					 if(!isChecked)
						for(int i=0;i<chosenCategorys.size();i++)
						{
							if(cats.get(which).getID()==chosenCategorys.get(i))
								chosenCategorys.remove(i);
						}
			}
		});
		categories.setPositiveButton("Finished", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				for(int i=0;i<chosenCategorys.size();i++)
				{
					for(Category cat : cats)
					{
						if(cat.getID()==chosenCategorys.get(i))
						{
							Log.d("name", cat.getName());
							Log.d("id", ""+cat.getID());
							recipe.addCategory(cat);
						}
					}
				}
				finish(recipe);
			}
		});
        AlertDialog build = categories.create();
        build.show();
	}
//------------------------------------------------------------------------------------------------------------------------	
	//first runs through the EditText views and make sure 
	//that all EditText views contain values
	//if not alert user, else create and goto selectCategory
	public void finalize(View view)
	{
		EditText E1 = (EditText) findViewById(R.id.rec_name);
		EditText E2 = (EditText) findViewById(R.id.rec_descript);
		ImageView iv = (ImageView) findViewById(R.id.add_recipe_display_img);
		iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		String recName,description;	
		recName = E1.getText().toString();
		description = E2.getText().toString();

		if(recName.matches("") || recName.matches(" "))
			Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
		else
			if(!checkNameToDb(recName))
				Toast.makeText(this, "That recipe name is already in use", Toast.LENGTH_SHORT).show();
			else
				if(description.matches("") || description.matches(" "))
					Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
				else
					if(chosenIngredients.size() <=0)
						Toast.makeText(this, "you must have ingredients you can't make something out of nothing :-P", Toast.LENGTH_SHORT).show();
					else
						if(steps.size() <=0)
							Toast.makeText(this, "Whats the point of a recipe without instructions?", Toast.LENGTH_SHORT).show();
						else
						{
							Recipe rec;
							Quantity.MeasureType mTemp;
							
							if(null!=iv.getDrawable())
						    {
								copyRecipeImageFile(recImgDir,recImgName);											//copy file to internal storage
								rec = new Recipe(0,recName,description,recImgName,false,1,1,0);
						    }
							else
							{
								rec = new Recipe(0,recName,description,"",false,1,1,0);
						    }
							for(int i=0;i<chosenIngredients.size();i++)
							{
								mTemp = Quantity.MeasureType.valueOf(qType.get(i));
								rec.addQu(new Quantity(0,rec.getID(),chosenIngredients.get(i).getID(),mTemp,amount.get(i)
										,chosenIngredients.get(i).getName(),chosenIngredients.get(i).getImg(),true));
							}
							for(int t=0;t<stepImgDir.size();t++)
								copyStepImageFile(stepImgDir.get(t),stepImgName.get(t));
							
							for(int i=0;i<steps.size();i++)
								rec.addStep(steps.get(i));

							selectCategory(rec);
						}
		
	}
//-------------------------------------------------------------------------------------------------------------------------
	private void finish(Recipe recipe)
	{
		db.addRecipe(recipe);
		recImgName=null;
		recImgDir=null;
		stepImgName.removeAll(stepImgName);
		stepImgDir.removeAll(stepImgDir);
		chosenIngredients.removeAll(chosenIngredients);
		amount.removeAll(amount);
		steps.removeAll(steps);
		stepHints.removeAll(stepHints);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
//--------------------------------------------------------------------------------------------------------------------------	
	//calls function onActivityResult()
	public void selectImage(View view)
	{
		Toast.makeText(this, "Select a Picture that will represent this recipe", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		whereFrom =1;
		startActivityForResult(intent,SELECT_PHOTO);
	}
//--------------------------------------------------------------------------------------------------------------------------	
	
	//prompt the user to select an image from gallery 
	//then pass the directory and name to function copyFile()
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent imageReturn)
	{
		String[] acceptedVideoFormats = {"mp4","3gp","ts","webm","mkv"};
		super.onActivityResult(requestCode, resultCode,imageReturn);
		switch(requestCode)
		{
			case SELECT_PHOTO:
				if(resultCode == RESULT_OK)
				{
					Uri selectedImage = imageReturn.getData();
			        String[] filePathColumn = {MediaStore.Images.Media.DATA};
			      
			        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			        cursor.moveToFirst(); 
			        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			        if(whereFrom==1)
			        {
				        recImgDir = cursor.getString(columnIndex);
				        File f = new File(recImgDir);
				        recImgName = f.getName().trim();
				        getFile(recImgDir);
				        
			        }
			        if(whereFrom==2)
			        {
			        	stepImgDir.add(cursor.getString(columnIndex));
			        	File f = new File(stepImgDir.get(stepImgDir.size()-1));
			        	String mediaName = f.getName().trim();
			        	for(int i=0;i<acceptedVideoFormats.length;i++)
			        	{
			        		if(f.getName().substring(f.getName().lastIndexOf('.')+1).trim().compareTo(acceptedVideoFormats[i])==0)
			        			mediaName = "video"+f.getName().trim();
			        	}
			        	stepImgName.add(mediaName);
			        }
			        if(whereFrom==3)
			        {
			        	stepImgDir.set(stepPos,cursor.getString(columnIndex));
			        	File f = new File(stepImgDir.get(stepPos));
			        	
			        	String mediaName = f.getName().trim();
			        	for(int i=0;i<acceptedVideoFormats.length;i++)
			        	{
			        		if(f.getName().substring(f.getName().lastIndexOf('.')+1).trim().compareTo(acceptedVideoFormats[i])==0)
			        			mediaName = "video"+f.getName().trim();
			        	}
			        	
			        	stepImgName.set(stepPos,mediaName);
			        }
			        cursor.close();
				}
		}
	}
//------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------	
	
	// copy the selected file from its current location to the applications internal storage
	// internal path == data/data/com.example.testapp/files
	private boolean copyRecipeImageFile(String frm,String fileName)
	{
		try
		{
			int byteRead =0;			
			InputStream in = new FileInputStream(frm);
		    FileOutputStream out = openFileOutput(fileName,Context.MODE_PRIVATE);
		    byte[] buffer = new byte[1024];
		  
		    while((byteRead = in.read(buffer)) != -1)
		    {
		        out.write(buffer, 0, byteRead);
		    }
		    in.close();
		    out.close();
		 
		    return true;
		}
		catch(Exception e)
		{
			return false;
		}
    }
//--------------------------------------------------------------------------------------------------------------------------
	private boolean copyStepImageFile(String frm,String fileName)
	{
		try
		{
			int byteRead =0;			
			InputStream in = new FileInputStream(frm);
		    FileOutputStream out = openFileOutput(fileName,Context.MODE_PRIVATE);
		    byte[] buffer = new byte[1024];
		  
		    while((byteRead = in.read(buffer)) != -1)
		    {
		        out.write(buffer, 0, byteRead);
		    }
		    in.close();
		    out.close();
		 
		    return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//--------------------------------------------------------------------------------------------------------------------------	
	//Gets the chosen image from internal storage 
	//then changes the displayContent of ImageView display_img
	//to that image
	private void getFile(String sdDir)
	{
		ImageView iv = (ImageView) findViewById(R.id.add_recipe_display_img);
		File filepath = new File(sdDir);
		Bitmap image = BitmapFactory.decodeFile(filepath.toString());
		int resize = (int) (image.getHeight() * (512.0 / image.getWidth()));
		Bitmap scaledImage = Bitmap.createScaledBitmap(image, 512, resize, true);
		iv.setImageBitmap(scaledImage);
	}
//--------------------------------------------------------------------------------------------------------------------------
	public void addIngredient(View view)
	{	
		Toast.makeText(this, "Add the ingredients that will be used in your recipe", Toast.LENGTH_LONG).show();
        AlertDialog alert11 = existingIng.create();
        alert11.show();
	}
//--------------------------------------------------------------------------------------------------------------------------
	public void addStep(View view)
	{
		Toast.makeText(this, "Here is where you add a step and any hints for that step to your recipe", Toast.LENGTH_LONG).show();
		addStepDialog();
	}
//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		return false;
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void addIngredientToView()
	{
		Spinner currentIngredients = (Spinner) findViewById(R.id.ing_list);
		currentIngredients.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos!=0)
				{
					existingIng.show();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
			
		});
		ArrayList<String> stepNoAndDetails= new ArrayList<String>();
		stepNoAndDetails.add("Current Ingredients");
		if(!chosenIngredients.isEmpty())
		{
			for(int i=0;i<chosenIngredients.size();i++)
				stepNoAndDetails.add(chosenIngredients.get(i).getName()+": "+qType.get(i).toString()+" "+amount.get(i).toString());
		}
		
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stepNoAndDetails);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currentIngredients.setAdapter(temp);

	}
//--------------------------------------------------------------------------------------------------------------------------
	private void addStepsToView()
	{
		Spinner stepSpinner = (Spinner) findViewById(R.id.steps);
		stepSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos!=0)
				{
					updateStep(steps.get(pos-1),pos-1);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
			
		});
		ArrayList<String> stepNoAndDetails= new ArrayList<String>();
		stepNoAndDetails.add("Current Steps");
		if(!steps.isEmpty())
		{
			for(Step step : steps)
				stepNoAndDetails.add("Step "+step.getStepNo()+": "+step.getDetails());
		}
		
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stepNoAndDetails);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stepSpinner.setAdapter(temp);
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void createIngredientRelatedD()
	{

		final AlertDialog.Builder MT = new AlertDialog.Builder(this);
		MT.setTitle("Select Measurement Type");
		CharSequence[] types = {"tbsp", "tsp", "cup", "quart", "pint", "ml", "g", "Whole", "Pinch", "Cloves", "NOTYPE"};
		
		MT.setSingleChoiceItems(types,-1,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int choice)
			{
				switch(choice)
				{
				case 0:
					qType.add("tbsp");
					break;
				case 1:
					qType.add("tsp");
					break;
				case 2:
					qType.add("cup");
					break;
				case 3:
					qType.add("quart");
					break;
				case 4:
					qType.add("pint");
					break;
				case 5:
					qType.add("ml");
					break;
				case 6:
					qType.add("g");
					break;
				case 7:
					qType.add("Whole");
					break;
				case 8:
					qType.add("Pinch");
					break;
				case 9:
					qType.add("Cloves");
					break;
				case 10:
					qType.add("NOTYPE");
					break;
				}
					dialog.cancel();
					enterAmountDialog();
			} 
		});
		
		ArrayList<String> names = new ArrayList<String>();
		for(int i=0;i<db.getAllIngredients().size();i++)
		{
			names.add(db.getAllIngredients().get(i).getName());
		}
		
		CharSequence[] items = names.toArray(new CharSequence[names.size()]);
	    boolean[] itemsChecked = new boolean[items.length];
		existingIng.setTitle("Select ingredient from existing ingredients?");
		existingIng.setCancelable(true);
		existingIng.setMultiChoiceItems(items,itemsChecked,new DialogInterface.OnMultiChoiceClickListener()
		{
			 @Override
             public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			 {
				 which = which+1;
				 if(isChecked)
				 {
					 chosenIngredients.add(db.getIngredient(which));
					 AlertDialog dialogtoDisplay = MT.create();
					 dialogtoDisplay.show();
				 }
				 else
				 if(!isChecked)
				 {
					 Ingredient ig = db.getIngredient(which);
					 for(int i=0;i<chosenIngredients.size();i++)
					 {
						 if(ig.getID()== chosenIngredients.get(i).getID())
						 {
							 chosenIngredients.remove(i);
							 qType.remove(i);
							 amount.remove(i);
						 }
					 }
				 }

             }
			 
		});
		existingIng.setPositiveButton("Continue",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
                dialog.cancel();
                addIngredientToView();
            }
        });	
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void enterAmountDialog()
	{
		AlertDialog.Builder ingAmount = new AlertDialog.Builder(this);
		ingAmount.setMessage("Enter amount");	
		ingAmount.setCancelable(false);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		ingAmount.setView(input);
		ingAmount.setPositiveButton("Continue", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(input.getText().toString().compareTo("")==0 || input.getText().toString().compareTo(".")==0)
				{
					dialog.cancel();
					Toast.makeText(getBaseContext(), "Please enter a valid ingredient amount", Toast.LENGTH_SHORT).show();
					enterAmountDialog();
				}
				else
				{
					dialog.cancel();
					amount.add(Float.valueOf(input.getText().toString()));
				}
			}	
		});
		
		AlertDialog dialog = ingAmount.create();
		dialog.show();
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void addStepDialog()
	{
		stepTimer=0;
		final int currentSize =stepImgName.size(); 
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;

		final AlertDialog.Builder buildStep = new AlertDialog.Builder(this);
		buildStep.setTitle("Recipe steps");
		TextView stepNum = new TextView(this);
		stepNum.setText("Step: "+(steps.size()+1));
		stepNum.setTextSize(20);
		final EditText stepDetails = new EditText(this);
		stepDetails.setHint("What is done in this step?");
		stepDetails.setGravity(Gravity.CENTER);
		
		final TextView timerTime = new TextView(this);
		timerTime.setHint("No Timer");
		timerTime.setTextSize(16);
		
		Button setTimer = new Button(this);
		setTimer.setText("Add a timer?");
		setTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				final TimeSelectDialog timer = new TimeSelectDialog();
				timer.show(getFragmentManager(), "timer");
				final Timer updater = new Timer();
				updater.scheduleAtFixedRate(new TimerTask()
				{
					@Override
					public void run() 
					{	
						AddRecipe.this.runOnUiThread(new Runnable()
						{
							@Override
							public void run() 
							{
								if(timer.getTime().compareTo("No Timer")!=0)
								{	
									timerTime.setText(timer.getTime());
									stepTimer=timer.getTimeInSeconds();
									updater.cancel();
									updater.purge();
								}
							}
						});
					}
				},0,500);
			}	
		});
		final Button addHint = new Button(this);
		addHint.setText("Add a Hint?");
		addHint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				addStepHintDialog(stepHints.size()+1);
			}	
		});
		TextView warning = new TextView(this);
		warning.setHint("Warning large videos may take a while to copy");
		Button stepImg = new Button(this);
		stepImg.setText("Add a picture or video for this step?");
		stepImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/* video/*");
				whereFrom=2;
				startActivityForResult(intent,SELECT_PHOTO);
			}
			
		});
		Button currentHints = new Button(this);
		currentHints.setText("Current Hints");
		currentHints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(stepHints.size()<=0)
					Toast.makeText(getBaseContext(), "This step currently has no hints", Toast.LENGTH_SHORT).show();
				else
					currentHintsDialog(stepHints);
			}
		});
		
		ll.addView(stepNum,layoutParams);
		ll.addView(stepDetails,layoutParams);
		ll.addView(setTimer,layoutParams);
		ll.addView(timerTime,layoutParams);
		ll.addView(stepImg,layoutParams);
		ll.addView(warning,layoutParams);
		ll.addView(addHint,layoutParams);
		ll.addView(currentHints,layoutParams);
		buildStep.setView(ll);
		
		buildStep.setPositiveButton("finished", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int t) 
			{
				String imgName = "";
				Step step;
				
				if(currentSize < stepImgName.size())
					imgName= stepImgName.get(stepImgName.size()-1);
				else
				{
					stepImgName.add(imgName);
					stepImgDir.add(imgName);
				}				
				step = new Step(0,0,stepDetails.getText().toString(),imgName,steps.size()+1,false,stepTimer);
				if(stepHints.size()>0)
				{
					step.setHints(stepHints);
					stepHints.removeAll(stepHints);
				}
				steps.add(step);
				addStepsToView();
				d.cancel();
			}
			
		});
		buildStep.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}			
		});
		AlertDialog dialog = buildStep.create();
		dialog.show();
	}
//--------------------------------------------------------------------------------------------------------------------------	
	private void addStepHintDialog(final int num)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder buildHint = new AlertDialog.Builder(this);
		buildHint.setTitle("Step hint");
		final TextView hintNum = new TextView(this);
		hintNum.setText("Hint: "+num);
		hintNum.setTextSize(20);
		final EditText hintDetails = new EditText(this);
		hintDetails.setHint("Hint details");
		buildHint.setPositiveButton("finished", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1) 
			{
				stepHints.add(new Hint(0,steps.size()+1,hintDetails.getText().toString(),num));
				dialog.cancel();
			}
			
		});
		buildHint.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();

			}			
		});
		ll.addView(hintNum,layoutParams);
		ll.addView(hintDetails,layoutParams);
		buildHint.setView(ll);
		
		AlertDialog dialog = buildHint.create();
		dialog.show();
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void updateStep(final Step oldStep,final int pos)
	{
		stepTimer=oldStep.getTimer();
		stepHints = oldStep.getHints();
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder stepUpdate = new AlertDialog.Builder(this);
		stepUpdate.setTitle("Modifiy Step");
		
		final TextView stepNum = new TextView(this);
		stepNum.setText("Step: "+oldStep.getStepNo());
		stepNum.setTextSize(20);
		
		final EditText stepDetails = new EditText(this);
		stepDetails.setText(oldStep.getDetails());
		stepDetails.setGravity(Gravity.CENTER);
		
		final TextView timerTime = new TextView(this);
		timerTime.setHint("No Timer");
		timerTime.setTextSize(16);
		timerTime.setText("The current timers length is. "+oldStep.getTimer()/3600+":"+(oldStep.getTimer()%3600)/60
				+":"+oldStep.getTimer()%60);
		
		Button setTimer = new Button(this);
		setTimer.setText("Change Timer?");
		setTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				final TimeSelectDialog timer = new TimeSelectDialog();
				timer.show(getFragmentManager(), "timer");
				final Timer updater = new Timer();
				updater.scheduleAtFixedRate(new TimerTask()
				{
					@Override
					public void run() 
					{	
						AddRecipe.this.runOnUiThread(new Runnable()
						{
							@Override
							public void run() 
							{
								if(timer.getTime().compareTo("No Timer")!=0)
								{	
									timerTime.setText(timer.getTime());
									stepTimer=timer.getTimeInSeconds();
									updater.cancel();
									updater.purge();
								}
							}
						});
					}
				},0,500);
			}	
		});
		
		final Button addHint = new Button(this);
		addHint.setText("Add a Hint?");
		addHint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				addStepHintDialog(oldStep.getHints().size()+1);
			}	
		});
		TextView warning = new TextView(this);
		warning.setHint("Warning large videos may take a while to copy");
		Button stepImg = new Button(this);
		stepImg.setText("Chose another picture or video for this step?");
		stepImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				stepPos=pos;
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/* video/*");
				whereFrom=3;
				startActivityForResult(intent,SELECT_PHOTO);
			}
			
		});
		
		Button currentHints = new Button(this);
		currentHints.setText("Current Hints");
		currentHints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!stepHints.isEmpty())
					currentHintsDialog(oldStep.getHints());
				else
					Toast.makeText(getBaseContext(), "This step currently has no hints", Toast.LENGTH_SHORT).show();
			}
		});
		
		ll.addView(stepNum,layoutParams);
		ll.addView(stepDetails,layoutParams);
		ll.addView(setTimer,layoutParams);
		ll.addView(timerTime,layoutParams);
		ll.addView(stepImg,layoutParams);
		ll.addView(warning,layoutParams);
		ll.addView(addHint,layoutParams);
		ll.addView(currentHints,layoutParams);
		stepUpdate.setView(ll);
		stepUpdate.setPositiveButton("update", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String imgName= stepImgName.get(pos);
				Step newStep = new Step(oldStep.getID(),oldStep.getRecID(),stepDetails.getText().toString(),imgName,oldStep.getStepNo(),oldStep.isImageInternal(),stepTimer);
				newStep.setHints(stepHints);
				steps.set(pos,newStep);
				addStepsToView();
				stepHints.removeAll(stepHints);
			}	
		});
		stepUpdate.setNeutralButton("Delete Step", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				steps.remove(pos);
				for(int i=pos;i<steps.size();i++)
				{
					steps.get(i).setStepNo(steps.get(i).getStepNo()-1);
				}
				stepHints.removeAll(stepHints);
				dialog.cancel();
				addStepsToView();
			}
			
		});
		stepUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				stepHints.removeAll(stepHints);
				d.cancel();
				addStepsToView();
			}
		});
		
		AlertDialog dialog = stepUpdate.create();
		dialog.show();
		
	}

//------------------------------------------------------------------------------------------------------------------
	private void currentHintsDialog(final ArrayList<Hint> tempHints)
	{
		stepHints = tempHints;
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder currentStepHints = new AlertDialog.Builder(this);
		currentStepHints.setTitle("Current Hints");
		
		
		RadioGroup rg = new RadioGroup(this);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int currentHintsDialog) 
			{
				hintSelection = currentHintsDialog-1000;
			}
		});
		rg.removeAllViews();
		for(int i=0;i<tempHints.size();i++)
		{
			RadioButton rb = new RadioButton(this);
			rb.setText("Hint "+(tempHints.get(i).getHintNo())+": "+tempHints.get(i).getDetails());
			rb.setId(i+1000);
			rg.addView(rb,layoutParams);
		}
		
		ll.addView(rg,layoutParams);
		currentStepHints.setView(ll);
		currentStepHints.setPositiveButton("Edit", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				if(hintSelection>=0)
				{
					d.cancel();
					updateHint(tempHints.get(hintSelection),hintSelection);
					hintSelection=-1;
				}
				else
					Toast.makeText(getBaseContext(), "No hint has been selected", Toast.LENGTH_SHORT).show();
			}	
		});
		currentStepHints.setNegativeButton("Dismiss", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				d.cancel();
			}
		});
		AlertDialog dialog = currentStepHints.create();
		dialog.show();		
	}
//--------------------------------------------------------------------------------------------------------------------------
	private void updateHint(final Hint oldHint,final int pos)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder hintUpdate = new AlertDialog.Builder(this);
		hintUpdate.setTitle("Modifiy Hint");
		
		final TextView hintNum = new TextView(this);
		hintNum.setText("Hint: "+oldHint.getHintNo());
		hintNum.setTextSize(20);
		
		final EditText hintDetails = new EditText(this);
		hintDetails.setText(oldHint.getDetails());
		
		
		ll.addView(hintNum,layoutParams);
		ll.addView(hintDetails,layoutParams);
		hintUpdate.setView(ll);
		hintUpdate.setPositiveButton("update", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				stepHints.set(pos, new Hint(oldHint.getHintID(),oldHint.getHintStepID(),hintDetails.getText().toString(),oldHint.getHintNo()));
			}	
		});
		hintUpdate.setNeutralButton("delete", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				stepHints.remove(pos);
				for(int i=pos;i<stepHints.size();i++)
				{
					stepHints.get(i).setHintNo(stepHints.get(i).getHintNo()-1);
				}
				dialog.cancel();
			}
			
		});
		hintUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				d.cancel();
			}
		});
		
		AlertDialog dialog = hintUpdate.create();
		dialog.show();
	}
}	



