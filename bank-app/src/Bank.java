import java.util.Scanner;
import java.sql.*;

public class Bank {
    Customer cus1 = new Customer(01,"Mr", "Jack", "McArdle", "Male");
    Scanner scan = new Scanner(System.in);




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
       // Connection conn;
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
        Customer cust = new Customer();
        cust.logIn(dbConnect());
//        System.out.println("Please enter your username");
//        String username = scan.nextLine();
//        System.out.println("Please enter your password");
//        String password = scan.nextLine();
//
//        String query = "SELECT * FROM CUSTOMERS;";
//        Statement statement = conn.createStatement();
//        ResultSet rs = statement.executeQuery(query);
        //System.out.println(rs.toString());

    }

    private void createAccount(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db","root","76647664");
        }
        catch (Exception e ){
            System.out.println(e);
        }


    }

    private void home(){
        System.out.println("==================== Welcome to your Online Banking " + cus1.getPrefix() + " " + cus1.getSurname() +
                " =========================\n" +
                "Lists of accounts");
        Account acc1 = new Account("123","pro",01,200);
        System.out.println();
    }
}
