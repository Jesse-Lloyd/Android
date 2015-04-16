package com.example.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Ingredient;
import com.example.testapp.R;
import com.example.testapp.Recipe;

public class Export extends Activity {
	DatabaseHandler db = new DatabaseHandler(this);
	ArrayList<Integer> chosenRecipes = new ArrayList<Integer>();
	EditText E1;
	File rootpath = new File("Recipes");

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(com.example.testapp.R.layout.export);
		E1 = (EditText) findViewById(R.id.exportFolder_name);
		LinearLayout left = (LinearLayout)findViewById(R.id.left);
		//(button) removeView(R.id.exportFolder_name);
		
		
		for(int i=0;i<db.getAllRecipes().size();i++)
		{
			CheckBox cb = new CheckBox(this);
			cb.setText(db.getRecipe(i+1).getName());
			cb.setId(db.getRecipe(i+1).getID());
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) 
				{
					if(isChecked)
						chosenRecipes.add(arg0.getId());
					else
					{
						int toRemove = arg0.getId();
						for(int i=0;i<chosenRecipes.size();i++)
						{
							int current = chosenRecipes.get(i);
							
							if(current == toRemove)
								chosenRecipes.remove(i);
						}
					}
				}
			});
			
			left.addView(cb);
		}
	}
	//----------------------------------------------------	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.export_recipe_menu, menu);
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
			case R.id.export_recipe_help:
				temp.help(7);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	//-------------------------------------------------------
	public void onExportRecipesButtonClick(View view)
	{
		//when button is clicked this function is called place what u want to do in here etc...
		String s = E1.getText().toString();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    	
		if (!s.replaceAll("[^A-Za-z0-9]","").equals(""))
		{
			Log.d("OnExportRecipeButton", "passes string check");
			if(!chosenRecipes.isEmpty())
			{
				Log.d("OnExportRecipeButton", "chosenRecipes is not empty");
				for (int i = 0; i < chosenRecipes.size(); i++ )
				{
					recipes.add(db.getRecipe(chosenRecipes.get(i)));
				}  
				exportDB(recipes, exportIngredients(),s.replaceAll("[^A-Za-z0-9]",""));
				exportMedia(recipes, exportIngredients(),s.replaceAll("[^A-Za-z0-9]",""));
			}
			else
				Log.d("OnExportRecipeButton", "chosenRecipes is empty");
		}
		else
			Log.d("OnExportRecipeButton", "failed string check");
	}

	// prepares an arraylist of ingredients to be exported
	public ArrayList<Ingredient> exportIngredients()
	{
		ArrayList<Ingredient> ingredients = db.getAllIngredients();
		ArrayList<Ingredient> ingredientsToBeExported = new ArrayList<Ingredient>();
		
		for(int i = 0; i < ingredients.size(); i++)
		{
			if(ingredients.get(i).getVersion() != 1 || !ingredients.get(i).isImageInternal())
			{
				ingredientsToBeExported.add(ingredients.get(i));
			}
		}
		//return ingredientsToBeExported;
		return ingredientsToBeExported;
	}
	
	//exports the media of the ingredients and recipes
	public void exportMedia(ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients, String folderName)
	{
		Log.d("Export Media","Running");
		for(int i = 0; i < ingredients.size(); i++)
		{
			Log.d("Export Media",String.valueOf(ingredients.get(i).isImageInternal()));
			if(!ingredients.get(i).isImageInternal())
			{
				Log.d("Export Media","ingredient image is internal");
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	    		    //handle case of no SDCARD present
					Log.d("Export Media","no sd card");
	    		} else 
	    		{
	    			Log.d("Export Media","Exporting ingredients");
	    		    File file = new File(Environment.getExternalStorageDirectory()
	    		         +File.separator
	    		         +this.rootpath //folder name
	    		         +File.separator
	    		         +folderName); //file name
	    		    
	    		    File filepath = new File(file,ingredients.get(i).getImg());
	    		    File frm = new File(this.getFilesDir(), ingredients.get(i).getImg());
	    		    InputStream in;
	    		    OutputStream out;
	    		    try {
	    		    	in = new FileInputStream(frm);
	    		    	out = new FileOutputStream(filepath);

	    		    	// Transfer bytes from in to out
	    		    	byte[] buf = new byte[1024];
	    		    	int len;
	    		    	while ((len = in.read(buf)) > 0) {
	    		    		out.write(buf, 0, len);
	    		    	}
	    		    	in.close();
	    		    	out.close();
	    		    } catch (FileNotFoundException e) {
	    		    	// TODO Auto-generated catch block
	    		    	e.printStackTrace();
	    		    } catch (IOException e) {
	    		    	// TODO Auto-generated catch block
	    		    	e.printStackTrace();
	    		    }
	    		}
			}
		}
		for(int i = 0; i < recipes.size(); i++)
		{
			if(!recipes.get(i).isImageInternal())
			{
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	    		    //handle case of no SDCARD present
	    		} else 
	    		{
	    		    File file = new File(Environment.getExternalStorageDirectory()
	    		         +File.separator
	    		         +this.rootpath //folder name
	    		         +File.separator
	    		         +folderName); //file name
				
	    		    File filepath = new File(file,recipes.get(i).getImg());
	    		    File frm = new File(this.getFilesDir(), recipes.get(i).getImg());
	    		    InputStream in;
	    		    OutputStream out;
	    		    try {
	    		    	in = new FileInputStream(frm);
	    		    	out = new FileOutputStream(filepath);

	    		    	// Transfer bytes from in to out
	    		    	byte[] buf = new byte[1024];
	    		    	int len;
	    		    	while ((len = in.read(buf)) > 0) {
				        out.write(buf, 0, len);
				    }
				    in.close();
				    out.close();
	    		    } catch (FileNotFoundException e) {
	    		    	// TODO Auto-generated catch block
	    		    	e.printStackTrace();
	    		    } catch (IOException e) {
	    		    	// TODO Auto-generated catch block
	    		    	e.printStackTrace();
	    		    }
	    		}
			}
			for(int j = 0; j < recipes.get(i).getSteps().size(); j++)
			{
				if(!recipes.get(i).getStep(j).isImageInternal())
				{
					if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    		    //handle case of no SDCARD present
		    		} else 
		    		{
		    		    File file = new File(Environment.getExternalStorageDirectory()
		    		         +File.separator
		    		         +this.rootpath //folder name
		    		         +File.separator
		    		         +folderName); //file name
		    		    File filepath = new File(file,recipes.get(i).getStep(j).getMedia());
		    		    File frm = new File(this.getFilesDir(), recipes.get(i).getStep(j).getMedia());
		    		    InputStream in;
						OutputStream out;
						try {
							in = new FileInputStream(frm);
							out = new FileOutputStream(filepath);

					    	// Transfer bytes from in to out
							byte[] buf = new byte[1024];
					   		int len;
					    	while ((len = in.read(buf)) > 0) {
					    		out.write(buf, 0, len);
					    	}
					    	in.close();
					    	out.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
				}
			}
		}
		Toast.makeText(this, "Recipes Exported", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	/* Using Serialization is the quickest and dirtiest way to quickly implement export and import.
     * How ever this comes at a cost, using this method makes the program prone to errors   
     */
    public void exportDB(ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients, String folderName)
    { 	
    	try 
    	{    		
    		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		    //handle case of no SDCARD present
    		} else {
    		    File file = new File(Environment.getExternalStorageDirectory()
    		         +File.separator
    		         +this.rootpath //folder name
    		         +File.separator
    		         +folderName);
    		    Log.d("directory", file.toString());
    		    Log.d("Directory created", String.valueOf(file.isDirectory()));
    		    file.mkdirs();
    		    Log.d("Directory created", String.valueOf(file.isDirectory()));
    		    
		        File fileN = new File(file, "IngredientExport.ser");
		        Log.d("IngFile", String.valueOf(fileN.isFile()));
		        fileN.createNewFile(); 
		        Log.d("IngFile", String.valueOf(fileN.isFile()));
		        
    		    FileOutputStream ingfileStream = new FileOutputStream(fileN);
    			ObjectOutputStream ingobjectStream = new ObjectOutputStream(ingfileStream);
    			ingobjectStream.writeObject(ingredients);    		
    		    
    		    File rfileN = new File(file, "recipeExport.ser");
    		    Log.d("IngFile", String.valueOf(rfileN.isFile()));
		        rfileN.createNewFile(); 
		        Log.d("IngFile", String.valueOf(rfileN.isFile()));
    		    
    		    FileOutputStream rfileStream = new FileOutputStream(rfileN);
    			ObjectOutputStream robjectStream = new ObjectOutputStream(rfileStream);
    			robjectStream.writeObject(recipes);
    		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
}
