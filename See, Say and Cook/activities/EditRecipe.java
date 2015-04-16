package com.example.activities;
/*
 * TODO
 * if recipe has different name promote user if they want to add new recipe or update the existing recipe
 * if a step,quanitiy or hint is deleted add it to the corosponding deletedArrayList.
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Hint;
import com.example.testapp.Ingredient;
import com.example.testapp.Quantity;
import com.example.testapp.R;
import com.example.testapp.Recipe;
import com.example.testapp.Step;
import com.example.testapp.TimeSelectDialog;

public class EditRecipe extends Activity 
{
	private DatabaseHandler db = new DatabaseHandler(this);
	private static Recipe chosenRecipe;
	private static int chosenRecipeID;
	private static ArrayList<Step> steps = new ArrayList<Step>();
	private static ArrayList<Step> removedSteps = new ArrayList<Step>();
	private static ArrayList<Hint> stepHints = new ArrayList<Hint>();
	private int stepTimer,stepPos,whereFrom;
	private final int SELECT_PHOTO = 100;
	private static String recImgName,recImgDir;
	private static ArrayList<String> stepImgName = new ArrayList<String>();
	private static ArrayList<String> stepImgDir = new ArrayList<String>();
	private static ArrayList<Ingredient> chosenIngredients = new ArrayList<Ingredient>();
	private static ArrayList<Ingredient> allDBIngredients = new ArrayList<Ingredient>(); 
	private static ArrayList<Quantity> chosenIngredientsQuanitity = new ArrayList<Quantity>();
	private static ArrayList<Quantity> previousIngredients = new ArrayList<Quantity>();
	private static ArrayList<Quantity> deletedIngredients = new ArrayList<Quantity>();
	private static ArrayList<String> qType = new ArrayList<String>();
	private static ArrayList<Float> amount = new ArrayList<Float>();	
	private AlertDialog.Builder existingIng; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(com.example.testapp.R.layout.edit_recipe);
		existingIng = new AlertDialog.Builder(this);
		allDBIngredients = db.getAllIngredients();
		
		if(chosenRecipe ==null)
			selectRecipeDialog();
		else
			loadRecipe(chosenRecipeID);
			
	}
	//----------------------------------------------------	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.edit_recipe_menu, menu);
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
				case R.id.edit_recipe_help:
					temp.help(5);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
	//-------------------------------------------------------
//----------------------------------------------------------------------------------------------------------
	public void selectRecipe(View view)
	{
		selectRecipeDialog();
	}
//----------------------------------------------------------------------------------------------------------
	private void selectRecipeDialog()
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		
		final ArrayList<Recipe> recipes = db.getAllRecipes();
		
		RadioGroup rg = new RadioGroup(this);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int recipe)
			{
				chosenRecipeID = recipes.get(recipe-1).getID();
			}
		});
		for(int i=0;i<recipes.size();i++)
		{
			RadioButton rb = new RadioButton(this);
			rb.setText(recipes.get(i).getName());
			rb.setId(i+1);
			rg.addView(rb);
		}
		AlertDialog.Builder recipeUpdate = new AlertDialog.Builder(this);
		recipeUpdate.setTitle("Select Recipe To Edit");
		recipeUpdate.setPositiveButton("Select", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				if(chosenRecipeID<=0)
					Toast.makeText(getBaseContext(), "Please select a recipe", Toast.LENGTH_SHORT).show();
				else
				{
					dialog.cancel();
					loadRecipe(chosenRecipeID);
				}
			}	
		});
		recipeUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1) 
			{
				dialog.cancel();
			}
		});
		ll.addView(rg);
		recipeUpdate.setView(ll);
		
		AlertDialog dialog = recipeUpdate.create();
		dialog.show();
	}
//---------------------------------------------------------------------------------------------------------
	private void deleteIngredient(final Quantity ing,final int pos,final Spinner spinner)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		
		AlertDialog.Builder deleteIngredientConfirm = new AlertDialog.Builder(this);
		deleteIngredientConfirm.setTitle("Delete?");
		TextView tv = new TextView(this);
		tv.setText("Do you want to delete this ingredient?");
		
		deleteIngredientConfirm.setPositiveButton("Delete", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				deletedIngredients.add(ing);
				int id =chosenIngredientsQuanitity.get(pos).getIngID();
				chosenIngredientsQuanitity.remove(pos);
				for(int i=0;i<chosenIngredients.size();i++)
				{ 
					if(id==chosenIngredients.get(i).getID())
					{
						chosenIngredients.remove(i);
						qType.remove(i);
						amount.remove(i);
					}
				}
				spinner.setAdapter(getIngredients());
			}	
		});
		deleteIngredientConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1) 
			{
				dialog.cancel();
			}
		});
		ll.addView(tv,layoutParams);
		deleteIngredientConfirm.setView(ll);
		
		AlertDialog dialog = deleteIngredientConfirm.create();
		dialog.show();
	}
//----------------------------------------------------------------------------------------------------------
	private void loadRecipe(int rec)
	{
		chosenRecipe = db.getRecipe(rec);
		steps = chosenRecipe.getSteps();
		recImgName = chosenRecipe.getImg();
		for(int i=0;i<chosenRecipe.getSteps().size();i++)
		{
			stepImgName.add(steps.get(i).getMedia());
			stepImgDir.add(this.getFilesDir().getPath());
		}
		chosenIngredientsQuanitity.removeAll(chosenIngredientsQuanitity);
		chosenIngredients.removeAll(chosenIngredients);
		deletedIngredients.removeAll(deletedIngredients);
		
		previousIngredients = chosenRecipe.getQus();
		
		ImageView stepBack = (ImageView) findViewById(R.id.stepListback);
		ImageView ingBack = (ImageView) findViewById(R.id.ingredientListback);
		EditText recipeName = (EditText) findViewById(R.id.recipeName);
		EditText recipeDescription = (EditText) findViewById(R.id.recipeDescription);
		TextView nameTitle = (TextView) findViewById(R.id.editRecipeTextView1);
		TextView descriptTitle = (TextView) findViewById(R.id.editRecipeTextView2);
		Button restButton = (Button) findViewById(R.id.editRecipeReset);
		Button contin = (Button) findViewById(R.id.editRecipeContinue);
		Button delete = (Button) findViewById(R.id.editRecipeDelete);
		Button selectImage= (Button) findViewById(R.id.editRecipeSelectImage);
		final Spinner allIngredients = (Spinner) findViewById(R.id.ingredientList);
		createIngredientRelatedD(allIngredients);
		allIngredients.setAdapter(getIngredients());
		allIngredients.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos>1)
				{
					deleteIngredient(chosenIngredientsQuanitity.get(pos-2),pos-2,allIngredients);
				}
				else
					if(pos==1)
					{
				        AlertDialog alert11 = existingIng.create();
				        alert11.show();
					}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
		});
		
		final Spinner allSteps = (Spinner) findViewById(R.id.stepList);
		allSteps.setAdapter(getSteps());
		allSteps.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos>1)
				{
					updateStep(steps.get(pos-2),pos-2,allSteps);
				}
				else
					if(pos==1)
						addStepDialog(allSteps);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
		});
		
		
		nameTitle.setVisibility(0);
		descriptTitle.setVisibility(0);
		restButton.setVisibility(0);
		restButton.setEnabled(true);
		contin.setVisibility(0);
		contin.setEnabled(true);
		delete.setVisibility(0);
		delete.setEnabled(true);
		selectImage.setVisibility(0);
		selectImage.setEnabled(true);
		allSteps.setVisibility(0);
		allSteps.setEnabled(true);
		allIngredients.setVisibility(0);
		allIngredients.setEnabled(true);
		stepBack.setVisibility(0);
		ingBack.setVisibility(0);
		
		recipeName.setVisibility(0);
		recipeName.setText(chosenRecipe.getName());
		
		recipeDescription.setVisibility(0);
		recipeDescription.setText(chosenRecipe.getDescription());
		getSteps();
	}
//---------------------------------------------------------------------------------------------------------
	public void reset(View view)
	{
		areYouSure(0);
	}
//----------------------------------------------------------------------------------------------------------
	private void updateHint(final Hint oldHint,final int pos,final Spinner allHints,final Step parentStep)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder hintUpdate = new AlertDialog.Builder(this);
		hintUpdate.setTitle("Modifiy Step");
		
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
				allHints.setAdapter(getHints(parentStep));
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
				allHints.setAdapter(getHints(parentStep));
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
//---------------------------------------------------------------------------------------------------------
	public ArrayAdapter<String> getSteps()
	{
		ArrayList<String> stepNoAndDetails= new ArrayList<String>();
		stepNoAndDetails.add("Steps");
		stepNoAndDetails.add("Add step");
		for(Step step : steps)
		{
			stepNoAndDetails.add("Step "+step.getStepNo()+": "+step.getDetails());
		}
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stepNoAndDetails);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return temp;
	}
//--------------------------------------------------------------------------------------------------------------------------	
	private void addStepHintDialog(final int num,final Spinner allHints,final Step parentStep)
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
				
				if(allHints !=null)
					allHints.setAdapter(getHints(parentStep));
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
	private void addStepDialog(final Spinner spinner)
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
		TextView stepNumTitle = new TextView(this);
		stepNumTitle.setText("What step is this?");
		stepNumTitle.setTextSize(20);
		final EditText stepNum = new EditText(this);
		stepNum.setHint("Leave blank to make last");
		stepNum.setInputType(InputType.TYPE_CLASS_NUMBER);
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
						EditRecipe.this.runOnUiThread(new Runnable()
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
				addStepHintDialog(stepHints.size()+1,null,null);
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
		ll.addView(stepNumTitle,layoutParams);
		ll.addView(stepNum,layoutParams);
		ll.addView(stepDetails,layoutParams);
		ll.addView(setTimer,layoutParams);
		ll.addView(timerTime,layoutParams);
		ll.addView(stepImg,layoutParams);
		ll.addView(warning,layoutParams);
		ll.addView(addHint,layoutParams);
		buildStep.setView(ll);
		
		buildStep.setPositiveButton("finished", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int t) 
			{
				
				String imgName = "";
				Step step;
				Log.d("arraySize", Integer.toString(stepImgName.size()));
				if(currentSize < stepImgName.size())
				{
					Log.d("arraySize", Integer.toString(stepImgName.size()));
					imgName=stepImgName.get(stepImgName.size()-1);
				}
				else
				{
					stepImgName.add(imgName);
					stepImgDir.add(imgName);
				}		
				step = new Step(0,0,stepDetails.getText().toString(),imgName,0,false,stepTimer);
				if(stepHints.size()>0)
				{
					step.setHints(stepHints);
					stepHints.removeAll(stepHints);
				}
				if(!stepNum.getText().toString().isEmpty() && Integer.parseInt(stepNum.getText().toString())<steps.size())
				{
					int pos = Integer.parseInt(stepNum.getText().toString());
					step.setStepNo(pos);
					steps.add(pos-1, step);
					for(int i=pos;i<steps.size();i++)
					{
						steps.get(i).setStepNo(steps.get(i).getStepNo()+1);
					}
				}
				else
				{
					step.setStepNo(steps.size()+1);
					steps.add(step);
				}
				spinner.setAdapter(getSteps());
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
	private void updateStep(final Step oldStep,final int pos,final Spinner spinner)
	{
		stepTimer=oldStep.getTimer();
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
		
		final Spinner allHints = new Spinner(this);
		allHints.setAdapter(getHints(oldStep));
		allHints.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos>1)
				{
					updateHint(stepHints.get(pos-2),pos-2,allHints,oldStep);
					allHints.setAdapter(getHints(oldStep));
				}
				else
					if(pos == 1)
						addStepHintDialog(stepHints.size()+1,allHints,oldStep);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
		});
		
		
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
						Log.d("test", "updater");
						EditRecipe.this.runOnUiThread(new Runnable()
						{
							@Override
							public void run() 
							{
								if(timer.getTime().compareTo("No Timer")!=0)
								{	
									Log.d("test", "UiThread");
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
		
		ll.addView(stepNum,layoutParams);
		ll.addView(stepDetails,layoutParams);
		ll.addView(setTimer,layoutParams);
		ll.addView(timerTime,layoutParams);
		ll.addView(allHints,layoutParams);
		ll.addView(stepImg,layoutParams);
		ll.addView(warning,layoutParams);
		stepUpdate.setView(ll);
		
		stepUpdate.setPositiveButton("update", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String imgName= stepImgName.get(pos);
				Step newStep;
				if(oldStep.getMedia().compareTo(imgName)==0)
					newStep = new Step(oldStep.getID(),oldStep.getRecID(),stepDetails.getText().toString(),imgName,oldStep.getStepNo(),oldStep.isImageInternal(),stepTimer);
				else
					newStep = new Step(oldStep.getID(),oldStep.getRecID(),stepDetails.getText().toString(),imgName,oldStep.getStepNo(),false,stepTimer);
				
				steps.set(pos,newStep);
				spinner.setAdapter(getSteps());
					
			}	
		});
		stepUpdate.setNeutralButton("Delete Step", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				removedSteps.add(oldStep);
				steps.remove(pos);
				for(int i=pos;i<steps.size();i++)
				{
					steps.get(i).setStepNo(steps.get(i).getStepNo()-1);
				}
				dialog.cancel();
				spinner.setAdapter(getSteps());
			}
			
		});
		stepUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface d, int arg1) 
			{
				d.cancel();
			}
		});
		
		AlertDialog dialog = stepUpdate.create();
		dialog.show();
		
	}
//------------------------------------------------------------------------------------------
	private ArrayAdapter<String> getHints(Step step)
	{
		stepHints = step.getHints();
		ArrayList<String> stepNoAndDetails= new ArrayList<String>();
		
		stepNoAndDetails.add("Hints");
		stepNoAndDetails.add("Add hint");
		if(!step.getHints().isEmpty())
		{
			for(Hint hint : stepHints)
				stepNoAndDetails.add("Hint "+hint.getHintNo()+": "+hint.getDetails());
		}
		
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stepNoAndDetails);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return temp;
	}
//----------------------------------------------------------------------------------------------	
	private ArrayAdapter<String> getIngredients()
	{
		if(chosenIngredientsQuanitity.size()==0)
		{
			chosenIngredientsQuanitity=previousIngredients;	
		}
		ArrayList<String> ingredients= new ArrayList<String>();
		Quantity.MeasureType mTemp;
		if(!chosenIngredients.isEmpty())
		{
			for(int i=0;i<chosenIngredients.size();i++)
			{
				mTemp = Quantity.MeasureType.valueOf(qType.get(i));
				Quantity Q = new Quantity(0,chosenRecipe.getID(),chosenIngredients.get(i).getID(),mTemp,amount.get(i)
						,chosenIngredients.get(i).getName(),chosenIngredients.get(i).getImg(),true);
				
				chosenIngredientsQuanitity.add(Q);
			}
		}
		
		ingredients.add("Ingredients");
		ingredients.add("Add Ingredient");
		for(Quantity qu : chosenIngredientsQuanitity)
		{
			ingredients.add(allDBIngredients.get(qu.getIngID()-1).getName());
		}
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ingredients);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return temp;
	}
//------------------------------------------------------------------------------------------
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent imageReturn)
	{
		super.onActivityResult(requestCode, resultCode,imageReturn);
		String[] acceptedVideoFormats = {"mp4","3gp","ts","webm","mkv"};
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
//---------------------------------------------------------------------------------------------
	public void deleteRecipe(View view)
	{
		areYouSure(1);
	}
//---------------------------------------------------------------------------------------------
	private void areYouSure(final int doing)
	{
		AlertDialog.Builder doubleCheck = new AlertDialog.Builder(this);
		doubleCheck.setMessage("Warning");	
		doubleCheck.setCancelable(false);
		final TextView msg = new TextView(this);
		msg.setText("Are you sure you want to do this?");
		doubleCheck.setView(msg);
		doubleCheck.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				if(doing ==1)
				{
					db.deleteRecipe(chosenRecipe);
					Toast.makeText(getBaseContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
					chosenRecipe=null;
					chosenRecipeID=0;
					Intent intent = getIntent();
					finish();
					startActivity(intent);
				}
				else
				{
					loadRecipe(chosenRecipeID);
					Toast.makeText(getBaseContext(), "Recipe Reverted back to last version", Toast.LENGTH_SHORT).show();
				}
			}	
		});
		doubleCheck.setNegativeButton("On second thought", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		
		doubleCheck.create().show();
	}
//---------------------------------------------------------------------------------------------
	public void selectImage(View view)
	{
		Toast.makeText(this, "Select a Picture that will represent this recipe", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/* video/*");
		whereFrom =1;
		startActivityForResult(intent,SELECT_PHOTO);
	}
//----------------------------------------------------------------------------------------------
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
		    FileOutputStream out = openFileOutput(fileName,Context.MODE_APPEND);
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
			Log.d("failed step", e.toString());
			return false;
		}
	}
//----------------------------------------------------------------------------------------------
	public void finalize(View view)
	{
		EditText recipeName = (EditText) findViewById(R.id.recipeName);
		EditText recipeDescription = (EditText) findViewById(R.id.recipeDescription);
		Recipe newRecipe;
		
		if(chosenRecipe.getImg().compareTo(recImgName)==0)
		{
			newRecipe = new Recipe(chosenRecipe.getID(),recipeName.getText().toString(),recipeDescription.getText().toString(),
					chosenRecipe.getImg(),chosenRecipe.isImageInternal(),1,0,0);
			
		}
		else
		{
			copyRecipeImageFile(recImgDir,recImgName);	
			newRecipe = new Recipe(chosenRecipe.getID(),recipeName.getText().toString(),recipeDescription.getText().toString(),
					recImgName,false,1,0,0);
		}
		
		newRecipe.setSteps(steps);
		
		for(Quantity q : chosenIngredientsQuanitity)
			newRecipe.addQu(q);
		
		if(!removedSteps.isEmpty())
		{
			for(Step step : removedSteps)
				newRecipe.addDeletedStep(step);
		}
		if(!deletedIngredients.isEmpty())
		{
			for(Quantity q : deletedIngredients)
				newRecipe.addDeletedQuantity(q);
		}
		
		for(int i=0;i<stepImgDir.size();i++)
			copyStepImageFile(stepImgDir.get(i),stepImgName.get(i));
		
		if(recipeName.getText().toString().compareToIgnoreCase(chosenRecipe.getName())!=0)
			addOrEdit(newRecipe).show();
		else
		{
			db.updateRecipe(newRecipe);
			finished();
		}

	}
//-------------------------------------------------------------------------------------------
	private void finished()
	{
		Toast.makeText(this, "Recipe Updated", Toast.LENGTH_SHORT).show();
		chosenRecipe=null;
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
//----------------------------------------------------------------------------------------------
	private Dialog addOrEdit(final Recipe newRecipe)
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		
		AlertDialog.Builder recipeUpdate = new AlertDialog.Builder(this);
		recipeUpdate.setTitle("What would u like to do?");
		TextView msg = new TextView(this);
		msg.setText("you have changed the recipes name would you like to add this as a new recipe or update the already existing one?");
		recipeUpdate.setNeutralButton("Add as new recipe", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				db.addRecipe(newRecipe);
				finished();
			}	
		});
		recipeUpdate.setPositiveButton("Update", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1) 
			{
				dialog.cancel();
				db.updateRecipe(newRecipe);
				finished();
			}
		});
		recipeUpdate.setNegativeButton("Cancel", null);
		ll.addView(msg,layoutParams);
		recipeUpdate.setView(ll);
		
		return recipeUpdate.create();
	}
//----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------		
//-----------------------------------------------------------------------------------------
	private void createIngredientRelatedD(final Spinner spinner)
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
		for(int i=0;i<allDBIngredients.size();i++)
		{
			names.add(allDBIngredients.get(i).getName());
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
				 if(isChecked)
				 {
					 chosenIngredients.add(allDBIngredients.get(which));
					 AlertDialog dialogtoDisplay = MT.create();
					 dialogtoDisplay.show();
				 }
				 else
				 if(!isChecked)
				 {
					 int id=allDBIngredients.get(which).getID();
					 for(int i=0;i<chosenIngredients.size();i++)
					 {
						 if(id== chosenIngredients.get(i).getID())
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
                spinner.setAdapter(getIngredients());
            }
        });	
	}
//--------------------------------------------------------------------------------------------------
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
}

