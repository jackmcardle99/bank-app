import java.util.Scanner;
import java.sql.*;

public class Bank {
    private final Scanner scan = new Scanner(System.in);
    private final Customer customer = new Customer(); //for calling customer class methods
    private Customer session; //assigned when logged in, this is for the currently logged in customer



    public static void main(String[] args) throws SQLException {
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
                this.logIn();
                break;
            }
            if (input.equals("2")){
                this.applyForm();
                //customer.createCustomerProfile(this.dbConnect(),this.applyForm());
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            }
        }
    }
    public Connection dbConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db","root","76647664");
        }
        catch (Exception e ){
            throw new RuntimeException("Can't connect to database.",e);
        }
    }
    private void logIn() throws SQLException {
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

    private String[] applyForm(){
        String[]questionsArr = {"Please enter in your title e.g. (Mr/Ms/Mrs)","Please enter in your " +
                "forename.", "Please enter in your surname.","Please enter in your gender e.g. (Male/Female)" +
                "","Please Enter your date of birth. e.g. (YYYY-MM-DD)","Please enter in your new username. Cannot be " +
                "longer than 15 characters.","Please enter in your new password. Cannot be longer than 15 characters."};
        String[]answersArr = new String[questionsArr.length];
        boolean dontProceed = true;
        int count = 0;
        for (int i = 0; i < questionsArr.length; i++){
            while (true){
                System.out.println(questionsArr[i]);
                String answer = scan.nextLine();
                answersArr[i] = answer;
                //boolean test = customer.validateApplication(answersArr);
                if (customer.validateApplication(answersArr[i],count)){
                    break;
                }
            }
            count ++;
            }
        //customer.validateApplication(answersArr);
        return answersArr;
    }

    private void home(){
        System.out.println("==================== Welcome to your Online Banking " + session.getPrefix() + " " + session.getSurname() +
                " =========================\n" +
                "Lists of accounts");
        System.out.println();
    }
}
