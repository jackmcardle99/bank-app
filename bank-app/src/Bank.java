import java.util.Scanner;
import java.sql.*;

public class Bank {
    //Customer cus1 = new Customer(01,"Mr", "Jack", "McArdle", "Male", "1999-06-23");
    Scanner scan = new Scanner(System.in);
    Customer customer = new Customer();



    public static void main(String args[]) throws SQLException {

        Bank app = new Bank();
        app.menu();
    }

    private void menu() throws SQLException {
        while (true) {
            System.out.println("(1) Log in\n(2) Apply for Account\n(3) Exit");
            String input = scan.nextLine();
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Please enter valid input, either 1 or 2.");
            }
            if (input.equals("2")){
                this.createAccount();
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            } else {
                this.logIn(dbConnect());
            }
            break;
        }
    }
    public Connection dbConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db","root","76647664");
            return conn;
        }
        catch (Exception e ){
            System.out.println(e);
        }
        return null;

    }
    private void logIn(Connection conn) throws SQLException {
        boolean userFound = false, passFound = false;
        String user = null;
        while (userFound != true)
        {
            System.out.println("Please enter in your username.");
            user = scan.nextLine();
            userFound = customer.findUser(this.dbConnect(),user);
        }
        while (passFound != true){
            System.out.println("Please enter in the password for " + user);
            String pass = scan.nextLine();
            passFound = customer.findPass(this.dbConnect(),pass,user);
        }
        if (userFound && passFound) { //if both are true

        }
    }

    private void createAccount(){
        System.out.println("Please enter in your new username. Cannot be longer than 15 characters.");
        String newUser = scan.nextLine();
        System.out.println("Please enter in your new password. Cannot be longer than 15 characters.");
        String newPass = scan.nextLine();

    }

    private void home(){
//        System.out.println("==================== Welcome to your Online Banking " + cus1.getPrefix() + " " + cus1.getSurname() +
//                " =========================\n" +
//                "Lists of accounts");
        Account acc1 = new Account("123","pro",01,200);
        System.out.println();
    }
}
