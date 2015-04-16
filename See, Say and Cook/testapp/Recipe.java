package com.example.testapp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class Recipe implements Serializable
{
	public enum MeasureType{tbsp, tsp, cup, quart, pint, ml, gr, oz, Whole, Pinch}
	private static final long serialVersionUID = 0L;
	
	int rec_version;
	int rec_ID;
	String rec_name;
	String rec_descript;
	String finalImg;
	Boolean imageInternal;
	int serves;
	int timeEst;
	ArrayList<Step> steps = new ArrayList<Step>();
	ArrayList<Quantity> quAmount = new ArrayList<Quantity>();
	ArrayList<Category> cat = new ArrayList<Category>();
	
	ArrayList<Step> deleteSteps = new ArrayList<Step>();
	ArrayList<Quantity> deleteQuantities = new ArrayList<Quantity>();
	ArrayList<Category> deleteCategory = new ArrayList<Category>();
	
	public Recipe()
	{
		this.imageInternal = false;
	}
	
	public Recipe(int id, String name, String descript, String img, boolean internal, int serve, int time, int Version)
	{
		this.rec_ID = id;
		this.rec_name = name;
		this.rec_descript = descript;
		this.finalImg = img;
		this.imageInternal = internal; 
		this.serves = serve;
		this.timeEst = time;
		this.rec_version = Version;
		//this.steps.addAll(step);
		//this.quAmount.addAll(qu);
	}
	
	public int getID()
	{
		return this.rec_ID;
	}
	
	public void setID(int id)
	{
		this.rec_ID = id;
	}
	
	public int getServings()
	{
		return this.serves;
	}
	
	public void setServings(int serves)
	{
		this.serves = serves;
	}
	
	public int getTimeEst()
	{
		return this.timeEst;
	}
	
	public void setTimeEst(int time)
	{
		this.timeEst = time;
	}
	
	public Boolean isImageInternal()
	{
		return this.imageInternal;
	}
	
	public void ImageExternal()
	{
		this.imageInternal = false;
	}
	
	public String getName()
	{
		return this.rec_name;
	}
	
	public void setName(String name)
	{
		this.rec_name = name;
	}
	
	public String getDescription()
	{
		return this.rec_descript;
	}
	
	public void setDescription(String descript)
	{
		this.rec_descript = descript;
	}
	
	public String getImg()
	{
		return this.finalImg;
	}
	
	public void setImg(String img)
	{
		this.finalImg = img;
	}
	
	public ArrayList<Step> getSteps()
	{
		return this.steps;
	}
	
	public void setSteps(ArrayList<Step> step)
	{
		this.steps.addAll(step);
		Collections.sort(this.steps);
	}
	
	public void addStep(Step step)
	{
		this.steps.add(step);
	}
	
	public Step getStep(int num)
	{
		return this.steps.get(num);
	}
	
	public ArrayList<Quantity> getQus()
	{
		return this.quAmount;
	}
	
	public void setQus(ArrayList<Quantity> qu)
	{
		this.quAmount = qu;
	}
	
	public void addQu(Quantity qu)
	{
		this.quAmount.add(qu);
	}
	
	public Quantity getQu(int num)
	{
		return this.quAmount.get(num);
	}
	
	public int getVersion()
	{
		return this.rec_version;
	}
	
	public void setVersion(int v)
	{
		this.rec_version = v;
	}
	
	public void addDeletedStep(Step step)
	{
		this.deleteSteps.add(step);
	}
	
	public void addDeletedQuantity(Quantity qu)
	{
		this.deleteQuantities.add(qu);
	}
	
	public ArrayList<Step> getDeletedStepList()
	{
		return this.deleteSteps;
	}
	
	public ArrayList<Quantity> getDeletedQuList()
	{
		return this.deleteQuantities;
	}
	
	public Step getDeletedStep(int i)
	{
		return this.deleteSteps.get(i);
	}
	
	public Quantity getDeletedQu(int i)
	{
		return this.deleteQuantities.get(i);
	}
	
	public void addCategory(Category cat)
	{
		this.cat.add(cat);
	}
	public Category getCategory(int cat)
	{
		return this.cat.get(cat);
	}
	
	public void setCategories(ArrayList<Category> cat)
	{
		this.cat = cat;
	}
	
	public ArrayList<Category> getCategories()
	{
		return this.cat;
	}
	
	public void addDeletedCategory(Category cat)
	{
		this.deleteCategory.add(cat);
	}
	
	public ArrayList<Category> getDeletedCategoryList()
	{
		return this.deleteCategory;
	}
	
	public Category getDeletedCat(int i)
	{
		return this.deleteCategory.get(i);
	}
}
