package glassdoor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
//package com.glassdoor.test.intern.first;
import java.util.concurrent.locks.ReentrantLock;

public class PaymentProcessor {

	/* if multiple request come in, they are stored in the queue based on user Id and executed on FirstComeFrist Serve basis
	 Using locks for concurrency 
	 all the data compared is in its encrypted form
	 the data sent to submitPayment method is also is encrypted form, the HashMap containing the actual key, value pairs is passed, the actual data from the HashMap should be used while processing
	*/
	
public boolean process_payment(HashMap<String,ArrayList<String>> dataToProcess,String paymentMode,Queue<String> processlist,HashMap<String,String> encryptdata ) throws FileNotFoundException {
	
	//BufferedReader br = new BufferedReader(new FileReader("/Users/harika/Desktop/paymentStatus.txt"));
	
	ReentrantLock lock = new ReentrantLock();
	
	
		while(processlist.size() != 0) {
		// get the one request at a time from the process request queue
		String currentRequest = processlist.remove();
		int submitrequest = 0;
		// using locks for concurrency, so that only one request from the queue is processed at a time
		lock.lock();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/Users/harika/Desktop/paymentStatus.txt"));
			
		if(dataToProcess.containsKey(currentRequest)) {
			System.out.println("Valid Request from the payment Application");
			
			// submit the payment details based on the payment mode
			// here we are using encrypted data 
			ArrayList<String> paymentDetails = new ArrayList<String>();
			paymentDetails = dataToProcess.get(currentRequest);
			if(paymentDetails.contains(paymentMode) && paymentMode =="credit/debit") {
				submitrequest = 1;
				//card number is in encrypted form
				// index in paymentDetails list refers to 0-> userID, 1->userName, 2-> amount, 3->cardNumber, 4->securepin
				String amount = paymentDetails.get(2);
				String CardNum = paymentDetails.get(3);
				String securepin = paymentDetails.get(4);
				submitPayment(encryptdata, amount, CardNum, securepin);
				System.out.println("Credit/debit card transaction request submitted");
				return true;
			
			}
			if(paymentDetails.contains(paymentMode) && paymentMode == "mobileWallet") {
				submitrequest = 1;
				//Mobile Number & amount is in encrypted form
				// index in paymentDetails list refers to 0-> userID, 1->userName, 2-> amount, 3->MobileNum
				String amount = paymentDetails.get(2);
				String mobileNum = paymentDetails.get(3);
				submitPayment(encryptdata, amount, mobileNum);
				System.out.println("Mobile Wallet transaction request submitted");
				return true;
			
			}
			if(submitrequest == 0) {
				return false;
			}
		
		}
	}
		catch(Exception e) {
			
			e.printStackTrace();
			return false;
		}
		finally {
			lock.unlock();
		}
		}
	return false;
	
}
public void submitPayment(HashMap<String,String> encryptData, String amount, String mobileNum) {
	//function overloading 
	// this is called for mobilewallet processing
}
  public void submitPayment(HashMap<String,String> encryptData,String amount,String card,String securepin) {
    //Don't implement this.
	  //this function is called for processing credit/debit card transaction
  }
}
