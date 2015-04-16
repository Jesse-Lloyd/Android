package com.example.activities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.testapp.DatabaseHandler;
import com.example.testapp.GeneralDialogs;
import com.example.testapp.Ingredient;
import com.example.testapp.R;

public class AddIngredient extends Activity
{
	private DatabaseHandler db = new DatabaseHandler(this);
	private final int SELECT_PHOTO = 100;
	static String imgDir,imgName;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingredient);
		
		if(imgDir != null)
			setImageView(imgDir);
	}
	public void selectImage(View view)
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent,SELECT_PHOTO);
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent imageReturn)
	{
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
			        imgDir = cursor.getString(columnIndex);
				    File f = new File(imgDir);
				    imgName = f.getName().trim();
				    setImageView(imgDir);
			        cursor.close();
				}
		}
	}
//----------------------------------------------------	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.add_ingredient_menu, menu);
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
			case R.id.add_ingredient_help:
				temp.help(8);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
//-------------------------------------------------------
	private void setImageView(String dir)
	{
		ImageView iv = (ImageView) findViewById(R.id.display_ing_img);
		File filepath = new File(dir);
		Bitmap image = BitmapFactory.decodeFile(filepath.toString());
		int resize = (int) (image.getHeight() * (512.0 / image.getWidth()));
		Bitmap scaledImage = Bitmap.createScaledBitmap(image, 512, resize, true);
		iv.setImageBitmap(scaledImage);
	}
	public void finalize(View view)
	{
		boolean addIngredient=true;
		EditText et = (EditText) findViewById(R.id.set_ing_name);
		String ingName = et.getText().toString();
		if(ingName.compareTo("")==0)
		{
			Toast.makeText(this, "The ingredient needs a name", Toast.LENGTH_SHORT).show();
			addIngredient=false;
		}
		else
		{
			for(int i=0;i<db.getAllIngredients().size()-1;i++)
			{
				if(ingName.toLowerCase(Locale.US).compareTo(db.getIngredient(i+1).getName().toLowerCase(Locale.US))==0)
				{
					Toast.makeText(this, "That ingredient already exists change the name if you still want to add the ingredient", Toast.LENGTH_SHORT).show();
					addIngredient=false;
				}
			}
		}
		if(addIngredient)
		{
			if(imgDir != null && imgName != null)
			{
				if(copyIngredientImageFile(imgDir,imgName))
				{
					Ingredient ing = new Ingredient(db.getAllIngredients().size(),ingName,imgName,false);
					db.addIngredient(ing);
					Continue();
					imgDir=null;
					imgName=null;
				}
				else
					Toast.makeText(this, "failed to copy ingredient image, ingredient not added to database", Toast.LENGTH_LONG).show();
			}
			else
			{
				Ingredient ing = new Ingredient(db.getAllIngredients().size(),ingName,"",false);
				db.addIngredient(ing);
				Continue();
				imgDir=null;
				imgName=null;
			}
		}
		
	}
	private void Continue()
	{
		db.getIngredient(db.getAllIngredients().size()-1);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	private boolean copyIngredientImageFile(String frm,String fileName)
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
}
