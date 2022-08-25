public class Account {

    private String accountNo, accountType;
    private int custID;
    private double balance;


    public Account(String accountNo, String accountType, int custID, double balance){
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.custID = custID;
        this.balance = balance;
    }

    public Account(){}; //contructor overloading
    public boolean validateAccount(String type, int initBalance){
        // this method will validate the account opening application
            if (type.equals("standard") && initBalance < 0){
                System.out.println("Can't add negative funds.");
                return false;
            }
            if (type.equals("pro") && initBalance < 500){
                System.out.println("Please ensure you fund the account with at least 500");
                return false;
            }
            else{
                return true;
                //account validated - then create account in table
            }
    }

    public Account findCustAccount(){
        return null;
    }
}
