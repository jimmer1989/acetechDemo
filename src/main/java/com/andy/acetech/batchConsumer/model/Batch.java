package com.andy.acetech.batchConsumer.model;

import java.time.LocalDate;
import java.util.Date;

public class Batch extends Transmittable
{
	private LocalDate batchExpirationDate;
	private String batchTypeDescription;
	private String batchTypeId;
	private String batchId;
	private int batchCount;
	
	public Batch()
	{
		
	}
	
	public void print() {
		System.out.println("batchExpirationDate : "+batchExpirationDate);
		System.out.println("batchTypeDescription : "+batchTypeDescription);
		System.out.println("batchTypeId : "+batchTypeId);
		System.out.println("batchId : "+batchId);
		System.out.println("batchCount : "+batchCount);
		System.out.println("errors : "+getErrorList());
	}	
	
	public LocalDate getBatchExpirationDate() {
		return batchExpirationDate;
	}

	public void setBatchExpirationDate(LocalDate batchExpirationDate) {
		this.batchExpirationDate = batchExpirationDate;
	}

	public String getBatchTypeDescription() {
		return batchTypeDescription;
	}

	public void setBatchTypeDescription(String batchTypeDescription) {
		this.batchTypeDescription = batchTypeDescription;
	}

	public String getBatchTypeId() {
		return batchTypeId;
	}

	public void setBatchTypeId(String batchTypeId) {
		this.batchTypeId = batchTypeId;
	}

	public int getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	
	
	
}
