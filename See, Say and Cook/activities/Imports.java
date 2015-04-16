package com.example.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Ingredient;
import com.example.testapp.R;
import com.example.testapp.Recipe;

public class Imports extends Activity
{
	private DatabaseHandler db = new DatabaseHandler(this);
	private EditText E1;
	private String rootpath = "Recipes";
	private String s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(com.example.testapp.R.layout.imports);
		E1 = (EditText) findViewById(R.id.set_imports_folder);
	}
	
	public void onImportRecipesButtonClick(View view)
	{
		this.s = E1.getText().toString();
		Log.d("String", s);
		if (!s.replaceAll("[^A-Za-z0-9]","").equals(""))
		{
			Log.d("String", "Not Null");
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{}
			else
			{
				Log.d("Dir", "External Storage exists");
				File path = new File(Environment.getExternalStorageDirectory()
	    		         +File.separator
	    		         +rootpath //folder name
	    		         +File.separator
	    		         +s);
				Log.d("path", path.toString());
				if(path.isDirectory())
				{
					Log.d("Dir", "Exists");
					ArrayList<Ingredient> ingredients = loadIngredients(s);
					ArrayList<Recipe> recipes = loadRecipes(s);
				
					importIngredients(ingredients);
					importRecipes(recipes);
				}
			}
		}
		else
			Log.d("Import","Empty Import string" );
	}
	//----------------------------------------------------	
			@Override
			public boolean onCreateOptionsMenu(Menu menu) 
			{
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.import_recipe_menu, menu);
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
					case R.id.import_recipe_help:
						temp.help(6);
						return true;
					default:
						return super.onOptionsItemSelected(item);
				}
			}
		//-------------------------------------------------------
	public void importIngredients(ArrayList<Ingredient> ingredients)
	{
		String a = "The Import Ingredient function has detected there is another Ingredient with the same name already in the database called";
		ArrayList<Ingredient> ingredientCheck = db.getAllIngredients();
        for (int i = 0; i < ingredients.size(); i++)
        {
        	int match = 0;
        	for(int j = 0; j < ingredientCheck.size(); j++)
        	{
        		if (ingredientCheck.get(j).getName().equals(ingredients.get(i).getName()))
        		{
        			handleIngredientDialog(ingredients.get(i), a);
        			match++;
        		}
        	}
        	
        	if(match == 0)
        	{
        		db.addIngredient(ingredients.get(i));
        	}
        }
	}
	
	public void importRecipes(ArrayList<Recipe> recipes)
	{
		String a = "The Import Recipe function has detected a recipe with the Name and ID but a different version number";
		String b = "The Import Recipe function has detected a recipe with the Name, ID and version number";
		String c = "The Import Recipe function has detected a recipe with the Name but different ID";
		ArrayList<Recipe> recipeCheck = db.getAllRecipes();
        for (int i = 0; i < recipes.size(); i++)
        {
        	int match = 0;
        	for(int j = 0; j < recipeCheck.size(); j++)
        	{
        		if (recipeCheck.get(j).getName().equals(recipes.get(i).getName()))
        		{
        			if (recipeCheck.get(j).getID() == recipes.get(i).getID())
        			{
        				if (recipeCheck.get(j).getVersion() == recipes.get(i).getVersion())
            			{
            				handleRecipeDialog(recipes.get(i), b);
            			}
        				else
        				{
        					handleRecipeDialog(recipes.get(i), a);
        				}
        			}
        			else
        			{
        				handleRecipeDialog(recipes.get(i), c);
        			}
        			match++;
        		}
        	}
        	if(match == 0)
        	{
        		db.addRecipe(recipes.get(i));
        	}
        }
		Toast.makeText(this, "Recipes Imported", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public void handleRecipeDialog(Recipe recipes, String s)
	{
		final EditText input = new EditText(this);
		final Recipe recipe = recipes;
		final String rootpath = this.rootpath;
		final String folderpath = this.s;
		
		new AlertDialog.Builder(this)
	    .setTitle("Import Alert")
	    .setMessage(s + ": " + recipe.getName() +
	    		" \nWould you like to overwrite the original, save as new entry or ignore entry")
	    .setView(input)
	    .setPositiveButton("Overwrite(Recommended)", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            //Editable value = input.getText(); 
	            db.updateRecipe(recipe);
	            if(!recipe.isImageInternal())
				{
	            	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    		    //handle case of no SDCARD present
		    		} else 
		    		{
		    		    File path = new File(Environment.getExternalStorageDirectory()
		    		         +File.separator
		    		         +rootpath //folder name
		    		         +File.separator
		    		         +folderpath
		    		         +recipe.getImg()); //file name
		    		    File filepath = new File(getFilesDir().getPath(),recipe.getImg());
		    		    if(path.exists())
		    		    {
		    		    	InputStream in;
		    		    	OutputStream out;
		    		    	try {
		    		    		in = new FileInputStream(path);
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
	            
	            for(int i = 0; i < recipe.getSteps().size(); i++)
	            {
	            	if(!recipe.getStep(i).isImageInternal())
					{
	            		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			    		    //handle case of no SDCARD present
			    		} else 
			    		{
			    		    File path = new File(Environment.getExternalStorageDirectory()
			    		         +File.separator
			    		         +rootpath //folder name
			    		         +File.separator
			    		         +folderpath
			    		         +recipe.getStep(i).getMedia()); //file name
			    		    File filepath = new File(getFilesDir().getPath(),recipe.getStep(i).getMedia());
			    		    if(path.exists())
			    		    {
			    		    	InputStream in;
			    		    	OutputStream out;
			    		    	try {
			    		    		in = new FileInputStream(path);
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
	        }
	    }).setNeutralButton("Save as new(Beware of duplicates)", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        	db.addRecipe(recipe);
	        	if(!recipe.isImageInternal())
				{
	            	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    		    //handle case of no SDCARD present
		    		} else 
		    		{
		    		    File path = new File(Environment.getExternalStorageDirectory()
		    		         +File.separator
		    		         +rootpath //folder name
		    		         +File.separator
		    		         +folderpath
		    		         +recipe.getImg()); //file name
		    		    File filepath = new File(getFilesDir().getPath(),recipe.getImg());
		    		    if(path.exists())
		    		    {
		    		    	InputStream in;
		    		    	OutputStream out;
		    		    	try {
		    		    		in = new FileInputStream(path);
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
	            
	            for(int i = 0; i < recipe.getSteps().size(); i++)
	            {
	            	if(!recipe.getStep(i).isImageInternal())
					{
	            		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			    		    //handle case of no SDCARD present
			    		} else 
			    		{
			    		    File path = new File(Environment.getExternalStorageDirectory()
			    		         +File.separator
			    		         +rootpath //folder name
			    		         +File.separator
			    		         +folderpath
			    		         +recipe.getStep(i).getMedia()); //file name
			    		    File filepath = new File(getFilesDir().getPath(),recipe.getStep(i).getMedia());
			    		    if(path.exists())
			    		    {
			    		    	InputStream in;
			    		    	OutputStream out;
			    		    	try {
			    		    		in = new FileInputStream(path);
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
	        }
	    })
	    .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    }).show();
	}
	
	public void handleIngredientDialog(Ingredient ingredients,String s)
	{
		final EditText input = new EditText(this);
		final Ingredient ingredient = ingredients;
		final String rootpath = this.rootpath;
		final String folderpath = this.s;
		
		new AlertDialog.Builder(this)
	    .setTitle("Import Alert")
	    .setMessage(s + ": " + ingredient.getName() +
	    		" \nWould you like to overwrite the original, save as new entry or ignore entry")
	    .setView(input)
	    .setPositiveButton("Overwrite(Recommended)", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            //Editable value = input.getText(); 
	            db.updateIngredient(ingredient);
	            if(!ingredient.isImageInternal())
				{
	            	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    		    //handle case of no SDCARD present
		    		} else 
		    		{
		    		    File path = new File(Environment.getExternalStorageDirectory()
		    		         +File.separator
		    		         +rootpath //folder name
		    		         +File.separator
		    		         +folderpath
		    		         +ingredient.getImg()); //file name
		    		    File filepath = new File(getFilesDir().getPath(),ingredient.getImg());
		    		    if(path.exists())
		    		    {
		    		    	InputStream in;
		    		    	OutputStream out;
		    		    	try {
		    		    		in = new FileInputStream(path);
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
	    }).setNeutralButton("Save as new(Beware of duplicates)", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        	db.addIngredient(ingredient);
	        	if(!ingredient.isImageInternal())
				{
	        		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    		    //handle case of no SDCARD present
		    		} else 
		    		{
		    			File path = new File(Environment.getExternalStorageDirectory()
			    		         +File.separator
			    		         +rootpath //folder name
			    		         +File.separator
			    		         +folderpath
			    		         +ingredient.getImg()); //file name
			    		    File filepath = new File(getFilesDir().getPath(),ingredient.getImg());
			    		    if(path.exists())
			    		    {
			    		    	InputStream in;
			    		    	OutputStream out;
			    		    	try {
			    		    		in = new FileInputStream(path);
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
	    })
	    .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    }).show();
	}
	
	 @SuppressWarnings("unchecked")
		public ArrayList<Ingredient> loadIngredients(String s)
	    {
	    	ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			    //handle case of no SDCARD present
			} else 
			{
			    File path = new File(Environment.getExternalStorageDirectory()
			         +File.separator
			         +"Recipes" //folder name
			         +File.separator
			         +s);
			    
			    File ifile = new File(path, "IngredientExport.ser");
			    if( !ifile.exists())
		        	return ingredients;   
			    
			    try 
		         {
		 			FileInputStream ingfileStream = new FileInputStream(ifile);
		      		ObjectInputStream ingobjectStream = new ObjectInputStream(ingfileStream);
		      		ingredients = (ArrayList<Ingredient>)ingobjectStream.readObject();
		 			
		 		} catch (OptionalDataException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		} catch (ClassNotFoundException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		} catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}
			}
	    	    	
			return ingredients;
	    }
	    
	    @SuppressWarnings("unchecked")
		public ArrayList<Recipe> loadRecipes(String s)
	    {
	    	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			    //handle case of no SDCARD present
			} else 
			{
			    File path = new File(Environment.getExternalStorageDirectory()
			         +File.separator
			         +"Recipes" //folder name
			         +File.separator
			         +s);
			    
			    File file = new File(path, "RecipeExport.ser");
			    if( !file.exists())
		        	return recipes;       
		        
		        try 
		        {
		        	FileInputStream fileStream = new FileInputStream(file);
		     		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
		     		recipes = (ArrayList<Recipe>)objectStream.readObject();
					
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
			return recipes;
	    }
}
