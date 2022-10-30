public class ProfessionalAccount extends Account{

    private final double fee = 0.0125;

    public ProfessionalAccount(String accountNo, double balance, String accountType, int custID) {
        super(accountNo, balance, accountType, custID);
    }

    public ProfessionalAccount(){ //constructor overloading
    }

    public double applyFee(double amount){
        return (amount * fee) + amount;
    }
}
