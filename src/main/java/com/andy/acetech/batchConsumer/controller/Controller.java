package com.andy.acetech.batchConsumer.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.*;

import com.andy.acetech.batchConsumer.model.*;

public class Controller {
	
	public static JsonArray jsonToBatchArray(String jsonString){
		ArrayList<Batch> batchArray=new ArrayList<Batch>(); 
		if(isValid(jsonString)) {
			JsonArray data = (JsonArray) JsonParser.parseString(jsonString);
			for (JsonElement element : data) {
			    JsonObject object = element.getAsJsonObject();
			    System.out.println(object.toString());
			    Batch tempBatch=convertJsonToBatchObject(object);
			    if(tempBatch != null) {
			    	batchArray.add(convertJsonToBatchObject(object));
			    }else{
			    	return getJsonStringBadIDResponse();// return an error that properly reflects the issue.
			    }
			    
			}			
		}else { 
			return getJsonStringMalformedOrEmptyResponse();
		}
		// insert the batchArray successes into a database.
		// OUT OF SCOPE FOR NOW - need to handle database issue though.
		// what if they send a batchID we have not seen before ?
		// what if they have a batchID / batchTypeDescription pair that is different from what we have ?
		JsonArray response = new JsonArray(); 
		for(int i=0; i<batchArray.size(); i++) {
			JsonObject entry = new JsonObject();
			if(batchArray.get(i).isSuccessful()){
				 entry.addProperty(batchArray.get(i).getBatchId(), "Success");
			}else{
				entry.addProperty(batchArray.get(i).getBatchId(), "Failure");// we can count on batch ID existing here because we dont deal with that error by recording it in this way
				HashMap<String, String> errorMap=batchArray.get(i).getErrorList();
				for (Entry<String, String> errorEntry : errorMap.entrySet()) {
					entry.addProperty( errorEntry.getKey(),  errorEntry.getValue());
				}
			}
			response.add(entry);
		}
		return response;
	}
	
	private static JsonArray getJsonStringMalformedOrEmptyResponse() {
		JsonArray errorResponse = new JsonArray();
		JsonObject errorMessage=new JsonObject();
		errorMessage.addProperty("ERROR", "message was empty or invalid json");
		errorResponse.add(errorMessage);
		
		return errorResponse;
	}

	private static JsonArray getJsonStringBadIDResponse() {
		JsonArray errorResponse = new JsonArray();
		JsonObject errorMessage=new JsonObject();
		errorMessage.addProperty("ERROR", "One or more nodes had a missing ID, please resend batch");
		errorResponse.add(errorMessage);
		
		return errorResponse;
	}
	
	private static Batch convertJsonToBatchObject(JsonObject jsonBatch) {
		Batch newBatch=new Batch();
		
		if(jsonBatch.has("batchid")){ 
			newBatch.setBatchId(jsonBatch.get("batchid").getAsString() );
		}else{
			/* The reason we return null from this instead of simply recording the issue is 
			 * this field is the unique id, if it is missing there will be no true way to tell the sender what to resend.
			 * I believe the best solution is to have them resend the batch*/
			return null;
		}
		
		if(jsonBatch.has("batchTypeId")){ 
			newBatch.setBatchTypeId(jsonBatch.get("batchTypeId").getAsString());
		}else{
			newBatch.addErrorList("batchTypeId", "value not found"); 
		}
		
		if(jsonBatch.has("batchTypeDescription")){ 
			newBatch.setBatchTypeDescription(jsonBatch.get("batchTypeDescription").getAsString());
		}else{
			newBatch.addErrorList("batchTypeDescription", "value not found"); 
		}
		
		if(jsonBatch.has("batchExpirationDate")){ 
			try {
				LocalDate batchExpirationDate = LocalDate.parse( jsonBatch.get("batchExpirationDate").getAsString() );
				newBatch.setBatchExpirationDate(batchExpirationDate);
			}catch(Exception e){
				newBatch.addErrorList("batchExpirationDate", "value is not a date");
			}
		}else{
			newBatch.addErrorList("batchExpirationDate", "value not found"); 		
		}
		
		if(jsonBatch.has("batchCount")&&jsonBatch.get("batchCount").isJsonPrimitive()  ){ 
			try {
			newBatch.setBatchCount(jsonBatch.get("batchCount").getAsInt() );
			}catch(NumberFormatException e) {
				newBatch.addErrorList("batchCount", "value "+jsonBatch.get("batchCount")+" not invalid number");  
			}
		}else{
			newBatch.addErrorList("batchCount", "value not found");  
		}
		
		//System.out.println("");
		//newBatch.print();
		//System.out.println("");
		
		return newBatch;
	}

	public static boolean isValid(String json) {
		if(json==null) {
			return false;
		}
	    try {
	        JsonParser.parseString(json);
	    } catch (JsonSyntaxException e) { return false;
	    }
	    return true;
	}
}