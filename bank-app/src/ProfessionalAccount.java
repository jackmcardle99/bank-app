public class ProfessionalAccount extends Account{

    public ProfessionalAccount(String accountNo, double balance, String accountType, int custID) {
        super(accountNo, balance, accountType, custID);
    }

    public ProfessionalAccount(){ //constructor overloading
    }

    public double applyFee(double amount){
        double fee = 0.0125;
        return (amount * fee) + amount;
    }
}
