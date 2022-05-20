import java.util.Scanner;
import java.sql.*;

public class Bank {
    //Customer cus1 = new Customer(01,"Mr", "Jack", "McArdle", "Male", "1999-06-23");
    private Scanner scan = new Scanner(System.in);
    private Customer customer = new Customer(); //for calling customer class methods
    private Customer session; //assigned when logged in, this is for the currently logged in customer



    public static void main(String args[]) throws SQLException {

        Bank app = new Bank();
        app.menu();
    }

    private void menu() throws SQLException {
        String input = "";
        while (!input.equals("3")) {
            System.out.println("(1) Log in\n(2) Apply for Account\n(3) Exit");
            input = scan.nextLine();
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Please enter valid input, either 1 or 2.");
            }
            if(input.equals("1")){
                this.logIn(dbConnect());
                break;
            }
            if (input.equals("2")){
                this.applyForm();
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            }
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
        while (!userFound)
        {
            System.out.println("Please enter in your username.");
            user = scan.nextLine();
            userFound = customer.findUser(this.dbConnect(),user);
        }
            while (!passFound){
                System.out.println("Please enter in the password for " + user);
                String pass = scan.nextLine();
                passFound = customer.findPass(this.dbConnect(),pass,user);
            }
        int userID = customer.getUserID(dbConnect(),user);
        session = customer.getCustomerProfile(this.dbConnect(),userID);
        this.home();
    }

    private void applyForm(){
        String[]questions = {"Please enter in your title e.g. (Mr/Ms/Mrs","Please enter in your forename.", "Please enter in your surname.","Please enter in your gender e.g. (Male/Female)","Please Enter your date of birth. e.g. (YYYY-MM-DD)","Please enter in your new username. Cannot be longer than 15 characters.","Please enter in your new password. Cannot be longer than 15 characters."};
        String[]answers;
        //create for loop here which prints a question on each iteration, and adds answer to answer array
        //COULD POSSIBLY CREATE HASHMAP?? QUESTIONS = KEY ANSWERS = VALUE?
//        for (String lines : questions){
//            System.out.println(lines);
//        }

    }

    private void createProfile(){

    }

    private void home(){
        System.out.println("==================== Welcome to your Online Banking " + session.getPrefix() + " " + session.getSurname() +
                " =========================\n" +
                "Lists of accounts");
        Account acc1 = new Account("123","pro",01,200);
        System.out.println();
    }
}
