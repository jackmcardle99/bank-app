public class StandardAccount extends Account{

    private double fee = 2.5;
    public StandardAccount(String accountNo, double balance, String accountType, int custID) {
        super(accountNo, balance, accountType, custID);
    }

    public StandardAccount(){ //Constructor overloading

    }
}
