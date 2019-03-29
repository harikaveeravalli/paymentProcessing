package glassdoor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
//package com.glassdoor.test.intern.first;

public class PaymentApplication {

	static HashMap<String,String> encryptData = new HashMap<String,String>();
	// Creating a HashMap of UserID as Key and UserName as Value, to make sure that the userId for the Incoming Request is unique 	
	static HashMap<String, String> hm = new HashMap<String, String>();
	static HashMap<String, ArrayList<String>> dataForProcess = new HashMap<String, ArrayList<String>>();
	// Queue for processing the Payment Request, to maintain con
	static Queue<String> ProcessRequestQueue = new LinkedList<String>();
	// According the payment Mode the corresponding classes IncomingRequest are used
	//Change this to "credit/debit or mobilewallet"
	static String paymentMode="mobilewallet";
	
  public static void main(String[] args) {
	  Logger logData = Logger.getLogger(PaymentApplication.class.getName());
	  Scanner sc = new Scanner(System.in);
	  Boolean isDuplicateUserId = false;
	  validatingData obj;
    try {
   FileHandler handler = new FileHandler("/Users/harika/Desktop/PaymentProcessing.log");
   logData.addHandler(handler);
	IncomingRequestCard incomingRequest = new IncomingRequestCard();
	IncomingReqMobile incomingReqMobile = new IncomingReqMobile();
   if(paymentMode == "credit/debit") {
    incomingRequest.userId = 1;
    incomingRequest.userName = "ABC";
    incomingRequest.billingAddress = "123 Some Street, City Name, ST";
    incomingRequest.amount = 20;
    incomingRequest.paymentMode = "credit/debit";
    paymentMode = incomingRequest.paymentMode;
    incomingRequest.cardnumber = "3855045094059045";
    incomingRequest.securepin = 456;
    incomingRequest.expiryDate = "03/23";
    obj = new validatingData(incomingRequest);
    if(!hm.containsKey(incomingRequest.userId)) {
    	hm.put(incomingRequest.userId+"", incomingRequest.userName);
    }
    else
    {
    	if(hm.get(incomingRequest.userId) != incomingRequest.userName) {
    		isDuplicateUserId = true;
        	System.out.println("userID and username combination does not match");
    	}
    	
    }
    if(obj.cardValid == false)
    {
    	System.out.println("Your Credit number is not Valid");
    	
    }
    if(obj.dateValid == false) {
    	System.out.println("Your Expiry Date is not Valid, Please try again");
    }
    if(obj.pinValid == false) {
    	System.out.println("Please entry a valid digit number");
    }
    // exit out from the application, if any of entered credentials are wrong
    if(!obj.cardValid || !obj.dateValid || !obj.pinValid || isDuplicateUserId ) {
    	System.exit(0);
    }
   }
   if(paymentMode == "mobilewallet") {
	   //incomingReqMobile = new IncomingReqMobile();
	   incomingReqMobile.userId = 1;
	    incomingReqMobile.userName = "ABC";
	    incomingReqMobile.billingAddress = "123 Some Street, City Name, ST";
	    incomingReqMobile.amount = 20;
	    incomingReqMobile.paymentMode = "mobilewallet";
	    paymentMode = incomingReqMobile.paymentMode;
	    incomingReqMobile.mobileNum = "2132147751";
	     obj = new validatingData(incomingReqMobile);
	   if(obj.mobileValid == false) {
		   System.out.println("Please enter a Valid US mobile Number");
		   System.exit(0);
	   }
   }   
   // Depending on the payment Mode, i am creating a hashMap of encrypted keys and values and passing it to the payment processing class
   
   if(paymentMode == "credit/debit") {
  /* encrypting the data received by Incoming Request. Using SHA encryption */
   String temp = incomingRequest.userId+"";
   String userId = org.apache.commons.codec.digest.DigestUtils.sha256Hex(temp);
   String userName = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingRequest.userName);
   String billingAddress = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingRequest.billingAddress);
   String amount = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingRequest.amount+"");
  // Creating new HashMap that stores the encrypted keys and its original value for everyRequest
   
   encryptData = new HashMap<String, String>();
   encryptData.put(userId, incomingRequest.userId+"");
   encryptData.put(userName, incomingRequest.userName+"");
   encryptData.put(billingAddress, incomingRequest.billingAddress);
   encryptData.put(amount, incomingRequest.amount+"");
   
    
    String cardNumber = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingRequest.cardnumber+"");
    String pinNum = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingRequest.securepin+"");
    //creating an HashMap that stores the encrypted String and its corresponding original value received by the 
    
    encryptData.put(cardNumber,incomingRequest.cardnumber);
    encryptData.put(pinNum, incomingRequest.securepin+"");
    if(!dataForProcess.containsKey(userId)) {
    	ArrayList<String> ai = new ArrayList<String>();
    	ai.add(userName);
    	ai.add(billingAddress);
    	ai.add(amount);
    	ai.add(cardNumber);
    	ai.add(pinNum);
    	
    	dataForProcess.put(userId, ai);
    }
    ProcessRequestQueue.add(incomingRequest.userId+"");
    boolean successfulPayment = new PaymentProcessor().process_payment(dataForProcess, incomingRequest.paymentMode, ProcessRequestQueue,encryptData);
    // Add the incoming request to the process payment queue
    
    System.out.println("successful sent data for processing Credit/debit card payment");
   
   }
   if(paymentMode == "mobilewallet") {
	   String temp = incomingReqMobile.userId+"";
	   String userId = org.apache.commons.codec.digest.DigestUtils.sha256Hex(temp);
	   String userName = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingReqMobile.userName);
	   String billingAddress = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingReqMobile.billingAddress);
	   String amount = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingReqMobile.amount+"");
	   String mobileNum = org.apache.commons.codec.digest.DigestUtils.sha256Hex(incomingReqMobile.amount+"");
   
	// Creating HashMap that stores the encrypted keys and its original value
	   encryptData.put(userId, incomingReqMobile.userId+"");
	   encryptData.put(userName, incomingReqMobile.userName+"");
	   encryptData.put(billingAddress, incomingReqMobile.billingAddress);
	   encryptData.put(amount, incomingReqMobile.amount+"");
	   encryptData.put(mobileNum, incomingReqMobile.mobileNum);
    	
    	ProcessRequestQueue.add(incomingReqMobile.userId+"");
    	if(!dataForProcess.containsKey(userId)) {
        	ArrayList<String> ai = new ArrayList<String>();
        	ai.add(userName);
        	ai.add(billingAddress);
        	ai.add(amount);
        	ai.add(mobileNum);
      	
        	dataForProcess.put(userId, ai);
    	}
    	boolean successfulPayment = new PaymentProcessor().process_payment(dataForProcess, incomingReqMobile.paymentMode, ProcessRequestQueue, encryptData);
	   System.out.println("successful sent data for processing mobile Wallet Payment");
   }
    }
   
    
    // For all exceptions occurred
    catch(Exception e) {
    	System.out.println("Exception occured");
    	logData.severe(e+"");
    	e.printStackTrace();
    }
    }
}