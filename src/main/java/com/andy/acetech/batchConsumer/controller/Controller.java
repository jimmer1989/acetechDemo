package com.andy.acetech.batchConsumer.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gson.*;
import com.andy.acetech.batchConsumer.model.*;

public class Controller {
	
	public static JsonArray jsonToBatchArray(String jsonString){
		ArrayList<String> batchIDs=new ArrayList<String>();
		
		ArrayList<Batch> batchArray=new ArrayList<Batch>(); 
		if(isValidJsonArray(jsonString)) { // validate JSON and make sure it is an array
			JsonArray data = (JsonArray) JsonParser.parseString(jsonString);
			for (JsonElement element : data) { // go through list of json objects, check for issues and add them to the array.
			    JsonObject object = element.getAsJsonObject();
			    Batch tempBatch=convertJsonToBatchObject(object);
			    if(tempBatch != null) {
			    	batchArray.add(convertJsonToBatchObject(object));
			    	batchIDs.add(tempBatch.getBatchId());
			    }else{
			    	return getJsonStringBadIDResponse();
			    }
			}			
		}else{
			return getJsonStringMalformedOrEmptyResponse();
		}
				
		ArrayList<String> duplicates = findDuplicates(batchIDs);// I assume duplicate batchIDs is an issue
		for (int i = 0; i < duplicates.size(); i++) {
		    for(int j = 0; j < batchArray.size(); j++){
		    	if(batchArray.get(j).getBatchId().equals(duplicates.get(i) ) ) {
		    		batchArray.get(j).addErrorList("batchId", "duplicates found, object is rejected");
		    	}
		    }
		}
		
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
		
		
		// insert the batchArray successes into a database.
		// for each object that returns true from .isSuccessful()
		
		// OUT OF SCOPE FOR NOW - need to handle database issue though.
		// what if they send a batchID we have not seen before ?
		// what if they have a batchID / batchTypeDescription pair that is different from what we have ?
		// If i had the the date-time of message generation I would use update where batchID = batchID and current_timestamp< new_timestamp 
		
		// there could be issues inserting into database, and this method should be able to add errors to the object and return it to the sender much like what we have here.
		// possible solution is to return Db busy please resend in 5 minutes
		
		// unknown batch ID, etc  
		
		// what if the database is simply non responsive?
		// is it possible to send failures at a later time, asynchronously? 
	
		
		// we can log and store messages once database recovers, but might have missed opportunity to send back unknown batch error.
		// in these scenarios we can build a queue, and submit once db has recovered.
		
		// final thought, It is best to request resend in this case, unless the there is no timeout set, then we could leave sender hanging (until we have a final answer) if that is acceptable to them
		// accuracy is important, commonly occurring database issues should be dealt with by bug fixing or scaling up resources (based on the situation)
		
		return response;
	}
	
	private static JsonArray getJsonStringMalformedOrEmptyResponse() {
		JsonArray errorResponse = new JsonArray();
		JsonObject errorMessage=new JsonObject();
		errorMessage.addProperty("ERROR", "message was empty or invalid json array");
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
		
		if(jsonBatch.has("batchid")&&jsonBatch.get("batchid").getAsString().length()>0 ){ 
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
		newBatch.setRecievedDate( LocalDate.now() );
		return newBatch;
	}

	public static boolean isValidJsonArray(String json) {
		if(json==null) {
			return false;
		}
	    try {
	    	JsonElement data =JsonParser.parseString(json);
	    	if(!data.isJsonArray() || data.getAsJsonArray().size()<1) {
	    		return false;
	    	}
	    	
	    } catch (JsonSyntaxException e) { return false;
	    }
	    return true;
	}
	
	public static ArrayList<String> findDuplicates(ArrayList<String> batchIDs) {
	    ArrayList<String> duplicates = new ArrayList<String>();
	    HashSet<String> set = new HashSet<String>();
	    for (String id : batchIDs) {
	        if (!set.add(id)) {
	            duplicates.add(id);
	        }
	    }
	    return duplicates;
	}
}
