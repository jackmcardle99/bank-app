public class StandardAccount extends Account{

    private final double fee = 0.025;
    public StandardAccount(int accountNo, double balance, String accountType, int custID) {
        super(accountNo, balance, accountType, custID);
    }

    public StandardAccount(){ //Constructor overloading
    }

    public double applyFee(double amount){
        return (amount * fee) + amount;
    }
}
