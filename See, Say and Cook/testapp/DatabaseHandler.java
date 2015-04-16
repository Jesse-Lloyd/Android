package com.example.testapp;
/*
 * TODO
 * Create better method for timers
 * Import export method
 * 
 * 
 * 
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper{
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "SeeSayCook";
 
    // Table names
    private static final String TABLE_Recipe = "Recipes";
    private static final String TABLE_Step = "Steps";
    private static final String TABLE_Hint = "Hints";
    private static final String TABLE_Quantity = "Quantities";
    private static final String TABLE_Ingredient = "Ingredients";
    //private static final String TABLE_Timers = "Timers";
    private static final String TABLE_Categories = "Categories";
    private static final String TABLE_CatToRecipe = "CatRecipe";
 
    // Recipes Table Columns names
    private static final String RecipeID_PK = "rec_ID";
    private static final String RecipeName = "rec_name";
    private static final String RecipeDescription = "rec_descript";
    private static final String RecipeFinalImage = "rec_finalImg";
    private static final String RecipeImageInternal = "rec_img_internal";
    private static final String RecipeServes = "rec_serves";
    private static final String RecipeTime = "rec_time";
    private static final String RecipeVersion = "rec_version";
    
    // Steps Table Columns names
    private static final String StepsID_PK = "step_ID";
    private static final String StepRecID_FK = "step_rec_ID";
    private static final String StepDetails = "step_details";
    private static final String StepMedia = "step_media";
    private static final String StepNo = "step_num";
    private static final String StepImageInternal = "step_img_internal";
    private static final String TIMERTIME = "timer_length";
    
    // Hints Table Columns names
    private static final String HintID_PK = "hint_id";
    private static final String HintStepID_FK = "hint_step_ID";
    private static final String HintDetails = "hint_details";
    private static final String HintNo = "hint_num";
    
    // Ingredients Table Columns names
    private static final String ingID_PK = "ing_ID";
    private static final String IngName = "ing_name";
    private static final String IngImage = "ing_img";
    private static final String IngImageInternal = "ing_img_internal";
    private static final String IngVersion = "ing_version";
    
    // Quantity Table Columns names
    private static final String QUID_pk = "qu_id";
    private static final String QURecipe_FK = "qu_rec_ID";
    private static final String QUIng_FK = "qu_ing_ID";
    private static final String QUMeasureType = "qu_measureType";
    private static final String QUAmount = "qu_amount";
    
    /*
    private static final String TIMER_ID_PK = "timer_id";
    private static final String TIMERRecipe_FK = "timer_rec_ID";
    private static final String TIMERStepStart = "timer_start";
    private static final String TIMERStepFin = "time_fin";
    private static final String TIMERTIME = "timer_length";
    */
    
    private static final String CatID_pk = "cat_id";
    private static final String CatName = "cat_name";
    
    private static final String CatRecID_pk = "cat_rec_id";
    private static final String CatRecCat_fk = "cat_rec_cat_ID";
    private static final String CatRecRec_fk = "cat_rec_rec_ID";
    
    /************************************************************************************************/
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_Recipe + "("
                + RecipeID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," + RecipeName + " TEXT,"
                + RecipeDescription + " TEXT," + RecipeFinalImage + " Text, "
                + RecipeImageInternal + " INTEGER, "
                + RecipeServes + " INTEGER, "
                + RecipeVersion + " INTEGER, "
                + RecipeTime + " INTEGER" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);
        
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_Ingredient + "("
        		+ ingID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," + IngName + " TEXT,"
        		+ IngImageInternal + " INTEGER, "
        		+ IngVersion + " INTEGER, "
                + IngImage + " Text" + ")";
        db.execSQL(CREATE_INGREDIENT_TABLE);
        
        String CREATE_STEP_TABLE = "CREATE TABLE " + TABLE_Step + "("
                + StepsID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," + StepDetails + " TEXT,"
                + StepNo + " INTEGER, "
                + StepImageInternal + " INTEGER, "
                + TIMERTIME + " INTEGER, "
                + StepMedia + " TEXT," + StepRecID_FK +" INTEGER, "+"FOREIGN KEY (" + StepRecID_FK + " ) REFERENCES " 
                + TABLE_Recipe + "(" + RecipeID_PK + ") " + ")";
        db.execSQL(CREATE_STEP_TABLE);
        
        String CREATE_HINT_TABLE = "CREATE TABLE " + TABLE_Hint + "("
        		+ HintID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ HintNo + " INTEGER, "
        		+ HintDetails + " TEXT,"+ HintStepID_FK + " INTEGER, " + "FOREIGN KEY(" + HintStepID_FK + " ) REFERENCES " 
                + TABLE_Step + "(" + StepsID_PK + ") " + ")";
        db.execSQL(CREATE_HINT_TABLE);
        
        String CREATE_QUANTITY_TABLE = 
        		 "CREATE TABLE " + TABLE_Quantity + "( "
        		 + QUID_pk + " INTEGER PRIMARY KEY AUTOINCREMENT, "                     
        		 + QUMeasureType + " TEXT, "
        		 + QUAmount + " REAL, " 
        		 + QUIng_FK + " INTEGER, "
        		 + QURecipe_FK + " INTEGER, "
        		 + " FOREIGN KEY("+ QUIng_FK + ") REFERENCES " + TABLE_Ingredient + "("+ ingID_PK + "),"
        		 + " FOREIGN KEY("+ QURecipe_FK + ") REFERENCES " + TABLE_Recipe + "(" + RecipeID_PK + ")" + ")";          
        		          
        db.execSQL(CREATE_QUANTITY_TABLE);
        
        /*String CREATE_TIMER_TABLE = 
        		"CREATE TABLE " + TABLE_Timers + "( "
        		+ TIMER_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        		+ TIMERStepStart + " INTEGER, "
        		+ TIMERStepFin + " INTEGER, "
        		+ TIMERTIME + " INTEGER, "
        		+ TIMERRecipe_FK + " INTEGER,"
        		+ " FOREIGN KEY("+ TIMERRecipe_FK + ") REFERENCES " + TABLE_Recipe + "(" + RecipeID_PK + ")" + ")";
        db.execSQL(CREATE_TIMER_TABLE);*/
        
        String CREATE_Category_TABLE = 
        		"CREATE TABLE " + TABLE_Categories + "( "
        		+ CatID_pk + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        		+ CatName + " Text " + ")";
        db.execSQL(CREATE_Category_TABLE);
        
        String CREATE_CategoryToRecipe_TABLE = 
        		"CREATE TABLE " + TABLE_CatToRecipe + "( "
        		+ CatRecID_pk + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        		+ CatRecCat_fk + " INTEGER, "
        		+ CatRecRec_fk + " INTEGER, " 
        		+ " FOREIGN KEY("+ CatRecCat_fk + ") REFERENCES " + TABLE_Categories + "(" + CatID_pk + "), "
        		+ " FOREIGN KEY("+ CatRecRec_fk + ") REFERENCES " + TABLE_Recipe + "(" + RecipeID_PK + ")" + ")";
        db.execSQL(CREATE_CategoryToRecipe_TABLE);
        
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Hint + ", " + TABLE_Step + ", " 
        + TABLE_Quantity + ", " + TABLE_Ingredient + ", " + TABLE_Recipe );
 
        // Create tables again
        onCreate(db);
	
    }
    /************************************************************************************************/
    // Recipe Alterations
    // Adding new Recipe
    public void addRecipe(Recipe recipe) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
   	 
        ContentValues values = new ContentValues();
        
        values.put(RecipeName, recipe.getName()); // Recipe Name
        values.put(RecipeDescription, recipe.getDescription()); // Recipe Description
        values.put(RecipeFinalImage, recipe.getImg()); // Recipe image
        values.put(RecipeImageInternal, recipe.isImageInternal());
        values.put(RecipeServes, recipe.getServings());
        values.put(RecipeTime, recipe.getTimeEst());
        values.put(RecipeVersion, 1);
        
        long recID = db.insert(TABLE_Recipe, null, values);
        
        if(!recipe.getSteps().isEmpty())
        {
        	for(int i = 0; i < recipe.getSteps().size(); i++)
        	{
        		long stepID = addStep(db ,recipe.getStep(i), recID);
        	        	
        		if(!recipe.getStep(i).getHints().isEmpty())
        		{
        			for(int j = 0; j < recipe.getStep(i).getHints().size(); j++)
        			{
        				addHint(db, recipe.getStep(i).getHint(j), stepID);
        			}
        		}
        	}
        }
        
        if(!recipe.getCategories().isEmpty())
        {
        	for(int i = 0; i < recipe.getCategories().size(); i++)
        	{
        		addRecipeCategory(db, recipe.getCategory(i), recID);
        	}
        }
        
        if(!recipe.getQus().isEmpty())
        {
        	for (int i = 0; i < recipe.getQus().size(); i++)
        	{
        		addQuantity(db, recipe.getQu(i), recID);
        	}
        }
        db.close(); // Closing database connection
    }
     
    public long addStep(SQLiteDatabase db,Step step, long recID)
    {
    	ContentValues temp = new ContentValues();
    	temp.put(StepDetails,step.getDetails());
		temp.put(StepMedia, step.getMedia());
		temp.put(StepRecID_FK, recID);
		temp.put(StepNo, step.getStepNo());
		temp.put(StepImageInternal, step.isImageInternal());
		temp.put(TIMERTIME, step.getTimer());
		
		return db.insert(TABLE_Step, null, temp);		
    }
    
    public void addHint(SQLiteDatabase db,Hint hint, long stepID)
    {
    	ContentValues temp = new ContentValues();
    	temp.put(HintDetails,hint.getDetails());
		temp.put(HintStepID_FK, stepID);
		temp.put(HintNo, hint.getHintNo());
		
		db.insert(TABLE_Hint, null, temp);		
    }
    
    public void addQuantity(SQLiteDatabase db,Quantity qu, long recID)
    {
    	ContentValues temp = new ContentValues();
    	
    	temp.put(QURecipe_FK, recID);
    	temp.put(QUIng_FK, qu.getIngID());
		temp.put(QUMeasureType, qu.getType().toString());
		temp.put(QUAmount, qu.getAmount());

		db.insert(TABLE_Quantity, null, temp);
		
    }
    
    public void addRecipeCategory(SQLiteDatabase db,Category cat, long recID)
    {
    	ContentValues temp = new ContentValues();
    	
    	temp.put(CatRecRec_fk, recID);
    	temp.put(CatRecCat_fk, cat.getID());

		db.insert(TABLE_CatToRecipe, null, temp);		
    }
    
    // Getting single Recipe
    public Recipe getRecipe(int id) 
    {
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_Recipe, new String[] { RecipeID_PK,
        		RecipeName, RecipeDescription, RecipeFinalImage, RecipeImageInternal, RecipeServes, RecipeTime, RecipeVersion }, RecipeID_PK + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();  
        Boolean bool = ( Integer.parseInt(cursor.getString(4))!= 0);
        Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), bool, 
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
   
        // Select All associated steps Query
        ArrayList<Step> stepList = new ArrayList<Step>();
        String selectStepsQuery = "SELECT  " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " 
        		+ StepMedia + "," + StepNo + ", "+ StepImageInternal + "," + TIMERTIME +" FROM " + TABLE_Step + " WHERE " + StepRecID_FK
        		+ " = " + id;
        Cursor stepCursor = db.rawQuery(selectStepsQuery, null);
        
        // looping through all rows and adding to list
        stepCursor.moveToFirst();
        if (stepCursor.getCount()>0 && stepCursor!=null) 
        {
            do 
            {
                boolean stepTemp = ( Integer.parseInt(stepCursor.getString(5))!= 0);
                
            	Step steps = new Step(Integer.parseInt(stepCursor.getString(0)), Integer.parseInt(stepCursor.getString(1)), 
            			stepCursor.getString(2), stepCursor.getString(3), Integer.parseInt(stepCursor.getString(4)), stepTemp, 
            			Integer.parseInt(stepCursor.getString(6)));
                
                ArrayList<Hint> hintList = new ArrayList<Hint>();
                String selectHintsQuery = "SELECT " + HintID_PK + ", " + HintStepID_FK + ", " + HintDetails + ", " 
        		+ HintNo +  " FROM " + TABLE_Hint + " WHERE " + HintStepID_FK
                		+ " = " + steps.getID();
                Cursor hintCursor = db.rawQuery(selectHintsQuery, null);
                
                // looping through all rows and adding to list
                hintCursor.moveToFirst();
                if (hintCursor.getCount()>0 && stepCursor!=null) {
                    do {
                        Hint hints = new Hint(Integer.parseInt(hintCursor.getString(0)), Integer.parseInt(hintCursor.getString(1)), 
                        		hintCursor.getString(2),  Integer.parseInt(hintCursor.getString(3)));
                        // Adding step to list
                        hintList.add(hints);                        
                    } while (hintCursor.moveToNext());
                    steps.setHints(hintList);
                }
                // Adding step to list
                stepList.add(steps);
                
            } while (stepCursor.moveToNext());
            recipe.setSteps(stepList);
        }
            
            
        Quantity.MeasureType mtTemp;
        ArrayList<Quantity> quList = new ArrayList<Quantity>();

        String selectQuQuery = "SELECT  " + QUID_pk + ", " + QURecipe_FK + ", " + QUIng_FK + ", " 
        		+ QUMeasureType + "," + QUAmount + " FROM " + TABLE_Quantity + " WHERE " + QURecipe_FK
        		+ "=" + id +";";
        Cursor quCursor = db.rawQuery(selectQuQuery, null);
        quCursor.moveToFirst();
        if (quCursor.getCount()>0)
        {        	
        	do 
        	{
        		
        		String selectIngQuery = "SELECT  " + IngName + ", "
        				+ IngImage + ", " + IngImageInternal + " FROM " + TABLE_Ingredient + " WHERE " + ingID_PK
                		+ " = " + quCursor.getString(2) +";";
        		
            	Cursor ingCursor = db.rawQuery(selectIngQuery, null);
       
        		ingCursor.moveToFirst();
        		if(ingCursor.getCount()>0)
        		{
        			mtTemp = Quantity.MeasureType.valueOf(quCursor.getString(3));

        			Boolean imgBool = ( Integer.parseInt(ingCursor.getString(2))!= 0);
        			
        			Quantity quantity = new Quantity(Integer.parseInt(quCursor.getString(0)),
        					Integer.parseInt(quCursor.getString(1)), Integer.parseInt(quCursor.getString(2)), mtTemp, 
        					Float.parseFloat(quCursor.getString(4)),ingCursor.getString(0), ingCursor.getString(1), imgBool);
			
        			quList.add(quantity);
        		}
        	} while (quCursor.moveToNext());
        	recipe.setQus(quList);
        }
        
        ArrayList<Category> cats = new ArrayList<Category>();
        String selectCatQuery = "SELECT " + CatRecID_pk + ", " + CatRecCat_fk + ", " + CatName
        		+ " FROM " + TABLE_Categories +  ", " + TABLE_CatToRecipe
        		+ " Where " + id + " = " + CatRecRec_fk + " AND " + CatRecCat_fk + " = " + CatID_pk;
        
        Cursor catCursor = db.rawQuery(selectCatQuery, null);
        catCursor.moveToFirst();
        if (catCursor.getCount() > 0)
        {
        	do
        	{
        		Category cat = new Category(Integer.parseInt(catCursor.getString(0)),
        				Integer.parseInt(catCursor.getString(1)), catCursor.getString(2));
        		cats.add(cat);
        	}while(catCursor.moveToNext());
        	recipe.setCategories(cats);
        }
        
        //db.close();
        return recipe;
    }  
    
    // get all recipes by category
    public ArrayList<Recipe> getRecipesByCategory(int cat)
    {
    	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT " + RecipeID_PK  + ", " + RecipeName + ", " + RecipeDescription  + ", " 
        + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion + " FROM " + TABLE_Recipe+ ", " 
        		+ TABLE_CatToRecipe + " Where " + RecipeID_PK + " = " + CatRecRec_fk + " AND " + CatRecCat_fk + " = " + cat + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Boolean bool = ( Integer.parseInt(cursor.getString(4))!= 0);
                Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), bool, 
                        Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
            	recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        db.close();
        
        return recipeList;
    }
    
    
    // gets all recipes by title
    public ArrayList<Recipe> getAllByNameSearch(String name) 
    {
    	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT " + RecipeID_PK  + ", " + RecipeName + ", " + RecipeDescription  + ", " 
        + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion+ " FROM " + TABLE_Recipe + " WHERE " + RecipeName + " LIKE "
        + "'%" + name + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Boolean bool = ( Integer.parseInt(cursor.getString(4))!= 0);
                Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), bool, 
                        Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
            	recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        db.close();
        
        return recipeList;
    }
    
 // gets all recipes by Ingredients
    public ArrayList<Recipe> getAllRecipesByIngredients(ArrayList<Ingredient> ing, boolean MatchAll) 
    {
    	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT " + RecipeID_PK  + ", " + RecipeName + ", " + RecipeDescription  + ", " 
        + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion + " FROM " + TABLE_Recipe + ", " + TABLE_Quantity + " WHERE ";
        for(int i = 0; i < ing.size(); i++)
        {
        	selectQuery += "(" + RecipeID_PK + " = " + QURecipe_FK + " AND " + QUIng_FK + " = " +ing.get(i).getID() + ")";
        	if(i + 1 <  ing.size())
        		if(MatchAll)
        			selectQuery += " AND ";
        		else
        			selectQuery += " OR ";
        	Log.d("Run", "True");
        }
        selectQuery += ";";
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("return count", Integer.toString(cursor.getCount()));
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Boolean bool = ( Integer.parseInt(cursor.getString(4))!= 0);
                Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), bool, 
                        Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
            	recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        db.close();
        
        return recipeList;
    }    
    
    
    // Getting All Recipe
    public ArrayList<Recipe> getAllRecipes() 
    {
    	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT " + RecipeID_PK  + ", " + RecipeName + ", " + RecipeDescription  + ", " 
        + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime+ ", " + RecipeVersion + " FROM " + TABLE_Recipe;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Boolean bool = ( Integer.parseInt(cursor.getString(4))!= 0);
                Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), bool, 
                        Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));
            	recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        
        return recipeList;
    }
     
    // Getting Recipe Count
    public int getRecipeCount() 
    {
    	String countQuery = "SELECT  * FROM " + TABLE_Recipe;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        
        db.close();
        // return count
        return cursor.getCount();
    }
    
    // Updating single Recipe
    public int updateRecipe(Recipe recipe) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
        ContentValues values = new ContentValues();
        values.put(RecipeName, recipe.getName());
        values.put(RecipeFinalImage, recipe.getImg());
        values.put(RecipeDescription, recipe.getDescription());
        values.put(RecipeVersion, recipe.getVersion() + 1);
        values.put(RecipeTime, recipe.getTimeEst());
        values.put(RecipeServes, recipe.getServings());
        values.put(RecipeImageInternal, recipe.isImageInternal());
        
        db.update(TABLE_Recipe, values, RecipeID_PK + " = ?",
                new String[] { String.valueOf(recipe.getID()) });        
        
        
        if (!recipe.getDeletedStepList().isEmpty())
        {
        	for(int i = 0; i < recipe.getDeletedStepList().size(); i++)
        	{
        		deleteStep(recipe.getDeletedStep(i), db);
        	}
        }
        
        if(!recipe.getDeletedQuList().isEmpty())
        {
        	for(int i = 0; i < recipe.getDeletedQuList().size(); i++)
        	{
        		deleteQuantity(recipe.getDeletedQu(i), db);
        	}
        }
        
        if(!recipe.getDeletedCategoryList().isEmpty())
        {
        	for(int i = 0; i < recipe.getDeletedCategoryList().size(); i++)
        	{
        		deleteRecipeCategory(recipe.getDeletedCat(i), db);
        	}
        }
        
        if(!recipe.getCategories().isEmpty())
        {
        	for(int i = 0; i < recipe.getCategories().size(); i++)
        	{
        		if(recipe.getCategory(i).getCatID_PK() != 0 )
        		{
        			ContentValues CTemp = new ContentValues();
        	    	CTemp.put(CatRecCat_fk, recipe.getCategory(i).getID());
        	    	db.update(TABLE_Quantity, CTemp, CatRecID_pk + " = ?",
	        				new String[] { String.valueOf(recipe.getCategory(i).getCatID_PK()) });
        		}
        		else
        			addRecipeCategory(db, recipe.getCategory(i), recipe.getID());
        	}
        }
        
        if(!recipe.getQus().isEmpty())
        {
        	for(int i = 0; i < recipe.getQus().size(); i++)
        	{
        		if(recipe.getQu(i).getID() != 0)
        		{
        			ContentValues QTemp = new ContentValues();
        			QTemp.put(QUIng_FK, recipe.getQu(i).getIngID());
        			QTemp.put(QUMeasureType, recipe.getQu(i).getType().toString());
	        		QTemp.put(QUAmount, recipe.getQu(i).getAmount());

	        		db.update(TABLE_Quantity, QTemp, QUID_pk + " = ?",
	        				new String[] { String.valueOf(recipe.getQu(i).getID()) });
        		}
        		else
        			addQuantity(db, recipe.getQu(i), recipe.getID());
        	}	
        }
        // if there is Hints and steps this part checks whether the steps passed are to be updated or inserted
        
        
        if(!recipe.getSteps().isEmpty())
        {
        	for (int i = 0; i < recipe.getSteps().size(); i++)
            {
        		if(!recipe.getStep(i).getDeletedHintsList().isEmpty())
        		{
        			for(int f = 0; f < recipe.getStep(i).getDeletedHintsList().size(); f++)
        			{
        				deleteHint(recipe.getStep(i).getDeletedHint(f), db);
        			}
        		}        			
            	if(recipe.getStep(i).getID() == 0)
            	{
            		//Insert statement       
            		long stepID = addStep(db, recipe.getStep(i), recipe.getID());
                	
            		if(!recipe.getStep(i).getHints().isEmpty())
            		{
            			for(int j = 0; j < recipe.getStep(i).getHints().size(); j++)
                		{	
            				//Insert Hint
            				addHint(db, recipe.getStep(i).getHint(j), stepID);
                		}     		
            		}            		
            	}
            	else
            	{
            		//update statement
            		ContentValues stepVal = new ContentValues();
                	stepVal.put(StepRecID_FK, recipe.getStep(i).getRecID());
                	stepVal.put(StepDetails, recipe.getStep(i).getDetails());
                	stepVal.put(StepMedia, recipe.getStep(i).getMedia());
                	stepVal.put(StepNo, recipe.getStep(i).getStepNo());
                	stepVal.put(StepImageInternal, recipe.getStep(i).isImageInternal());
                	stepVal.put(TIMERTIME, recipe.getStep(i).getTimer());
                	
                	db.update(TABLE_Step, stepVal, StepsID_PK + " = ?",
                            new String[] { String.valueOf(recipe.getStep(i).getID()) });
            		if(!recipe.getStep(i).getHints().isEmpty())
            		{
            			for(int j = 0; j < recipe.getStep(i).getHints().size(); j++)
                		{
                			if(recipe.getStep(i).getHint(j).getHintStepID() == 0)
                			{
                				//Insert Hint
                				addHint(db, recipe.getStep(i).getHint(j), recipe.getStep(i).getID());
                			}
                			else
                			{
                				//update Hint
                				ContentValues hintVal = new ContentValues();
                        		hintVal.put(HintStepID_FK, recipe.getStep(i).getHint(j).getHintStepID());
                            	hintVal.put(HintDetails, recipe.getStep(i).getHint(j).getDetails());
                            	hintVal.put(HintNo, recipe.getStep(i).getHint(j).getHintNo());
                            	db.update(TABLE_Hint, hintVal, HintID_PK + " = ?",
                                        new String[] { String.valueOf(recipe.getStep(i).getHint(j).getHintID()) });
                			}
                		}
            		}            		
            	}
            }
        }        
        db.close();
        // updating row
        return 1;
    }
    
    // need to take into account on dependent data (foreign keys)
    // Deleting single Recipe
    public void deleteRecipe(Recipe recipe) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	for(int i = 0; i < recipe.getQus().size(); i++)
    	{
    		deleteQuantity(recipe.getQu(i), db);
    	}
    	for(int i = 0; i < recipe.getSteps().size(); i++)
    	{
    		deleteStep(recipe.getStep(i),db);
    	}
	    db.delete(TABLE_Recipe, RecipeID_PK + " = ?",
	            new String[] { String.valueOf(recipe.getID()) });
	    db.close();
    }
    
    public void deleteStep(Step step, SQLiteDatabase db) 
    {
    	
    	for(int i = 0; i < step.getHints().size(); i++)
    	{
    		deleteHint(step.getHint(i), db);
    	}
	    db.delete(TABLE_Step, StepsID_PK + " = ?",
	            new String[] { String.valueOf(step.getID()) });
    }
    
    public void deleteHint(Hint hint, SQLiteDatabase db) 
    {
    	db.delete(TABLE_Hint, HintID_PK + " = ?",
	            new String[] { String.valueOf(hint.getHintID()) });
    }
    
    public void deleteQuantity(Quantity quantity, SQLiteDatabase db) 
    {
    	db.delete(TABLE_Quantity, QUID_pk + " = ?",
	            new String[] { String.valueOf(quantity.getID()) });
    }
    
    public void deleteRecipeCategory(Category cat, SQLiteDatabase db)
    {
    	db.delete(TABLE_CatToRecipe, CatRecID_pk + " = ?",
	            new String[] { String.valueOf(cat.getCatID_PK()) });
    }
    
    public ArrayList<Category> getCategoryList()
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	ArrayList<Category> cats = new ArrayList<Category>();
    	
    	String getCategories = "SELECT " + CatID_pk+", "+CatName + " FROM " + TABLE_Categories;
    	
    	Cursor cursor = db.rawQuery(getCategories, null);
        
		cursor.moveToFirst();
		if(cursor.getCount()>0)
		{
			do
			{
				Category cat = new Category();
				cat.setID(Integer.parseInt(cursor.getString(0)));
				cat.setName(cursor.getString(1));
				cats.add(cat);
			}while(cursor.moveToNext());
		}
		db.close();
		return cats;
    }
    
    /************************************************************************************************/
    // Ingredient Alterations
    // Adding new Ingredient
    public void addIngredient(Ingredient ingredient) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(IngName, ingredient.getName()); // Ingredient Name
        values.put(IngImage, ingredient.getImg()); // Ingredient image name
        values.put(IngImageInternal, ingredient.isImageInternal());
        values.put(IngVersion, 1);
        // Inserting Row
        db.insert(TABLE_Ingredient, null, values);
        db.close(); // Closing database connection
    }
     
    // Getting single Ingredient
    public Ingredient getIngredient(int id) 
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.query(TABLE_Ingredient, new String[] { ingID_PK,
        		IngName, IngImage, IngImageInternal }, ingID_PK + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        
        Boolean bool = ( Integer.parseInt(cursor.getString(3))!= 0);
        Ingredient ingredient = new Ingredient(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), bool);
        
        db.close();
        return ingredient;
    }
     
    // Getting All Ingredients
    public ArrayList<Ingredient> getAllIngredients() 
    {
    	ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        // Select All Query
        String selectQuery = "SELECT " + ingID_PK+ ", "+ IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion + " FROM " + TABLE_Ingredient;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setID(Integer.parseInt(cursor.getString(0)));
                ingredient.setName(cursor.getString(1));
                ingredient.setImg(cursor.getString(2));
                ingredient.ImageExternal(Integer.parseInt(cursor.getString(3))!= 0);
                ingredient.setVersion(Integer.parseInt(cursor.getString(4)));
                ingredientList.add(ingredient);
            } while (cursor.moveToNext());
        }   
        return ingredientList;
    }
     
    // Getting Ingredients Count
    public int getIngredientCount() 
    {
    	String countQuery = "SELECT  * FROM " + TABLE_Ingredient;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
    // Updating single contact
    public int updateIngredient(Ingredient ingredient) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(IngName, ingredient.getName());
        values.put(IngImage, ingredient.getImg());
        values.put(IngImageInternal, ingredient.isImageInternal());
        values.put(IngVersion, ingredient.getVersion() + 1);
        db.close();
        // updating row
        return db.update(TABLE_Ingredient, values, ingID_PK + " = ?",
                new String[] { String.valueOf(ingredient.getID()) });
    }
     
    // Deleting single contact
    public void deleteIngredient(Ingredient ingredient) 
    {
    	 SQLiteDatabase db = this.getWritableDatabase();
    	    db.delete(TABLE_Ingredient, ingID_PK + " = ?",
    	            new String[] { String.valueOf(ingredient.getID()) });
    	    db.close();
    }
    /**
     * @param s **********************************************************************************************/
    
   
    
    // checks whether imported recipes ingredient list matches you own and makes any small changes if necessary
    // returns true if all ingredients recipe requires are present, false if they are not
    public boolean importRecipe(Recipe recipe)
    {
    	ArrayList<Ingredient> ingredients = getAllIngredients();
    	for(int i = 0; i < recipe.getQus().size(); i++)
    	{
    		int match = 0;
    		for(int j = 0; j < ingredients.size(); j++)
    		{
    			if(recipe.getQu(i).getIngName().equals(ingredients.get(i).getName()))
    			{
    				recipe.getQu(i).setID(ingredients.get(i).getID());
    				match++;
    			}
    		}
    		if(match == 0)
    			return false;
    	}
    	
    	return true;
    }
    
    /************************************************************************************************/
    
    public void populateTable()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	String populateHints;
    	String populateIngredients;
    	String populateRecipes;
    	String populateQuantities;
    	String populateSteps;
    	//String populateTimers;
    	String populateCategories;
    	String populateCatToRecipes;
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateCategories = "INSERT INTO " + TABLE_Categories + "(" + CatID_pk + ", " + CatName + ") " +
    	        "VALUES (" + 1 + ", " + "'Breakfast'" + ") "; 	
    	db.execSQL(populateCategories);
    	populateCategories = "INSERT INTO " + TABLE_Categories + "(" + CatID_pk + ", " + CatName + ") " +
    	        "VALUES (" + 2 + ", " + "'Lunch'" + ") "; 	
    	db.execSQL(populateCategories);
    	populateCategories = "INSERT INTO " + TABLE_Categories + "(" + CatID_pk + ", " + CatName + ") " +
    	        "VALUES (" + 3 + ", " + "'Snack'" + ") "; 	
    	db.execSQL(populateCategories);
    	populateCategories = "INSERT INTO " + TABLE_Categories + "(" + CatID_pk + ", " + CatName + ") " +
    	        "VALUES (" + 4 + ", " + "'Dinner'" + ") "; 	
    	db.execSQL(populateCategories);
    	populateCategories = "INSERT INTO " + TABLE_Categories + "(" + CatID_pk + ", " + CatName + ") " +
    	        "VALUES (" + 5 + ", " + "'Dessert'" + ") "; 	
    	db.execSQL(populateCategories);
    	db.execSQL("END TRANSACTION");
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (1,'Baking Rice', 'bakingrice', 1 , 1) "; 	
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (2,'Basil Leaves', 'basilleaves', 1 , 1) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (3,'Butter', 'butter', 1 , 1 ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (4,'Capsicum', 'capsicum', 1 , 1 ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (5,'Carrot', 'carrot', 1 , 1 ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (6,'Chicken Breast', 'chickenbreast', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (7,'Chilli Red', 'chillired', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (8,'Chopped Herbs', 'choppedherbs', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (9,'Cream', 'cream', 1 , 1  ) ";
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (10,'Cream Pure', 'creampure', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (11,'dremerara Sugar', 'demerarasugar', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (12,'Eggs', 'eggs', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (13,'Flour Tortillas', 'flourtortillas', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (14,'Garlic', 'garlic', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (15,'Ginger', 'ginger', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (16,'Grain Rice', 'grainrice', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (17,'Icing Sugar', 'icingsugar', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (18,'Jam', 'jam', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (19,'Lemons', 'lemons', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (20,'Lettuce', 'lettuce', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (21,'Mesculin', 'mesculin', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (22,'Milk', 'milk', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (23,'Mushroom', 'mushroom', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (24,'Oil', 'oil', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (25,'Oil Olive', 'oilolive', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (26,'Oil Sesame', 'oilsesame', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (27,'Parmesan', 'parmesan', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (28,'Parsley', 'parsley', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (29,'Pastry', 'pastry', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (30,'Potatoes', 'potatoes', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (31,'Ricotta', 'ricotta', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (32,'Salt and Pepper', 'saltandpepper', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (33,'Self Raising Flour', 'selfraisingflour', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (34,'Sour Cream', 'sourcream', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (35,'Soy Sauce', 'soysauce', 1, 1   ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (36,'Spaghetti Dried', 'spaghettidried', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (37,'Sultanas', 'sultanas', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (38,'Water', 'water', 1 , 1  ) ";    	
    	db.execSQL(populateIngredients);
    	populateIngredients = "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (39,'Baking Rice', 'bakingrice', 1 , 1  ) " ;
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (40,'Cheese', 'cheese', 1 , 1  ) ";
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (41,'Tomato', 'tomato', 1 , 1  ) ";
    	db.execSQL(populateIngredients); 
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (42,'Onions', 'onion', 1 , 1  ) ";
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (43,'Ham', 'ham', 1 , 1  ) ";
    	db.execSQL(populateIngredients);
    	populateIngredients =  "INSERT INTO " + TABLE_Ingredient + "(" + ingID_PK + ", " + IngName + ", " + IngImage + ", " + IngImageInternal + ", " + IngVersion+") " +
    	        "VALUES (44,'Black Peppercorns', 'blackpeppercorns', 1 , 1  ) ";
    	db.execSQL(populateIngredients);
    	db.execSQL("END TRANSACTION");
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (1, 'Baked Sliced Potatoes with Onion, Garlic and Cheese', " +
    					"'A tasty Potato Bake makes excellent comfort food for a cold night. The flavor and the crispy top is absolutely delicious.' ," +
    					"'bakedslicedpotatoeswithoniongarlicandcheese', 1, 2, 60, 1) ;";
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (2, 'Caramalized Onion and Ricotta', " +
    					"'Hmm, sweet caramelized onions balanced out by the smooth taste of ricotta all in a tart.' ," +
    					"'caramalizedonionandricotta', 1, 2, 60 , 1); " ;
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (3, 'Carbonara with Ham Mushroom and Cream', " +
    					"'This is a tasty Spaghetti dish in a rich creamy sauce with ham, mushroom and cheese.' ," +
    					"'carbonarawithhammushroomandcream', 1, 1, 60, 1); ";
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (4, 'Cheese Tomato and Mixed Herb Omelette', " +
    					"'Omelettes make a great breakfast lunch or dinner. This recipe is a good guide and you can choose your own filling.' ," +
    					"'cheesetomatoandmixedherbomlette', 1, 1, 20, 1 ); ";
    	db.execSQL(populateRecipes);				
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (5, 'Chicken Vegetable and Egg Fried Rice', " +
    					"'Why go out for Chinese when you can make your own and choose what to put in it. This tasty fried rice recipe can be treated as a guide and change it to your own liking.' ," +
    					"'chickenvegetableandeggfriedrice', 1, 1, 20, 1); ";
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (6, 'Peppered Chicken Quesadilla with Sour Cream', " +
    					"'Melted cheese, Zesty peppered Chicken strips make a fantastic snack for a group or a healthy meal.' ," +
    					"'pepperedchickenquesadillawithsourcream', 1, 2, 120, 1); ";
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (7, 'Sweet Scones with Jam and Cream', " +
    					"'These sweet Scones garnished with your favorite flavored Jam and cream make a tasty simple treat.' ," +
    					"'sweetsconeswithjamandcream', 1, 5, 40, 1); ";
    	db.execSQL(populateRecipes);
    	populateRecipes = "INSERT INTO " + TABLE_Recipe + "(" + RecipeID_PK + ", " + RecipeName + " ," + RecipeDescription 
    					+ ", " + RecipeFinalImage + ", " + RecipeImageInternal + ", " + RecipeServes + ", " + RecipeTime + ", " + RecipeVersion +")" +
    					"VALUES (8, 'Tomato Cheese and Mixed Herb Fritatta', " +
    					"'A frittata is a nice alternative to an omelette. Feel free to adjust to your preferred choice of filling.' ," +
    					"'tomatocheeseandmixedherbfritatta', 1, 1, 15, 1); ";	    		
    	db.execSQL(populateRecipes);
    	db.execSQL("END TRANSACTION");
        
    	db.execSQL("BEGIN TRANSACTION");
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 1 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 2 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 2 + ", " + 2 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 3 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 4 + ", " + 1 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 4 + ", " + 2 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 4 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 5 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 6 + ", " + 3 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 6 + ", " + 2 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 6 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 7 + ", " + 5 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 7 + ", " + 3 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 7 + ", " + 2 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 8 + ", " + 1 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 8 + ", " + 2 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	populateCatToRecipes = "INSERT INTO " + TABLE_CatToRecipe + "(" + CatRecRec_fk + ", " +CatRecCat_fk + ") " +
    	        "VALUES (" + 8 + ", " + 4 + ") "; 	
    	db.execSQL(populateCatToRecipes);
    	db.execSQL("END TRANSACTION");
    	
    	
    	// Omelete recipe ************************************************************************************************************************
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount  + ") "
    			+ "VALUES (" + 4 + ", " +  3 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  8 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  12 + ", " + "'Whole'" + ", " + 3 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  40 + ", " + "'g'" + ", " + 30 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  24 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  32 + ", " + "'Pinch'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 4 + ", " +  41 + ", " + "'Whole'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);   	
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	 +"VALUES (" + 1 + ", " + 4 + ", " + "'In a mixing bowl crack and beat the eggs for 30 seconds.'" + ", " + "'videocheesetomatoandmixedherbomlette1'" + ", " + 1 + ", " + 1 + ", " + 0 + ")"; 
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( "+ StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	 +"VALUES (" + 2 + ", " + 4 + ", " + "'Place small frying pan on MEDIUM heat.'" + ", " + "'videocheesetomatoandmixedherbomlette2'" + ", " + 2 + ", " + 1 + ", " + 0 + ")"; 
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( "+ StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	 +"VALUES (" + 3 + ", " + 4 + ", " + "'Increase stove element to a MEDIUM HIGH heat.'" + ", " + "'bakingrice'" + ", " + 3 + ", " + 1 + ", " + 0 +")"; 
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( "+ StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	 +"VALUES (" + 4 + ", " + 4 + ", " + "'Pour beaten eggs into pan.'" + ", " + "'videocheesetomatoandmixedherbomlette4'" + ", " + 4 + ", " + 1 + ", " + 0 + ")"; 
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( "+ StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	+"VALUES (" + 5 + ", " + 4 + ", " + "'Add the tomato, cheese and chopped herbs.'" + ", " + "'t'" + ", " + 5 + ", " + 1 + ", " + 0 + ")"; 
    	 db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( "+ StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    	  +"VALUES (" + 6 + ", " + 4 + ", " + "'To fold the Omelet slide your utensil under one half and fold it over.'" + ", " + "'videocheesetomatoandmixedherbomlette6'" + ", " + 6 + ", " + 1 + ", " + 0 + ")"; 
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 1 + ", " + "'Add salt and pepper.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 1 + ", " + "'Beat for another 30 seconds.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 2 + ", " + "'Once pan is hot brush vegetable oil over the frying pan.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 3 + ", " + "'Place butter in pan and allow to melt.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 4 + ", " + "'With one hand shake the pan and with the other hand stir the edges of the egg inwards.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 4 + ", " + "'When egg begins to set, lower the temperature to LOW.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 5 + ", " + "'Allow to heat for 30 seconds.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);    	
    	db.execSQL("END TRANSACTION");
    	
    	// Potatoe Bake ************************************************************************************************************************
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 9 + ", " + "'ml'" + ", " + 265 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 14 + ", " + "'Cloves'" + ", " + 2 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 40 + ", " + "'g'" + ", " + 75 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 30 + ", " + "'g'" + ", " + 400 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 20 + ", " + "'Whole'" + ", " + 1 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 24 + ", " + "'tbsp'" + ", " + 1 + ")";
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 1 + ", " + 32 + ", " + "'Pinch'" + ", " + 1 + ")";
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 7 + ", " + 1 + ", " +  "'Puree the garlic.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese1'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 8 + ", " + 1 + ", " +  "'Place cream in a pot.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese2'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 9 + ", " + 1 + ", " +  "'Place the pot on the element on LOW to warm up.'" + ", " + "'t'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 10 + ", " + 1 + ", " +  "'While cream is warming, slice the potatoes really thin.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese4'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 11 + ", " + 1 + ", " +  "'Once the cream is heated add the potatoes and the garlic.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese5'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 12 + ", " + 1 + ", " +  "'Spray oil into an oven safe fry pan.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese6'" + ", " + 6  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 13 + ", " + 1 + ", " +  "'Put half the potatoes in the frying pan and lay them evenly flat across the base.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese7'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 14 + ", " + 1 + ", " +  "'Add the rest of the potatoes.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese8'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 15 + ", " + 1 + ", " +  "'Place the oven proof pan/dish in center of the oven.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese9'" + ", " + 9  + ", " + 1 + ", " + 1800 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 16 + ", " + 1 + ", " +  "'Remove dish from the oven.'" + ", " + "'t'" + ", " + 10  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 17 + ", " + 1 + ", " +  "'Skewer the potatoes with a knife, if soft they are ready.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese11'" + ", " + 11  + ", " + 1 + ", " + 600 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 18 + ", " + 1 + ", " +  "'Leave to stand for 4-5 minutes.'" + ", " + "'videobakedslicedpotatoeswithoniongarlicandcheese12'" + ", " + 12  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 7 + ", " + "'Pinch of salt on the garlic Cloves.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 7 + ", " + "'Rub knife across until its a nice puree.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 8 + ", " + "'Add pinch of salt and pepper.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 10 + ", " + "'Use a knife or a mandolin to achieve 2mm cuts.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 11 + ", " + "'Add pinch of salt and pepper.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 11 + ", " + "'Heat to a slow simmer to allow soften the potatoes.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 13 + ", " + "'Sprinkle a layer of cheese across the potatoes base.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 13 + ", " + "'Sprinkle the parsley over the top.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 14 + ", " + "'Layer the potatoes evenly over the top.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 14 + ", " + "'Sprinkle more cheese over the top.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 15 + ", " + "'Leave for 30 minutes.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 17 + ", " + "'If the potatoes still have some are not softened place back in the oven for 10 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	
    	db.execSQL("END TRANSACTION");
    	
    	// Caramelized Onion and Ricotta *********************************************************************************************************************
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  1 + ", " + "'NOTYPE'" + ", " + 0 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  12 + ", " + "'NOTYPE'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  11 + ", " + "'g'" + ", " + 40 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  14 + ", " + "'Cloves'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  19 + ", " + "'NOTYPE'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  32 + ", " + "'Pinch'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  42 + ", " + "'g'" + ", " + 700 + ") "; 
    	db.execSQL(populateQuantities);   	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  31 + ", " + "'g'" + ", " + 80 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  29 + ", " + "'g'" + ", " + 100 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 2 + ", " +  25 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 49 + ", " + 2 + ", " +  "'Caramalized Onion Prep.'" + ", " + "'t'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 19 + ", " + 2 + ", " +  "'Place fry pan on a LOW heat.'" + ", " + "'t'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 20 + ", " + 2 + ", " +  "'Add onions and thyme and cook on LOW heat over the period of an hour stirring occasionally.'" + ", " + "'t'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 21 + ", " + 2 + ", " +  "'Heat oven to 180c on bake.'" + ", " + "'t'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 22 + ", " + 2 + ", " +  "'Pastry Preparation:'" + ", " + "'videocaramalizedonionandricotta5'" + ", " + 5  + ", " + 1 + ", " + 1200 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 23 + ", " + 2 + ", " +  "'Once the pastry has stood for 20 minutes:'" + ", " + "'videocaramalizedonionandricotta6'" + ", " + 6  + ", " + 1 + ", " + 1500 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 24 + ", " + 2 + ", " +  "'While waiting for the pastry, In a mixing bowl add the ricotta.'" + ", " + "'videocaramalizedonionandricotta7'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 25 + ", " + 2 + ", " +  "'Place the baby cos lettuce into a bowl and wash the lettuce with cold water and a pinch of salt.'" + ", " + "'videocaramalizedonionandricotta8'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 26 + ", " + 2 + ", " +  "'Once 25 minutes have passed, remove the pastry from the oven.'" + ", " + "'videocaramalizedonionandricotta9'" + ", " + 9  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 27 + ", " + 2 + ", " +  "'Remove the rice and paper from the pastry.'" + ", " + "'t'" + ", " + 10  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 28 + ", " + 2 + ", " +  "'Increase oven temperature to 190.'" + ", " + "'t'" + ", " + 11  + ", " + 1 + ", " + 5 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 29 + ", " + 2 + ", " +  "'After a further 5 minutes remove from oven.'" + ", " + "'t'" + ", " + 12  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 30 + ", " + 2 + ", " +  "'(Optional)'" + ", " + "'videocaramalizedonionandricotta14'" + ", " + 13  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 31 + ", " + 2 + ", " +  "'Spread the ricotta over the top of the onions.'" + ", " + "'videocaramalizedonionandricotta15'" + ", " + 14  + ", " + 1 + ", " + 240 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 32 + ", " + 2 + ", " +  "'Allow to cool for a further 3-4 minutes.'" + ", " + "'t'" + ", " + 15  + ", " + 1 + ", " + 240 + ")";
    	db.execSQL(populateSteps);
    	
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 49 + ", " + "'Slice onions to preferred thickness.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 49 + ", " + "'Chop thyme finely (leaves only).'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 19 + ", " + "'Melt the Butter.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 20 + ", " + "'Allow to cool.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 22 + ", " + "'Grease a small oven proof fry pan or medium pie dish with oil.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 22 + ", " + "'Line with puff pastry.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 22 + ", " + "'Let stand in the fridge for 20 minutes.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 23 + ", " + "'Stab the bottom of the pastry with a fork.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 23 + ", " + "'Line the pastry with baking paper and fill paper with baking rice.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 23 + ", " + "'Place pan in the oven for 25 minutes.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 24 + ", " + "'Add thyme leaves.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 24 + ", " + "'Grate (or super finely dice) lemon then add to the ricotta.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 24 + ", " + "'Add pinch of salt and cracked pepper.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 24 + ", " + "'Then beat thoroughly together.'" + ", " + 4 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 27 + ", " + "'Place the caramelized onions in the pan with the pastry.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 28 + ", " + "'Place back in the oven for 5-6 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 30 + ", " + "'Beat one egg.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 30 + ", " + "'Give the pastry an eggwash with beated egg.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 30 + ", " + "'Cook in the oven for a further 3-4 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	db.execSQL("END TRANSACTION");
    	
    	//Carbonara with ham, mushroom and parsley -------------------------------------------------------------------
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  3 + ", " + "'g'" + ", " + 30 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  43 + ", " + "'g'" + ", " + 100 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  9 + ", " + "'ml'" + ", " + 300 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  14 + ", " + "'Cloves'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  36 + ", " + "'g'" + ", " + 250 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  27 + ", " + "'g'" + ", " + 25 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  23 + ", " + "'g'" + ", " + 150 + ") "; 
    	db.execSQL(populateQuantities);   	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  42 + ", " + "'g'" + ", " + 75 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  28 + ", " + "'tbsp'" + ", " + 3 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 3 + ", " +  32 + ", " + "'Pinch'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 50 + ", " + 3 + ", " +  "'Preparation:'" + ", " + "'videocarbonarawithhammushroomandcream1'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 33 + ", " + 3 + ", " +  "'Pasta:'" + ", " + "'videocarbonarawithhammushroomandcream2'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 34 + ", " + 3 + ", " +  "'When the water is at a rolling boil place in the pasta with some sticking out.'" + ", " + "'t'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 35 + ", " + 3 + ", " +  "'Drain pasta and add 2tbsp oil, cover and set aside. '" + ", " + "'t'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 36 + ", " + 3 + ", " +  "'Bring a fry pan to a MEDIUM heat.'" + ", " + "'t'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 37 + ", " + 3 + ", " +  "'Place some Olive Oil into the fry pan.'" + ", " + "'videocarbonarawithhammushroomandcream6'" + ", " + 6  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 38 + ", " + 3 + ", " +  "'Add mushrooms and constantly stir as not to burn mushrooms until softened. About 2 minutes.'" + ", " + "'videocarbonarawithhammushroomandcream7'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 39 + ", " + 3 + ", " +  "'Add parsley.'" + ", " + "'videocarbonarawithhammushroomandcream8'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 40 + ", " + 3 + ", " +  "'Add pasta to frying pan that has the sauce you just made.'" + ", " + "'videocarbonarawithhammushroomandcream9'" + ", " + 9  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 50 + ", " + "'Finely dice onion.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 50 + ", " + "'Finely dice garlic Cloves.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 50 + ", " + "'Slice mushrooms.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 33 + ", " + "'Fill a large pot with boiling water.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 33 + ", " + "'Add pinch of salt and pepper.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 34 + ", " + "'Once the pasta has soften push the pasta down and stir.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 34 + ", " + "'Stir every 2-3 minutes until pasta is soft to bite.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 37 + ", " + "'Place the onion and garlic into the fry pan constantly stir until the onions lose their color.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 37 + ", " + "'Add pinch salt and pepper.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 38 + ", " + "'Add ham and stir.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 38 + ", " + "'Add cream,, stir and simmer for about a minute.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 39 + ", " + "'Add cheese.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 39 + ", " + "'Simmer for a further 2 minutes, stirring occasionally.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 40 + ", " + "'Stir and simmer the pasta and sauce together untill the sauce is sticking to the pasta (No Lose sauce).'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	db.execSQL("END TRANSACTION");
    	
    	// Chicken and vegetable fried rice*********************************************************************************    	
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  4 + ", " + "'g'" + ", " + 50 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  5 + ", " + "'g'" + ", " + 50 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  6 + ", " + "'g'" + ", " + 100 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  16 + ", " + "'g'" + ", " + 250 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  21 + ", " + "'g'" + ", " + 30 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  24 + ", " + "'tbsp'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  15 + ", " + "'g'" + ", " + 150 + ") "; 
    	db.execSQL(populateQuantities);   	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  32 + ", " + "'Pinch'" + ", " + 75 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  26 + ", " + "'tbsp'" + ", " + 3 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  7 + ", " + "'NOTYPE'" + ", " + 0.5 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  35 + ", " + "'tbsp'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 5 + ", " +  38 + ", " + "'ml'" + ", " + 500 + ") "; 
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 41 + ", " + 5 + ", " +  "'Chicken Preparation:'" + ", " + "'t'" + ", " + 1 + ", " + 1 + ", " + 7200 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 42 + ", " + 5 + ", " +  "'Rice Preparation:'" + ", " + "'t'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 43 + ", " + 5 + ", " +  "'Heat a wok (or large fry pan ) to a MEDIUM-HIGH temp.'" + ", " + "'videochickenvegetableandeggfriedrice3'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 44 + ", " + 5 + ", " +  "'Add sesame oil.'" + ", " + "'videochickenvegetableandeggfriedrice4'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 45 + ", " + 5 + ", " +  "'Place chicken in wok.'" + ", " + "'videochickenvegetableandeggfriedrice5'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 46 + ", " + 5 + ", " +  "'Once chicken and veges are cooked add rice using your hand to break it apart as you add it.'" + ", " + "'videochickenvegetableandeggfriedrice6'" + ", " + 6  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 47 + ", " + 5 + ", " +  "'Stir every 20 or so seconds breaking up the rice.'" + ", " + "'videochickenvegetableandeggfriedrice7'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 48 + ", " + 5 + ", " +  "'Add mesculin, herbs, soysauce, fish sauce.'" + ", " + "'t'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 41 + ", " + "'Slice chicken breast.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 41 + ", " + "'Marinate chicken in soysauce,, chilli,, ginger,, black bean sauce and a little sesame oil'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 41 + ", " + "'Allow to marinate in fridge for about 2 hours.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 42 + ", " + "'Cook fluffy white grain rice as per instructions on bag of rice or to your preferred method.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 42 + ", " + "'Allow to cool.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 43 + ", " + "'Add sesame oil.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 43 + ", " + "'Add beaten egg.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 43 + ", " + "'Do not mix the egg but instead use your utensil to spread it around.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 43 + ", " + "'Once the egg is done move it to a plate and loosly chop it,, you do not need to clean the wok at this point.'" + ", " + 4 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 44 + ", " + "'Increase wok temp to HIGH.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 45 + ", " + "'Reframe from stirring the chicken too much only stirring occassionaly chicken does not need to be fully cooked at this point.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 45 + ", " + "'Add vegetables.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 46 + ", " + "'Add the cooked egg.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 47 + ", " + "'You should hear the rice frying.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 47 + ", " + "'Move on when the rice is starting to brown.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 48 + ", " + "'Toss around and fry off a little more.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 48 + ", " + "'Remove from Wok once rice is fried.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	db.execSQL("END TRANSACTION");
    	
    	//Peppered Chicken Quesadilla with sour cream***********************************************************************************
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  2 + ", " + "'NOTYPE'" + ", " + 8 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  44 + ", " + "'tbsp'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  4 + ", " + "'NOTYPE'" + ", " + 0.5 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  6 + ", " + "'NOTYPE'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  13 + ", " + "'NOTYPE'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  19 + ", " + "'NOTYPE'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  25 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);   	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  32 + ", " + "'Pinch'" + ", " + 75 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  42 + ", " + "'g'" + ", " + 50 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 6 + ", " +  34 + ", " + "'tbsp'" + ", " + 2 + ") "; 
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 54 + ", " + 6 + ", " +  "'Flaten chicken:'" + ", " + "'t'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 55 + ", " + 6 + ", " +  "'Crack pepper corns over the chicken generously.'" + ", " + "'t'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 56 + ", " + 6 + ", " +  "'Place fry pan on HIGH.'" + ", " + "'pepperedchickenquesadillawithsourcream3'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 57 + ", " + 6 + ", " +  "'Spray oil over oven tray.'" + ", " + "'videopepperedchickenquesadillawithsourcream4'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 58 + ", " + 6 + ", " +  "'Sprinkle half of the cheese on top.'" + ", " + "'videopepperedchickenquesadillawithsourcream5'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 59 + ", " + 6 + ", " +  "'Allow to rest in the fridge for at least 3 hours.'" + ", " + "'t'" + ", " + 6  + ", " + 1 + ", " + 10800 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 60 + ", " + 6 + ", " +  "'Heat oven to 190c grill.'" + ", " + "'videopepperedchickenquesadillawithsourcream8'" + ", " + 7  + ", " + 1 + ", " + 420 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 61 + ", " + 6 + ", " +  "'Take the Quesidilla out of the oven and use a fish slice to remove from tray onto a chopping board.'" + ", " + "'videopepperedchickenquesadillawithsourcream8'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 54 + ", " + "'Between two sheets of glad wrap place the chicken breast in between and use a blunt object to evenly flaten it.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 55 + ", " + "'Place on baking paper.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 56 + ", " + "'Place chicken baking paper side down on the fry pan.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 56 + ", " + "'Cook for 2 minutes.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 56 + ", " + "'Take the paper off the chicken and flip to cook other side.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 56 + ", " + "'Remove chicken allow to rest.'" + ", " + 4 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 57 + ", " + "'Place flour tortilla on oven tray.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 57 + ", " + "'Lay out sliced vegetables over tortilla as you would a pizza.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 57 + ", " + "'Season with salt and pepper.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 58 + ", " + "'Slice the chicken as you did the vegetables and place on top of the cheese layer.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 58 + ", " + "'Sprinkle over the rest of the cheese.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 58 + ", " + "'Place a flour tortilla over the top.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 60 + ", " + "'Place the tortilla in the top of the oven.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 60 + ", " + "'Cook for 7 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 61 + ", " + "'Cut Quesadilla into prefered size.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 61 + ", " + "'Use fish slice to serve on plates.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 61 + ", " + "'Garnish with sour cream and black pepper.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	db.execSQL("END TRANSACTION");
    	
    	//Sweet Scones with jam and cream*******************************************************************************************
    	
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  10 + ", " + "'ml'" + ", " + 100 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  17 + ", " + "'g'" + ", " + 100 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  18 + ", " + "'NOTYPE'" + ", " + 0 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  22 + ", " + "'ml'" + ", " + 130 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  9 + ", " + "'ml'" + ", " + 140 + ") "; 
    	db.execSQL(populateQuantities);    	  	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  32 + ", " + "'Pinch'" + ", " + 75 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  33 + ", " + "'g'" + ", " + 300 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 7 + ", " +  37 + ", " + "'g'" + ", " + 70 + ") "; 
    	db.execSQL(populateQuantities); 
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 62 + ", " + 7 + ", " +  "'Spray or brush oil over muffin trays.'" + ", " + "'t'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 63 + ", " + 7 + ", " +  "'Heat oven to 160c on bake.'" + ", " + "'t'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 64 + ", " + 7 + ", " +  "'In a mixing bowl sift 3 parts raising flour and 1 part icing sugar.'" + ", " + "'t'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 65 + ", " + 7 + ", " +  "'Flour bench.'" + ", " + "'t'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 66 + ", " + 7 + ", " +  "'Using your hands flaten the mixture to about an inch thick.'" + ", " + "'t'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 67 + ", " + 7 + ", " +  "'Flour your cutter and cut out your scone.'" + ", " + "'videosweetsconeswithjamandcream7'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 68 + ", " + 7 + ", " +  "'Place scones in oven.'" + ", " + "'t'" + ", " + 8  + ", " + 1 + ", " + 900 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 69 + ", " + 7 + ", " +  "'Remove from oven.'" + ", " + "'videosweetsconeswithjamandcream9'" + ", " + 9  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 70 + ", " + 7 + ", " +  "'Sift icing sugar over it.'" + ", " + "'t'" + ", " + 10  + ", " + 1 + ", " + 300 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 71 + ", " + 7 + ", " +  "'Serve with cream and jam and enjoy.'" + ", " + "'t'" + ", " + 11  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 64 + ", " + "'Add sultanas.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 64 + ", " + "'Add equal parts full fat milk and cream and mix.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 64 + ", " + "'If the mixture is too dry add more cream.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 64 + ", " + "'If the mixture is too wet add flour.'" + ", " + 4 + ") ";
    	db.execSQL(populateHints);    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 65 + ", " + "'Place mixture on bench.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 65 + ", " + "'Sprinkle some flour over the top of the mixture.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 65 + ", " + "'Flour your hands.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 66 + ", " + "'Flour your hands again when it starts to stick.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 67 + ", " + "'Place cuts in the muffin tray.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 67 + ", " + "'After each cut reflour the cutter to stop sticking.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 67 + ", " + "'Repeat.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 67 + ", " + "'Reroll mixture if necessary.'" + ", " + 4 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 68 + ", " + "'Leave for 15 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 69 + ", " + "'Check they are ready with knife.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 69 + ", " + "'If the centers are gooey return to the oven for another 2 minutes.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 69 + ", " + "'If they are set move on.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 70 + ", " + "'Allow to stand for 5 minutes.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 70 + ", " + "'Carefully remove scones from tray and onto a cooling rack.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	db.execSQL("END TRANSACTION");
    	
    	//Tomato cheese and mixed herb frittata********************************************************************************
    	db.execSQL("BEGIN TRANSACTION");
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  3 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  8 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  12 + ", " + "'NOTYPE'" + ", " + 3 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  40 + ", " + "'g'" + ", " + 30 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  24 + ", " + "'tbsp'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);    	  	
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8 + ", " +  32 + ", " + "'Pinch'" + ", " + 75 + ") "; 
    	db.execSQL(populateQuantities);
    	populateQuantities = "INSERT INTO " + TABLE_Quantity + "( " + QURecipe_FK + ", " + QUIng_FK + ", " + QUMeasureType + ", " + QUAmount + ") "
    			+ "VALUES (" + 8+ ", " +  31 + ", " + "'NOTYPE'" + ", " + 1 + ") "; 
    	db.execSQL(populateQuantities);
    	
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", " + StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 72 + ", " + 8 + ", " +  "'Preparation:'" + ", " + "'t'" + ", " + 1 + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 73 + ", " + 8 + ", " +  "'Place fry pan on MEDIUM-HIGH heat.'" + ", " + "'t'" + ", " + 2  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 74 + ", " + 8 + ", " +  "'In a bowl beat 3 eggs with fork.'" + ", " + "'videotomatocheeseandmixedherbfritatta3'" + ", " + 3  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 75 + ", " + 8 + ", " +  "'Brush heated Pan with oil.'" + ", " + "'t'" + ", " + 4  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 76 + ", " + 8 + ", " +  "'Pour mixture into fry pan.'" + ", " + "'videotomatocheeseandmixedherbfritatta5'" + ", " + 5  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 77 + ", " + 8 + ", " +  "'Cook fritttatta for about one minute.'" + ", " + "'t'" + ", " + 6  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 78 + ", " + 8 + ", " +  "'Flip fritatta using another splate to help.'" + ", " + "'videotomatocheeseandmixedherbfritatta9'" + ", " + 7  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	populateSteps = "INSERT INTO " + TABLE_Step + "( " + StepsID_PK + ", "+ StepRecID_FK + ", " + StepDetails + ", " + StepMedia + ", " + StepNo  + ", " + StepImageInternal + ", " + TIMERTIME +") "
    			+"VALUES (" + 79 + ", " + 8 + ", " +  "'Flip fritatta onto plate.'" + ", " + "'videotomatocheeseandmixedherbfritatta10'" + ", " + 8  + ", " + 1 + ", " + 0 + ")";
    	db.execSQL(populateSteps);
    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 72 + ", " + "'Chop Herbs.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 72 + ", " + "'Dice Tomato.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 72 + ", " + "'Grate Cheese.'" + ", " + 3 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 74 + ", " + "'Add salt and pepper'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);    	
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 74 + ", " + "'Add all ingredients together into the egg mixture.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 75 + ", " + "'Turn up temperature to HIGH.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 75 + ", " + "'Add butter to pan and melt.'" + ", " + 2 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 76 + ", " + "'Stir edges into center until it starts to coagulate.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 76 + ", " + "'Then change temp to LOW.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 77 + ", " + "'Use spatula or fish slice to ensure the frittata is not sticking.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	populateHints = "INSERT INTO " + TABLE_Hint + "( " + HintStepID_FK + ", " + HintDetails + ", " + HintNo +") "
    			+ "VALUES (" + 78 + ", " + "'Cook for another minute.'" + ", " + 1 + ") ";
    	db.execSQL(populateHints);
    	
    	db.execSQL("END TRANSACTION");
    	
    	/*populateTimers = "INSERT INTO " + TABLE_Timers + "( " + TIMERRecipe_FK + ", " + TIMERStepStart + ", " + TIMERStepFin + ", " + TIMERTIME + ") "
    			+ "VALUES (" + 1 + ", " + 9 + ", " + 10 + ", " + 30 +") ";
    	db.execSQL(populateTimers);*/
    	
    	
    	db.close();
    }
    
}