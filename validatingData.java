package glassdoor;

import java.util.Date;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class validatingData {
	Boolean cardValid;
	Boolean mobileValid;
	Boolean pinValid;
	Boolean dateValid;
	public validatingData(IncomingRequestCard request){
		// Do validations only for the corresponding payment mode
		if(request.paymentMode == "credit/debit") {
		Long cardNum = Long.parseLong(request.cardnumber+"");
		//check if the entered card Number is Valid
	    cardValid = isValid(cardNum);
	    dateValid = isValidExpiryDate(request.expiryDate);
	    pinValid = isValidSecurePin(request.securepin);
		}
	    
		}
	public validatingData(IncomingReqMobile request) {
		if(request.paymentMode == "mobilewallet") {
	    	mobileValid = isValidMobileNum(request.mobileNum);
	    }
	}
	//validating the credit card Number received from the incomingRequest
    //Checking the length of the card Number entered and the valid prefixes allowed
	public boolean isValid(long number) {
		return (getSize(number) >= 13 && getSize(number) <= 16 && (prefixMatched(number,4) || prefixMatched(number,5) || prefixMatched
				(number, 37) || prefixMatched(number, 6)));
	}
	public int getSize(long num) {
		String num1 = num+"";
		return num1.length();
	}
	//prefix of card Number starting with 4 -> Visa Card
	//prefix of card number starting with 5 -> Master Card
	//prefix of card Number starting with 37 -> American Express Card
	// prefix of card Number starting with 6 -> Discover Card
	public  boolean prefixMatched(long Number, int d) {
		String num = Number+"";
		int number = Integer.parseInt(num.substring(0,getSize(d)));
		if(number == d) return true;
		else return false;			
	}

	public boolean isValidMobileNum(String number) {
		// Validating US format mobile numbers
		// Mobile Number of format (234)-565-6766, 2346543456, 234-565-6766 are the valid formats
		String regEx = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		
		if(Pattern.compile(regEx).matcher(number).matches()) {
			return true;
		}
		else {
		return false;
		}
	}
	// Check if the cvv number is digit and contains 3 digits or not
	public boolean isValidSecurePin(int number) {
		String num = number+"";
		
		if(num.matches("[0-9]+") && num.length() == 3) return true;
		else
			return false;
		
	}
	//date of format MM/yy used for debit & credit card transactions
	public boolean isValidExpiryDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
		Boolean returnVal = false;
		try {
			Date expiry = simpleDateFormat.parse(date);
			returnVal = expiry.before(new Date());
			System.out.println(returnVal);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return returnVal;
		
	}
}
	

