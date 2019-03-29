package glassdoor;

// depending on the payment mode, the additional details are requested 
// this class is for credit/debit card payment 
public class IncomingRequestCard extends IncomingRequest {
  
  public String cardnumber;
  public int securepin;
  // expiry date format "MM/yy"
  public String expiryDate;

}