public class ProfessionalAccount extends Account{

    private double fee = 1.25;

    public ProfessionalAccount(String accountNo, double balance, String accountType, int custID) {
        super(accountNo, balance, accountType, custID);
    }

    public ProfessionalAccount(){ //constructor overloading
    }
}
