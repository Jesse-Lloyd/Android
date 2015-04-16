package com.example.testapp;

import java.io.Serializable;

public class Quantity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum MeasureType{tbsp, tsp, cup, quart, pint, ml, g, Whole, Pinch, Cloves, NOTYPE}
	
	int qu_ID;
	int qu_rec_ID;
	int qu_ing_ID;
	MeasureType measureType;
	float qu_amount;
	String ing_name;
	String ing_imageName;
	Boolean imgInternal;
	
	public Quantity()
	{
		qu_ID = 0;
	}
	
	public Quantity(int id, int recID, int ingID, MeasureType type, float amount, String name, String imageName, Boolean internal )
	{
		this.qu_ID = id;
		this.qu_rec_ID = recID;
		this.qu_ing_ID = ingID;
		this.measureType = type;
		this.qu_amount = amount;
		this.ing_name = name;
		this.ing_imageName = imageName;
		this.imgInternal = internal;
	}
	public int getID()
	{
		return this.qu_ID;
	}
	
	public void setID(int ingID)
	{
		this.qu_ID = ingID;
	}
	
	public int getIngID()
	{
		return this.qu_ing_ID;
	}
	
	public void setIngID(int ingID)
	{
		this.qu_ing_ID = ingID;
	}
	
	public int getRecID()
	{
		return this.qu_rec_ID;
	}
	
	public void setRecID(int recID)
	{
		this.qu_rec_ID = recID;
	}
	
	public float getAmount()
	{
		return this.qu_amount;
	}
	
	public void setAmount(float amount)
	{
		this.qu_amount = amount;
	}
	
	public MeasureType getType()
	{
		return this.measureType;
	}
	
	public void setType(MeasureType type)
	{
		this.measureType = type;
	}
	
	public String getIngName()
	{
		return this.ing_name;
	}
	
	public void setAmount(String name)
	{
		this.ing_name = name;
	}
	
	public String getIngImage()
	{
		return this.ing_imageName;
	}
	
	public void setIngImage(String path)
	{
		this.ing_imageName = path;
	}
	public boolean isImageInternal()
	{
		return this.imgInternal;
	}
}
