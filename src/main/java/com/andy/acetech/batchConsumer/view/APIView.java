package com.andy.acetech.batchConsumer.view;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andy.acetech.batchConsumer.controller.Controller;
import com.andy.acetech.batchConsumer.model.Batch;
import com.google.gson.JsonArray;

@RestController
public class APIView {
 
	@GetMapping("/")
	public String getPage() {
		return "welcome";
	}
	 
	@PutMapping("/")
	public ResponseEntity<String> updateBatch(@RequestBody String jsonString) {

	    JsonArray responseContent = Controller.jsonToBatchArray(jsonString);
	    System.out.println("\n\n "+responseContent.toString()+" \n\n");
	    
	    ResponseEntity<String> response = new ResponseEntity<String>(responseContent.toString(), HttpStatus.ACCEPTED);

	    return response;
	}
	/*
		Notes to self, should we reject a batch if there is an issue with one ?
		or is the customer system good enough to resend single items ?
	
	*/
}
