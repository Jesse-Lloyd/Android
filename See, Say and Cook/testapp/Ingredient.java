package com.example.testapp;

import java.io.Serializable;

public class Ingredient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int ing_version;
	int ing_ID;
	String ing_name;
	String ing_Img;
	Boolean imageInternal;
	
	public Ingredient()
	{		
		this.imageInternal = false;
		this.ing_version = 0;
	}
	
	public Ingredient(int id, String name, String img, boolean internal)
	{
		this.ing_ID = id;
		this.ing_name = name;
		this.ing_Img = img;
		this.imageInternal = internal;
	}
	
	public int getID()
	{
		return this.ing_ID;
	}
	
	public void setID(int id)
	{
		this.ing_ID = id;
	}
	
	public Boolean isImageInternal()
	{
		return this.imageInternal;
	}
	
	public void ImageExternal(boolean b)
	{
		this.imageInternal = b;
	}
	
	public String getName()
	{
		return this.ing_name;
	}
	
	public void setName(String name)
	{
		this.ing_name = name;
	}
	
	public String getImg()
	{
		return this.ing_Img;
	}
	
	public void setImg(String img)
	{
		this.ing_Img = img;
	}
	
	public int getVersion()
	{
		return this.ing_version;
	}
	
	public void setVersion(int v)
	{
		this.ing_version = v;
	}
}
