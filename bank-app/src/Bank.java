import java.util.Scanner;

public class Bank {
    Customer cus1 = new Customer(01,"Mr", "Jack", "McArdle", "Male");

    Scanner scan = new Scanner(System.in);
    public static void main(String args[]){
        Bank app = new Bank();
        app.logIn();
    }

    private void logIn(){
        while (true) {
            System.out.println("(1) Log in\n(2) Apply for Account\n(3) Exit");
            String input = scan.nextLine();
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Please enter valid input, either 1 or 2.");
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            } else {
                this.menu();
            }
            break;
        }
    }

    private void createAccount(){

    }

    private void menu(){
        System.out.println("==================== Welcome to your Online Banking " + cus1.getPrefix() + " " + cus1.getSurname() +
                " =========================\n" +
                "Lists of accounts");
        Account acc1 = new Account("123","pro",01,200);
        System.out.println();
    }
}
