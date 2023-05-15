import java.util.ArrayList;
public class Account {

    private String accountType;
    private int custID, accountNo;
    private double balance;
    ArrayList<Account> accountList = new ArrayList<Account>(); //instantiating arraylist

    public Account(int accountNo, double balance, String accountType, int custID){
        this.accountNo = accountNo;
        this.balance = balance;
        this.accountType = accountType;
        this.custID = custID;
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

    public String AcctoString(){ //RENAME THIS METHOD
        String accountOutput = "Account Number: " + this.accountNo + ", " + "Balance: " + this.balance + ", " +
                "Account Type: " + this.accountType;
        return accountOutput;
    }

    public double applyFee(double amount){ //applies payment fee and returns amount with fee
        double fee = 0.025;
        System.out.println((amount * fee) + amount);
        return (amount * fee) + amount;
    }

    public String AcctoString2(){ //RENAME THIS METHOD
        return "Account Number: " + this.accountNo;
    }
    public Account findCust3Account(){
        return null;
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public int getAccountNo(){
        return this.accountNo;
    }
}
