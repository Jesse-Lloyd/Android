package com.example.testapp;

import java.io.Serializable;

public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int catID_PK;
	private int catID;
	private String catName;
	
	public Category()
	{
		this.catID_PK = 0;
	}
	
	public Category(int idPK, int id, String name)
	{
		this.setCatID_PK(idPK);
		this.catID = id;
		this.catName = name;		
	}
	
	
	public int getID()
	{
		return this.catID;
	}
	
	public String getName()
	{
		return this.catName;
	}
	
	public void setID(int id)
	{
		this.catID = id;
	}
	
	public void setName(String name)
	{
		this.catName = name;
	}

	public int getCatID_PK() {
		return catID_PK;
	}

	public void setCatID_PK(int catID_PK) {
		this.catID_PK = catID_PK;
	}
}
