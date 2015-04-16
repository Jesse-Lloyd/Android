package com.example.testapp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class Step implements Serializable, Comparable<Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int step_rec_ID;
	int step_ID;
	int step_No;
	String step_details;
	String step_media;
	Boolean imageInternal;
	ArrayList<Hint> hints = new ArrayList<Hint>();
	int timer;
	ArrayList<Hint> deletedHints = new ArrayList<Hint>();	
	
	public Step()
	{
		this.step_ID = 0;
		this.imageInternal = false;
	}
	
	public Step(int ID, int recID, String details, String media, int stepNo, Boolean internal, int timer)
	{
		this.imageInternal = internal;
		this.step_rec_ID = recID;
		this.step_ID = ID;
		this.step_details = details;
		this.step_media = media;
		this.step_No = stepNo;
		this.timer = timer;
	}
	
	public int getRecID()
	{
		return this.step_rec_ID;
	}
	
	public void setRecID(int recID)
	{
		this.step_rec_ID = recID;
	}
	
	public Boolean isImageInternal()
	{
		return this.imageInternal;
	}
	
	public void ImageExternal()
	{
		this.imageInternal = false;
	}
	
	public int getStepNo()
	{
		return this.step_No;
	}
	
	public void setStepNo(int stepNo)
	{
		this.step_No = stepNo;
	}
	
	public int getID()
	{
		return this.step_ID;
	}
	
	public void setID(int id)
	{
		this.step_ID = id;
	}
	
	public String getDetails()
	{
		return this.step_details;
	}
	
	public void setDetails(String details)
	{
		this.step_details = details;
	}
	
	public String getMedia()
	{
		return this.step_media;
	}
	
	public void setMedia(String media)
	{
		this.step_media = media;
	}
	
	public ArrayList<Hint> getHints()
	{
		return this.hints;
	}
	
	public void setHints(ArrayList<Hint> hint)
	{
		this.hints.addAll(hint);
		Collections.sort(hints);
	}
	public void addHint(Hint hint)
	{
		this.hints.add(hint);
	}
	
	public Hint getHint(int hint)
	{
		return this.hints.get(hint);
	}
	public int getTimer()
	{
		return this.timer;
	}
	
	public void setTimer(int timer)
	{
		this.timer = timer;
	}
	
	public ArrayList<Hint> getDeletedHintsList()
	{
		return this.deletedHints;
	}
	
	public Hint getDeletedHint(int i)
	{
		return this.deletedHints.get(i);
	}
	
	public void addDeletedHint(Hint hint)
	{
		this.deletedHints.add(hint);
	}

	@Override
	public int compareTo(Object s) {
		return this.step_No - ((Step) s).getStepNo();
	}
	
	
}
