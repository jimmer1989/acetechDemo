package com.andy.acetech.batchConsumer.model;

import java.time.LocalDate;


public class Batch extends Transmittable
{
	private LocalDate batchExpirationDate;
	private String batchTypeDescription;
	private String batchTypeId;
	private String batchId;
	private int batchCount;
	private LocalDate receivedDate;
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

	public void setRecievedDate(LocalDate now) {/* I would much prefer this field was generated date. the sender does not currently support that but it would be very useful*/
		this.receivedDate = now;
	}

	public LocalDate getRecievedDate() {
		return receivedDate;
	}
	
	
}
