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

}
