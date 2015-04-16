package com.example.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.example.testapp.Ingredient;
import com.example.testapp.R;
import com.example.testapp.Recipe;

public class RecipeList extends Activity 
{
	DatabaseHandler db = new DatabaseHandler(this);
	Recipe chosenRec;
	static ArrayList<Recipe> savedRecipeList = new ArrayList<Recipe>();
	RadioGroup rg;
	int selectedCat;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		rg = (RadioGroup) findViewById(R.id.radiolist);
		selectedCat=0;
		setSearch();
		if(savedRecipeList.isEmpty())
		{
			allRecipes(db.getAllRecipes());
		}
		else
		{
			setRadioGroupListener(savedRecipeList);
			for(int i=0;i<savedRecipeList.size();i++)
			{
				RadioButton rb = new RadioButton(this);
				rb.setText(savedRecipeList.get(i).getName());
				rb.setId(i);
				rg.addView(rb);
			}
		}
	}
	//----------------------------------------------------	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.recipe_list_menu, menu);
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
				case R.id.recipe_list_help:
					temp.help(1);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
	//-------------------------------------------------------
	private void searchByIngredients()
	{
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		final ArrayList<Ingredient> chosenIngredients=new ArrayList<Ingredient>();
		
		final ArrayList<Ingredient> ing = db.getAllIngredients();
		ArrayList<String> ingNames = new ArrayList<String>();
		for(Ingredient ingredient : ing)
		{
			ingNames.add(ingredient.getName());
		}
		
		AlertDialog.Builder selectIngredients = new AlertDialog.Builder(this);
		selectIngredients.setTitle("Ingredients");
		TextView tv = new TextView(this);
		tv.setText("Select the ingredients that you want to search with");
		
		CharSequence[] items = ingNames.toArray(new CharSequence[ingNames.size()]);
	    boolean[] itemsChecked = new boolean[items.length];
	    selectIngredients.setCancelable(true);
	    selectIngredients.setMultiChoiceItems(items,itemsChecked,new DialogInterface.OnMultiChoiceClickListener()
		{
			 @Override
             public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			 {
				 if(isChecked)
				 {
					 chosenIngredients.add(ing.get(which));
				 }
				 else
				 if(!isChecked)
				 {
					 int id=ing.get(which).getID();
					 for(int i=0;i<chosenIngredients.size();i++)
					 {
						 if(id==chosenIngredients.get(i).getID())
							 chosenIngredients.remove(i);
					 }
				 }
             }
		});
	    selectIngredients.setPositiveButton("Recipes with ALL of these Ingredients",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	byIngredients(db.getAllRecipesByIngredients(chosenIngredients,true));
            }
        });	
	    selectIngredients.setNeutralButton("Recipes with ANY of these Ingredients",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	byIngredients(db.getAllRecipesByIngredients(chosenIngredients,false));
            }
        });	
	    selectIngredients.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	setSearch();
            }
        });	
		AlertDialog dialog = selectIngredients.create();
		dialog.show();
	}
	private void searchByName()
	{
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 20, 30, 0);
		layoutParams.gravity=Gravity.CENTER;
		
		AlertDialog.Builder enterName = new AlertDialog.Builder(this);
		enterName.setTitle("Search for recipes by Name");
		TextView tv = new TextView(this);
		tv.setText("enter the word/words you wish to search with");
		final EditText et = new EditText(this);
		et.setHint("example:  chicken cream");
		ll.addView(tv,layoutParams);
		ll.addView(et,layoutParams);
		enterName.setView(ll);
		enterName.setPositiveButton("Begin Search", new  DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				byName(db.getAllByNameSearch(et.getText().toString()));
			}
		});
		enterName.setNegativeButton("Cancel", new  DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				setSearch();
			}
		});
		AlertDialog dialog = enterName.create();
		dialog.show();
	}
	private void searchByCategory()
	{		
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		ArrayList<String> catNames = new ArrayList<String>();
		for(Category temp : db.getCategoryList())
		{

			catNames.add(temp.getName());
		}
		
		AlertDialog.Builder selectIngredients = new AlertDialog.Builder(this);
		selectIngredients.setTitle("Ingredients");
		TextView tv = new TextView(this);
		tv.setText("Select the ingredients that you want to search with");
		
		CharSequence[] items = catNames.toArray(new CharSequence[catNames.size()]);
	    selectIngredients.setCancelable(true);
	    selectIngredients.setSingleChoiceItems(items,-1,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int choice) 
			{
				selectedCat=db.getCategoryList().get(choice).getID();;
			}
		});
	    selectIngredients.setPositiveButton("Begin search",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	byCategory(db.getRecipesByCategory(selectedCat));
            }
        });		
	    selectIngredients.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	setSearch();
            }
        });	
		AlertDialog dialog = selectIngredients.create();
		dialog.show();
	}
	private void setSearch()
	{
		Spinner searchBy = (Spinner) findViewById(R.id.search);
		searchBy.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) 
			{
				if(pos !=0)
				{
					if(pos==1)
					{
						searchByIngredients();
					}
					else
					if(pos==2)
					{
						searchByName();
					}
					else
						searchByCategory();
				}
					
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}	
		});
		
		ArrayList<String> spinnerItems= new ArrayList<String>();		
		spinnerItems.add("Touch here to Search Recipes");
		spinnerItems.add("By Ingredients");
		spinnerItems.add("By Name");
		spinnerItems.add("Or By Category");
		
		ArrayAdapter<String> temp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerItems);
		temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchBy.setAdapter(temp);
	}
	private void byCategory(ArrayList<Recipe> refinedList)
	{
		ImageView iv = (ImageView) findViewById(R.id.finalimage);
		TextView tV = (TextView) findViewById(R.id.Description);
		Button b = (Button) findViewById(R.id.recipeList_next);

		b.setEnabled(false);
		tV.setText(""); 
		iv.setBackgroundResource(0);
		iv.setImageResource(0);
		
		setRadioGroupListener(refinedList);
		savedRecipeList.removeAll(savedRecipeList);
		savedRecipeList.addAll(refinedList);
		if(!refinedList.isEmpty())
		{
			for(int i=0;i<refinedList.size();i++)
			{
				RadioButton rb = new RadioButton(this);
				rb.setText(refinedList.get(i).getName());
				rb.setId(i);
				rg.addView(rb);
			}
		}
		else
		{
			TextView zero = new TextView(this);
			zero.setText("No recipes found");
			rg.addView(zero);
		}
		setSearch();
	}
	private void byName(ArrayList<Recipe> refinedList)
	{
		ImageView iv = (ImageView) findViewById(R.id.finalimage);
		TextView tV = (TextView) findViewById(R.id.Description);
		Button b = (Button) findViewById(R.id.recipeList_next);

		b.setEnabled(false);
		tV.setText(""); 
		iv.setBackgroundResource(0);
		iv.setImageResource(0);
		
		setRadioGroupListener(refinedList);
		savedRecipeList.removeAll(savedRecipeList);
		savedRecipeList.addAll(refinedList);
		if(!refinedList.isEmpty())
		{
			for(int i=0;i<refinedList.size();i++)
			{
				RadioButton rb = new RadioButton(this);
				rb.setText(refinedList.get(i).getName());
				rb.setId(i);
				rg.addView(rb);
			}
		}
		else
		{
			TextView zero = new TextView(this);
			zero.setText("No recipes found");
			rg.addView(zero);
		}
		setSearch();
	}
	private void byIngredients(ArrayList<Recipe> refinedList)
	{	 
		ImageView iv = (ImageView) findViewById(R.id.finalimage);
		TextView tV = (TextView) findViewById(R.id.Description);
		Button b = (Button) findViewById(R.id.recipeList_next);

		b.setEnabled(false);
		tV.setText(""); 
		iv.setBackgroundResource(0);
		iv.setImageResource(0);
		
		setRadioGroupListener(refinedList);
		savedRecipeList.removeAll(savedRecipeList);
		savedRecipeList.addAll(refinedList);
		if(!refinedList.isEmpty())
		{
			for(int i=0;i<refinedList.size();i++)
			{
				RadioButton rb = new RadioButton(this);
				rb.setText(refinedList.get(i).getName());
				rb.setId(i);
				rg.addView(rb);
			}
		}
		else
		{
			TextView zero = new TextView(this);
			zero.setText("No recipes found");
			rg.addView(zero);
		}
		setSearch();
	}
	private void allRecipes(final ArrayList<Recipe> recipeList)
	{
		setRadioGroupListener(recipeList);
		for (int i = 0; i < recipeList.size(); i++) 						// add radio buttons for each recipe in the recipe arrayList																// recipe in the database
		{
			RadioButton rb = new RadioButton(this);
			rb.setText(recipeList.get(i).getName());
			rb.setId(i);
			rg.addView(rb);
		}
	}
	private void setRadioGroupListener(final ArrayList<Recipe> recipeList)
	{
		rg.removeAllViews();
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int recipe)
			{
				ImageView iv = (ImageView) findViewById(R.id.finalimage);
				iv.setScaleType(ImageView.ScaleType.FIT_XY);
				TextView tv = (TextView) findViewById(R.id.Description);
				iv.setContentDescription(recipeList.get(recipe).getName());
				Button b = (Button) findViewById(R.id.recipeList_next);

				if(recipeList.get(recipe).isImageInternal())
				{
					iv.setImageResource(getResources().getIdentifier(recipeList.get(recipe).getImg(), "drawable", getPackageName()));
				}
				else
				if(!recipeList.get(recipe).isImageInternal())
				{
					if(recipeList.get(recipe).getImg().compareTo("") == 0)
					{
						iv.setImageResource(getResources().getIdentifier("noimage", "drawable", getPackageName()));
					}
					else
					{
						
						File filepath = new File("data/data/com.example.testapp/files/"+recipeList.get(recipe).getImg());
						Bitmap image = BitmapFactory.decodeFile(filepath.toString());
						int resize = (int) (image.getHeight() * (512.0 / image.getWidth()));
						Bitmap scaledImage = Bitmap.createScaledBitmap(image, 512, resize, true);
						iv.setImageBitmap(scaledImage);	
					}
				}
				iv.setBackgroundResource(getResources().getIdentifier("blackborder", "drawable",getPackageName()) );
				tv.setText(recipeList.get(recipe).getDescription()); 
				iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				b.setEnabled(true); 										// once a recipe is selected this enables the button to move onto the ingredient list
				chosenRec = recipeList.get(recipe);
			}
		});
	}
	public void onButtonClick(View view) 
	{
		if(chosenRec == null)
		{
			Toast.makeText(this, "Please select a recipe", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent intent = new Intent(this, IngredientList.class);
			intent.putExtra("pickedRecipe",chosenRec.getID());
			startActivity(intent);
		}
	}
}
