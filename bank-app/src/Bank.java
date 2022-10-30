import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class Bank {
    private final LocalDate currentDate = LocalDate.now(Clock.systemUTC());

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Scanner scan = new Scanner(System.in);
    private final Customer customer = new Customer(); //for calling customer class methods
    private Customer session; //assigned when logged in, this is for the currently logged in customer
    private final Database db = new Database();//instantiating Database class obj



    public static void main(String[] args) throws SQLException {
        Bank app = new Bank();
        app.menu();
    }

    private void menu() throws SQLException {
        String input = "";
        while (!input.equals("3")) {
            System.out.println("(1) Log in\n(2) Apply for Profile\n(3) Exit");
            input = scan.nextLine();
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Please enter valid input, either 1 or 2.");
            }
            if(input.equals("1")){
                this.logIn();
                break;
            }
            if (input.equals("2")){
                String strDob;
                try {
                    strDob = this.applyFormDOB();
                    db.createCustomerProfile(applyForm(),strDob);
                }catch (NumberFormatException | DateTimeException e){
                    System.out.println(e.getMessage() + ". Please enter a valid date.");
                }
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            }
        }
    }

    private void logIn() throws SQLException {
        boolean userFound = false, passFound = false;
        String user = null;
        while (!userFound)
        {
            System.out.println("Please enter in your username.");
            user = scan.nextLine();
            userFound = db.findUserProfile(user);
        }
            while (!passFound){
                System.out.println("Please enter in the password for " + user);
                String pass = scan.nextLine();
                passFound = db.findPass(pass,user);
            }
        int userID = db.getUserSessionID(user);
        session = db.getCustomerProfile(userID);
        try {
            this.home();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String applyFormDOB(){
        String strDay,strMonth,strYear;
        LocalDate dateDob;
        String strDate;

        while (true){
            System.out.println("Please enter your day of birth");
            strDay = scan.nextLine();
            System.out.println("Please enter your month of birth");
            strMonth = scan.nextLine();
            System.out.println("Please enter your year of birth");
            strYear = scan.nextLine();

            if (customer.validateDob(strDay,strMonth,strYear)){//if method returns true
                int day = Integer.parseInt(strDay);
                int month = Integer.parseInt(strMonth);
                int year = Integer.parseInt(strYear);
                dateDob = LocalDate.of(year,month,day);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");//date formatter object
                strDate = dateDob.format(formatter);//string is date with the format applied
                break;
            }
        }
        return strDate;
    }

    private String[] applyForm(){
        String[]questionsArr = {"Please enter in your title e.g. (Mr/Ms/Mrs)","Please enter in your " +
                "forename.", "Please enter in your surname.","Please enter in your gender e.g. (Male/Female)","Please " +
                "enter in your new username. Cannot be " +
                "longer than 15 characters.","Please enter in your new password. Password can contain special characters " +
                "but cannot be longer than 15 characters."};
        String[]answersArr = new String[questionsArr.length];
        int count = 0;
        for (int i = 0; i < questionsArr.length; i++){
            do {
                System.out.println(questionsArr[i]);
                String answer = scan.nextLine();
                answersArr[i] = answer;
            } while (!customer.validateApplication(answersArr[i], count));
            count ++;
            }
        return answersArr;
    }

    private void openAccForm() throws SQLException {
        Account acc = new Account();
        String userChoice, accChoice = null;
        int initialBalance = 0;
        //ask if they want to open pro or standard (if they choose pro min deposit is 500)
        //(cust can only have 1 pro acc)
        //ask them how much they would like to deposit initially
        System.out.println("""
                Would you like to open a Professional or Standard account? (You may only have 1 professional account, and it must be opened with a minimum balance of £500)
                (1) Standard Account
                (2) Professional Account""");
        userChoice = scan.nextLine();
        boolean validated = false;
        do { //do-while loop for initial deposit into account, calling validate method from account class
            if (userChoice.equals("1")){
                accChoice = "standard";
                System.out.print("Please enter your initial deposit amount. Can be 0.\nEnter amount: ");
                initialBalance = scan.nextInt();
                validated = acc.validateAccount(accChoice,initialBalance);
            }
            else if (userChoice.equals("2")) {
                accChoice = "pro";
                System.out.print("Please fund your account with £500 minimum.\nEnter amount: ");
                initialBalance = scan.nextInt();
                validated = acc.validateAccount(accChoice,initialBalance);
            }
        }while (!validated);
        db.createCustAccount(session.getCustID(),accChoice,initialBalance);

    }

    private void paymentForm() throws SQLException {
        String userInput;
        double userAmount;
        while(true){
            System.out.println(db.viewPayees(session.getCustID()));
            System.out.println("Please choose a payee from the list above."); userInput = scan.nextLine();
            if(db.payeeExists(Integer.parseInt(userInput))){
                System.out.println("Please enter amount you wish to send. (0.00)");
                try{
                    userAmount = scan.nextDouble();
                    if(!(userAmount > 0 )){
                        System.out.println("Please enter a valid amount.");
                    }
                    else db.sendPayment(Integer.parseInt(userInput), session.getCustID());
                    break;

                }catch (InputMismatchException | NumberFormatException ex){
                    System.out.println("Please enter a valid amount.");
                }
            }
            else System.out.println("Payee doesn't exist.");
        }
    }

    private void payeeForm() throws SQLException {
        String userInput;
        System.out.println("What would you like to do?" +
                "(1)Add payee" +
                "(2)Remove payee" +
                "(3)View payees");
        userInput = scan.nextLine();
        switch (userInput) {
            case "1" -> {
                String payeeName; int payeeAccNo;
                System.out.println("Enter payee name."); payeeName = scan.nextLine();
                System.out.println("Enter payee account number."); payeeAccNo = scan.nextInt();
                db.addPayee(payeeName,session.getCustID(),payeeAccNo);
            }
            case "2" -> {
                System.out.println(db.viewPayees(session.getCustID())); //printing out list of payees
                int userInpt;
                System.out.println("Please enter the ID of the payee you'd like to remove."); userInpt = scan.nextInt();
                System.out.println(db.payeeExists(userInpt));
                if (db.payeeExists(userInpt)) db.removePayees(userInpt);
                else System.out.println("Payee ID not correct");
            }
            case "3" ->
                    System.out.println(db.viewPayees(Integer.parseInt(db.findAccountNum(session.getCustID())))); //printing out list of payees
            default -> System.out.println("Please enter valid response.");
        }
    }

    private String selectAccount(ArrayList<Account> accArray){
        String selectedAcc;
        System.out.println("Which account would you like to select?\nEnter the account number.");
        for (Account acc : accArray){ //listing the accounts the user has
            System.out.println(acc.AcctoString());
        }
        selectedAcc = scan.nextLine();
        for (Account acc : accArray){ //listing the accounts the user has
            if (selectedAcc.equals(acc.getAccountNo())){
                return acc.getAccountNo(); //return account if user's input matches an account number on record
            }
        }
        return null;
    }
private void home() throws InterruptedException, SQLException {
    String userInput;
/// ARRAYLIST FOR STORING AND DISPLAYING CUSTOMER ACCOUNTS HERE
    label:
    while (true){//this loop condition will change in the future, true for the purposes of seeing the home menu atm
        final LocalTime currentTime = LocalTime.now();
        String strTime = currentTime.format(timeFormatter);
        String strDate = currentDate.format(dateFormatter);
        ArrayList<Account> accountList = db.findUserAccounts(session.getCustID()); //adding accounts to the arraylist

       // System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("==================== Welcome to your Online Banking " + session.getPrefix() + " " +
                "" + session.getSurname() +
                " =========================\n" +
                "Date: " + strDate + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Time: " + strTime);
        System.out.println("\n-- YOUR ACCOUNTS --");
        for (Account acc : accountList)
        { //this for-loop is printing out to string all the customer's accounts in the home menu
            System.out.println(acc.AcctoString());
        }
        System.out.println("----------------------------");
        System.out.println("""

                Enter what you would like to do.
                (1)Fund an Account
                (2)Make a Payment
                (3)View Payees
                (4)View Transactions
                (5)Open Account
                (6)Close Account
                (7)Exit""");
        //need to create sql query that searches for all accounts beloning to currently logged in customer - using
        // the session.getCustID variable - e.g. SELECT * FROM ACCOUNTS WHERE CUSTID = ....
        // Then add them into an ArrayList for manipulation of data
        // after that's done, then proceed to allow customer to add balance etc..
        userInput = scan.nextLine();
        switch (userInput) {
            case "1":
                //this method allows the user to select which account they want to modify
                db.fundAccount(this.selectAccount(accountList));
                break;
            case "2":
                paymentForm();
                break;
            case "3":
                this.payeeForm(); //need to create method to check if payee account number exists
                break;
                //call method here that makes payment
            case "5":
                this.openAccForm();
                break;
            case "7":
                System.out.println("Goodbye!");
                break label;
        }



    }
}
}
