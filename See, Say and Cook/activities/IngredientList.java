package com.example.activities;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.R;
import com.example.testapp.Recipe;

public class IngredientList extends Activity 
{
	DatabaseHandler db = new DatabaseHandler(this);
	int temp;
	int chosenRec;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredient_list);
		chosenRec = (Integer) getIntent().getSerializableExtra("pickedRecipe");	
		mainMethod();
	}
	//----------------------------------------------------	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.ingredient_list_menu, menu);
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
				case R.id.ingredient_list_help:
					temp.help(2);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
	//-------------------------------------------------------
	private void mainMethod()
	{
		
		final LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout2);		
		final ArrayList<CheckBox> cbList = new ArrayList<CheckBox>();
		final Button b = (Button) findViewById(R.id.ingredientList_next);
		Recipe recipe = db.getRecipe(chosenRec);
		
        for(int i = 0; i < recipe.getQus().size(); i++) 
        {
        	 ImageView iv = new ImageView(this);//start of adding Images of each ingredient
             iv.setLayoutParams(new LayoutParams(300,400));
             iv.setPaddingRelative(0, 75, 0, 0);
             if(recipe.getQus().get(i).isImageInternal())
 			{
 				iv.setImageResource(getResources().getIdentifier(recipe.getQus().get(i).getIngImage(), "drawable", getPackageName()));
 			}
 			else
 			if(!recipe.getQus().get(i).isImageInternal())
 			{
 				if(recipe.getQus().get(i).getIngName().compareTo("") == 0)
 				{
 					iv.setImageResource(getResources().getIdentifier("noimage", "drawable", getPackageName()));
 				}
 				else
 				{
 					File filepath = new File("data/data/com.example.testapp/files/"+recipe.getQus().get(i).getIngImage());
 					Uri uri = Uri.parse(filepath.getPath());
 					iv.setImageURI(uri);
 				}
 			}
            //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
             ll.addView(iv);									//end of adding Images of each ingredient
             
        	
            CheckBox cb = new CheckBox(this);	//start of adding checkboxes
            cb.setPaddingRelative(35, 15, 20, 10);
            cb.setTextSize(25);
            DecimalFormat df = new DecimalFormat("0.###");
            String amount = df.format(recipe.getQus().get(i).getAmount());
            
            if(recipe.getQus().get(i).getType().toString().compareTo("NOTYPE")==0)
            {
            	if(recipe.getQus().get(i).getAmount()==0)
            		cb.setText(recipe.getQus().get(i).getIngName());
            	else
            		cb.setText(amount+" "+recipe.getQus().get(i).getIngName());
            }
            else
            {
            	cb.setText(amount+" "+recipe.getQus().get(i).getType().toString()+" "
	            		+recipe.getQus().get(i).getIngName());
            }
            ll.addView(cb);
            
            cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
                {
                	TextView instrct = (TextView) findViewById(R.id.ingInstructions);
                	ScrollView sv = (ScrollView) findViewById(R.id.IngListScrollView);
                	
                	if(isChecked)
                	{
                		temp=temp+1;
                		int X = cbList.get(temp-1).getLeft();
                		int Y = cbList.get(temp-1).getBottom();
                		sv.scrollTo(X, Y);
                	}
                	else
                		temp=temp-1;
                	
                	if(temp == cbList.size())
                	{
                		b.setEnabled(true);
                		instrct.setText("Touch the above button to start cooking");
                	}
                	else
                	{
                		b.setEnabled(false);
                		instrct.setText(R.string.plz_get);
                	}
                }
            });       
            cbList.add(cb);									//end of adding checkboxes
        }
	}
	public void onButtonClick(View view)
	{
		Intent intent = new Intent(this,RecipeSteps.class);
		intent.putExtra("pickedRecipe",chosenRec);	
		startActivity(intent);	
	}
	
}