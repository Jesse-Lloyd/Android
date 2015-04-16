package com.example.testapp;

import java.io.Serializable;

public class Hint implements Serializable, Comparable<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int hint_ID;
	int hint_step_ID;
	int hint_No;
	String hint_details;
	
	public Hint()
	{
		hint_ID = 0;
	}
	
	public Hint(int id, int stepID, String details, int hintNo)
	{
		this.hint_ID = id;
		this.hint_step_ID = stepID;
		this.hint_details = details;
		this.hint_No = hintNo;
	}
	
	public int getHintID()
	{
		return this.hint_ID;
	}
	
	public void setHintID(int id)
	{
		this.hint_ID = id;
	}
	
	public int getHintNo()
	{
		return this.hint_No;
	}
	
	public void setHintNo(int hintNo)
	{
		this.hint_No = hintNo;
	}
	
	public int getHintStepID()
	{
		return this.hint_step_ID;
	}
	
	public void setHintStepID(int id)
	{
		this.hint_step_ID = id;
	}
	
	public String getDetails()
	{
		return this.hint_details;
	}
	
	public void setDetails( String detail)
	{
		this.hint_details = detail;
	}

	@Override
	public int compareTo(Object another) {
		return this.hint_No - ((Hint) another).getHintNo();
	}

}
