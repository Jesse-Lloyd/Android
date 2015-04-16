package com.example.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GeneralDialogs 
{
	Activity act;
	public GeneralDialogs(Activity a)
	{
		act = a;
	}
	public void about()
	{
		LinearLayout ll = new LinearLayout(act);
		ll.setOrientation(1);
		ll.setBackgroundResource(R.drawable.portraitback);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(act);
		aboutDialog.setTitle("About");
		TextView ParagraphOne = new TextView(act);
		TextView ParagraphTwo = new TextView(act);
		ParagraphOne.setTextSize(20);
		ParagraphTwo.setTextSize(20);
		ParagraphOne.setText("This Application See, Say and Cook version one was developed by Jesse " +
				"Lloyd and Brendan Cleghorn for a student project sponsored by Robert Sutcliffe of the wellington institute of technology.");
		ParagraphOne.setLines(3);
		ParagraphTwo.setText("\n\nSee, Say and Cook is meant to be a cookbook app aimed at those with learning disorders or are I.T illiterate, " +
				"as there are no other cookbooks out there that cover the things that these sort of people need the market is wide open and " +
				"ready for the taking this was one of the main reasons that we decided to take this project on as well as letting us two newbie programmers get a taste of android development." +
				"\n\nSpecial thanx to Harish Ravee & Kalyan Kodela for providing the built in images");
		ll.addView(ParagraphOne,layoutParams);
		ll.addView(ParagraphTwo,layoutParams);
		aboutDialog.setView(ll);
		aboutDialog.setPositiveButton("Close", null);
		AlertDialog dialog = aboutDialog.create();
		dialog.show();
	}
	
	public void help(int which)
	{
		ScrollView scroll = new ScrollView(act);
		LinearLayout ll = new LinearLayout(act);
		ll.setOrientation(1);
		
		ll.setBackgroundResource(R.drawable.portraitback);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		scroll.setLayoutParams(layoutParams);
		scroll.addView(ll);
		
		AlertDialog.Builder helpDialog = new AlertDialog.Builder(act);
		helpDialog.setTitle("Help");
		TextView ParagraphOne = new TextView(act);
		TextView ParagraphTwo = new TextView(act);
		ParagraphOne.setTextSize(20);
		ParagraphTwo.setTextSize(20);
		
		switch(which)
		{
		case 0:
			ParagraphOne.setText("This is the main menu from here you can navigate to any of the features"
					+" See, Say and Cook provides");
			ParagraphTwo.setText("\n\nTo start cooking touch the Lets Get Cooking button, to add a new recipe touch the " +
					"Add recipe button and so on, each button is labeled accordingly");
			break;
		case 1:
			ParagraphOne.setText("On this page you select the recipe you want to cook"
					+" by touching one of the empty circles and then the once you have selected a" +
					" recipe touch the continue button");
			ParagraphTwo.setText("\n\nIf you want you can refine the list of recipes by one of the"+
					" three different search functions provided, to do so simply touch the button at the top"+
					" and select which type of search you want to use");
			break;
		case 2:
			ParagraphOne.setText("Here are the ingredients you will need for the chosen recipe "+
					"each ingredient you will need should have a picture of the ingredient, name and amount of that ingredient you will need ");
			ParagraphTwo.setText("\n\nAs you get the ingredients touch the empty square boxes next to the ingredients name, Once you have"+
					" all the ingredients and all the boxes are ticked touch the continue button at the top of the screen");
			break;
		case 3:
			ParagraphOne.setText("this page displays one step at a time of the selected recipe and any corresponding information" +
					" related to that one step, here you can navigate through the various steps and view their videos or pictures as " +
					"well as the hints of that step which are usually just more in-depth descriptions of the step.");
			ParagraphTwo.setText("\n\n1. Next Step \nWhen this Button is touched the app will load and display the next step of the recipe " +
					"and all corresponding information of said step unless it is the last step of the recipe which instead will display a message telling you so."+
					"\n\n2. Previous Step \nWhen this Button is touched the app will load and display the previous step of the recipe and all corresponding information of said step, " +
					"unless the step youÅfre navigating from is the first step nothing will happen. \n\n3. Start Timer\nIf a step has a timer that can be started then this button will " +
					"be colored black otherwise it remains grey, if touched while grey nothing happens, if touched while black the timer for that step will start(a prompt will display " +
					"at the bottom of the screen telling you that a step has a timer). \n\n4.Active Timers \nIf one or more timers are active then this button will be black otherwise it " +
					"remains grey, if touched while grey nothing happens, if touched while black then a pop-up box will display with all the active timers the steps they are for and how " +
					"much time remains before an alarm is played. \n\n5.Stop Speaking \nStops the currently playing Text to Speech. \n\n6.Say Again \nWhen touched the text to speech will " +
					"play again speaking the recipes step details and the steps hints if any. \n\n7.Full Screen \nTransition the video into full screen. \n\n8.Ingredients \nA list of all the ingredients used in the whole recipe"+
					"\n\n9. App Icon \nWhen touched you will be sent back to the ingredient list after a series of prompts asking if you are sure you want to stop cooking (does not working on small devices)."+
					"\n\n10.Back button \nWill toggle the recipes step back unless you are on the first step which will then act the same as 9. App Icon.");
			break;
		case 4:
			ParagraphOne.setText("Here you can add a new recipe that you can use for cooking, you can give it videos, images, steps and ingredients basically a recipe that is fully customizable"+
								"simply Name Your Recipe, give it a description, some ingredients and steps and if you would like to you can add an image that will represent the recipe.");
			ParagraphTwo.setText("\n\n1.Recipe Name \nAnything that is typed in here will become the name of the recipe. \n\n2.Recipe Description \nA short description can be anything you want really."+
								"\n\n3.End Result \nTouch this button if you want add a picture for the recipe (maybe something like how it should look once itÅfs finished being cooked). \n\n"+
								"4.Add ingredient \nSelect ingredients from a list of all the ingredients that will be used in the recipe. \n\n5.Current ingredients \nTouch to be shown a drop " +
								"down list of all the current ingredients that have been selected. \n\n6.Add Step \nAdd a step to the recipe, the step will allow you to give it details of what " +
								"should be done a video or image to acts as help a timer if you want one and any hints if needed. \n\n7.Current Steps \nTouch to be shown a drop down of all the current " +
								"steps that have been added to the recipe you can touch any step that has been added if you want to edit that step. \n\n8.Continue \nWhen you are done creating you recipe " +
								"touch this button to finish and add it to all the other recipes and donÅft worry if you did something wrong you can always edit the recipe later which will be covered next.");
			break;
		case 5:
			ParagraphOne.setText("Here you can edit an already existing recipe to your hearts content simply select the recipe you want to edit, then the details of the selected recipe will be displayed and you can edit it how you want");
			ParagraphTwo.setText("\n\n1.Select Recipe \nTouching this button will allow you to select another recipe to edit do note that all unsaved changes of the current recipe you are editing will not be retained."+
			"\n\n2.Recipe Name \nThe current name of the recipe, can be edited. \n\n3.Recipe Description \nThe current Description of the recipe, can be edited. \n\n4.End result \nSelect another image for this recipe from the devices gallery."+
					"\n\n5.Steps \nTouch to open a drop down of all the current steps involved in the recipe, touching any of the steps will allow you to edit them or you can touch add step to add a new step which is located at the top of the list."+
			"\n\n6.	Ingredients \nTouch to open a drop down of all the ingredients that are used in the recipe, touching any of the ingredients will allow you the choice to delete it of you can add an ingredient by touching add ingredient which is " +
			"located at the top of the list. \n\n7.Delete \nDelete the current recipe (warning this cannot be undone). \n\n8.Reset \nReturn all the fields to the recipes default details since last update"+
			"\n\n9.Update \nOnce you have finished editing the recipe touch this button to apply the changes (note this cannot be undone).");
			break;
		case 6:
			ParagraphOne.setText("Here you can import recipes that have been exported, to use just \n\n1.Enter the name of the folder that the exported files are located in");
			ParagraphTwo.setText("\n2.Touch the Import recipes button to import them if the folder exists then all the recipes located within it are imported.");
			break;
		case 7:
			ParagraphOne.setText("Here you can export recipes so that you can import them on to another device or just as a backup to do so"); 
			ParagraphTwo.setText("\n\n1.Give a name to the folder that the exported recipes will be saved in." +
					"\n\n2.Select the recipes you want to export. \n\n3.Touch the export recipes button to finish and export the chosen recipes");
			break;
		case 8:
			ParagraphOne.setText("Here you can add new ingredients to be used in add or edit recipe to do so just");
			ParagraphTwo.setText("\n\n1.Enter the name of the ingredient do note that the name must be unique it cannot be the same as an already existing ingredient" +
					"\n\n2.select an image for the ingredient by touching the add ingredient picture button note the ingredient doesn't need an image" +
					"\n\n3.touch the continue button to add the ingredient to the app ready for your use");
			break;
		}
		ll.addView(ParagraphOne,layoutParams);
		ll.addView(ParagraphTwo,layoutParams);
		helpDialog.setView(scroll);
		helpDialog.setPositiveButton("Close", null);
		AlertDialog dialog = helpDialog.create();
		dialog.show();
		
		
	}
}
