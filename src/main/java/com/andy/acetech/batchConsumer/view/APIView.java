package com.andy.acetech.batchConsumer.view;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andy.acetech.batchConsumer.controller.Controller;
import com.google.gson.JsonArray;

@RestController
public class APIView {
 
	@GetMapping("/")
	public String getPage() {
		return "welcome";
	}
	 
	@PutMapping("/")
	public ResponseEntity<String> updateBatch(@RequestBody String jsonString) {
		//System.out.println("System.getProperty(\"java.version\")"+System.getProperty("java.version"));
	    
		JsonArray responseContent = Controller.jsonToBatchArray(jsonString);	    
	    /* How often does this message come in ?
	     * if it's every few seconds or once a minute, is it really necessary to chase fails ?
	     * I have seen well regarded and trusted systems that simply disregard the odd message failure, with the knowledge that another update will be along soon enough
	     * solutions like this should have a last update field when displaying info to the user.
	     * 
	     * This may not be good enough when dealing with medicine and the lives depending on it.
	     * 
	     * for now I will assume every message is unmissable, and should be followed up */
	    
	    /* http status is always ok if we have read and respond */
	    ResponseEntity<String> response = new ResponseEntity<String>(responseContent.toString(), HttpStatus.OK);
	    
	    /*
	     After asking Seachliann I treat these as if the client system can read errors and resend single messages based on errors in our responses.
	     if the client system could not read errors with this detail I would simply respond to all issue with 
	     
	     [{"ERROR":"One or more nodes had a missing ID, please resend batch"}]
	      */

	    return response;
	}
}
