package com.andy.acetech.batchConsumer.model;

import java.util.HashMap;

public class Transmittable {

	private HashMap<String,String> errorList=new HashMap<String,String>();
	
	public Transmittable()
	{
		
	}

	public Boolean isSuccessful() {
		return errorList.isEmpty();
	}

	public HashMap<String,String> getErrorList() {
		return errorList;
	}

	public void addErrorList(String key,String value) {
		this.errorList.put(key, value);
	}
	
	
	
	
}
