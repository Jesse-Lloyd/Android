package com.example.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.R;
import com.example.testapp.Recipe;

public class MainActivity extends Activity {
	public DatabaseHandler db = new DatabaseHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.example.testapp.R.layout.activity_main);
		ArrayList<Recipe> recipes = db.getAllRecipes();
		
		
		if (recipes.size() ==0) // populate tables there are no recipes in the db
		{
			Toast.makeText(this, "Please wait while the database is being created", Toast.LENGTH_LONG).show();
			db.populateTable();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		GeneralDialogs temp = new GeneralDialogs(this);
		switch(item.getItemId())
		{
			case R.id.main_menu_help:
				temp.help(0);
				return true;
			case R.id.about:
				temp.about();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	public void onButtonClick(View view) {
		View B1 = (View) findViewById(R.id.toSelectRecipes);
		View B2 = (View) findViewById(R.id.toAddRecipe);
		View B3 = (View) findViewById(R.id.toEditRecipe);
		View B4 = (View) findViewById(R.id.toImportRecipe);
		View B5 = (View) findViewById(R.id.toExportRecipe);
		View B6 = (View) findViewById(R.id.toAddIngredient);
		Intent intent = new Intent();

		if (view == B1)
			intent = new Intent(this, RecipeList.class);
		if (view == B2)
			intent = new Intent(this,AddRecipe.class);
		if (view == B3)
			intent = new Intent(this, EditRecipe.class);
		if(view == B4)
			intent = new Intent(this, Imports.class);
		if(view == B5)
			intent = new Intent(this, Export.class);
		if(view == B6)
			intent = new Intent(this, AddIngredient.class);

		startActivity(intent);
	}
}
