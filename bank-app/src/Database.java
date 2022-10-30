import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {

    private final Scanner scan = new Scanner(System.in);
    //method for connecting to mysql database
    public Connection dbConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db","root","76647664");
        }
        catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException("Can't connect to database.",e);
        }
    }

    private int getNewID() throws SQLException {
        //This code below is getting the ID of the new customer, so we can add it to the credentials table
        PreparedStatement getMaxID = dbConnect().prepareStatement("SELECT  MAX(custID) FROM customers");
        ResultSet rs = getMaxID.executeQuery();
        int id = 0;
        while(rs.next()){//set int as next ID num
            id = rs.getInt(1) +1;
        }
        return id;
    }

    public void createCustomerProfile(String[] answersArr, String strDob) throws SQLException {
        //Inserting new customer into customer table
        int newID = getNewID();
        PreparedStatement custStatement = dbConnect().prepareStatement("INSERT INTO customers(custID,prefix, forename, surname, " +
                "gender, dob) VALUES (?,?,?,?,?,?);");
        custStatement.setInt(1,newID);
        custStatement.setString(2, (String) java.lang.reflect.Array.get(answersArr,0));
        custStatement.setString(3,(String) java.lang.reflect.Array.get(answersArr,1));
        custStatement.setString(4,(String) java.lang.reflect.Array.get(answersArr,2));
        custStatement.setString(5,(String) java.lang.reflect.Array.get(answersArr,3));
        custStatement.setString(6, strDob);
        custStatement.executeUpdate();

        //inserting customer details into credential tables
        PreparedStatement credStatement = dbConnect().prepareStatement("INSERT INTO credentials (username,password,custID)" +
                "VALUES (?,?,?)");
        credStatement.setString(1,(String) java.lang.reflect.Array.get(answersArr,4));
        credStatement.setString(2,(String) Array.get(answersArr,5));
        credStatement.setInt(3,newID);
        credStatement.executeUpdate();
    }

    //this method is called when user tries to log in and enters username
    public boolean findUserProfile(String user) throws SQLException {
        PreparedStatement statement = dbConnect().prepareStatement("SELECT username FROM credentials WHERE username = ?");
        statement.setString(1,user);
        ResultSet rs = statement.executeQuery(); //save results of statement to rs
        if (rs.next()) { //rs.next checks to see if results from query
            return true;
        }
        else{
            System.out.println("Username not found.");
        }
        return false;
    }

    //this method looks for user password when they try to log in
    public boolean findPass(String pass,String user){
        try {
            PreparedStatement statement = dbConnect().prepareStatement("SELECT password FROM credentials WHERE password = ? AND username = ?");
            statement.setString(1,pass);
            statement.setString(2,user);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                System.out.println("Password wrong, try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // this method finds the custID once user successfully logs in, assigns it to current session
    public int getUserSessionID(String user) throws SQLException {
        PreparedStatement statement = dbConnect().prepareStatement("SELECT custID FROM credentials WHERE username = ?");
        statement.setString(1,user);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){
            return rs.getInt("custID");
        }
        return 0;
    }

    // this method finds the profile of the customer who logged in - creating the session id
    public Customer getCustomerProfile(int userID) throws SQLException {
        Customer currentSession = null;
        PreparedStatement statement = dbConnect().prepareStatement("SELECT * FROM customers WHERE custID = ?");
        statement.setInt(1,userID);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
             currentSession = new Customer(rs.getInt("custID"),rs.getString("prefix")
                    ,rs.getString("forename"),rs.getString("surname"),
                    rs.getString("gender"),rs.getString("dob"));
        }
        return currentSession;
    }

    private int getNewAccID() throws SQLException {
        //This code below is getting the ID of the new account, so we can add it to the accounts table
        PreparedStatement getMaxID = dbConnect().prepareStatement("SELECT  MAX(accountNo) FROM accounts");
        ResultSet rs = getMaxID.executeQuery();
        int accNO = 0;
        while(rs.next()){//set int as next ID num
            accNO = rs.getInt(1) +1;

        }
        return accNO;
    }

    public int getNewPayeeID() throws SQLException {
        int payeeNo = 0;
        PreparedStatement getMaxID = dbConnect().prepareStatement("SELECT MAX(payeeID) FROM payee");
        ResultSet rs = getMaxID.executeQuery();
        while (rs.next()){
            payeeNo = rs.getInt(1) +1;
        }
        return payeeNo;
    }
    public void createCustAccount(int cust, String type, int initBal) throws SQLException {
        int newAccNo = getNewAccID();//getting new account number
        System.out.println(newAccNo);
        PreparedStatement statement = dbConnect().prepareStatement("INSERT INTO accounts (accountNo,balance,accountType," +
                "custID)VALUES (?,?,?,?)");
        statement.setInt(1,newAccNo);
        statement.setLong(2,initBal);
        statement.setString(3,type);
        statement.setInt(4,cust);
        statement.executeUpdate();
    }

    public ArrayList<Account> findUserAccounts(int custID) throws SQLException {
        //Account account = new Account();
        ArrayList<Account> accountList = new ArrayList<>();
        PreparedStatement statement = dbConnect().prepareStatement("SELECT * FROM accounts WHERE custID = ?");
        statement.setInt(1,custID);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            if (rs.getString("accountType").equals("pro")){ //if the account is a pro account, create pro account object
                ProfessionalAccount proAccount;
                proAccount = new ProfessionalAccount(rs.getString("accountNo"),rs.getDouble("balance"),rs.getString("accountType"),rs.getInt("custID"));
                accountList.add(proAccount); //creating an instance of pro account class
            }
            else{
                StandardAccount stnAccount;
                stnAccount = new StandardAccount(rs.getString("accountNo"),rs.getDouble("balance"),rs.getString("accountType"),rs.getInt("custID"));
                accountList.add(stnAccount); //creating and instance of standard account class
            }
        }
        return accountList; //returning arraylist with the user's accounts
    }

    //this method is for validating payee's account exists
    private boolean findPayeeAccount(int payeeAcc) throws SQLException {
        //code in here for finding payee account, if account found - return true, else false
        PreparedStatement statement = dbConnect().prepareStatement("SELECT COUNT(1) FROM accounts WHERE accountNo = ?");
        statement.setInt(1,payeeAcc);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            //if account exists, = 1 (true), else = 0 (false)
            return rs.getInt(1) == 1;
        }
        return false;
    }

    public String findAccountNum(int custID) throws SQLException {
        PreparedStatement statement = dbConnect().prepareStatement("SELECT accountNo from accounts where custID = ?");
        statement.setInt(1,custID);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }
    public void fundAccount(String accountNo) throws SQLException {
        double fundAmount;
        System.out.println("How much would you liked to fund your account by?");
        fundAmount = scan.nextDouble();
        PreparedStatement fundStatement = dbConnect().prepareStatement("UPDATE accounts SET balance = balance + ? WHERE accountNo = ?");
        fundStatement.setDouble(1, fundAmount);
        fundStatement.setString(2,accountNo);
        fundStatement.executeUpdate();
    }

    public void addPayee(String payeeName,int custID, int payeeAccNo) throws SQLException {
        if(findPayeeAccount(payeeAccNo)){
            PreparedStatement statement = dbConnect().prepareStatement("INSERT INTO payee (payeeID,payeeName,payeeAccNo,custAccNo)"+
                    "VALUES (?,?,?,?)");
            statement.setInt(1,getNewPayeeID());
            statement.setString(2,payeeName);
            statement.setInt(3,payeeAccNo);
            statement.setInt(4,custID);
            statement.executeUpdate();
        }
        else System.out.println("That account number doesn't exist.");
    }

    public String viewPayees(int custID) throws SQLException {
       StringBuilder payeeList = new StringBuilder();
        PreparedStatement statement = dbConnect().prepareStatement("SELECT payeeID, payeeName, payeeAccNo FROM payee WHERE custAccNo = ?;");
        statement.setInt(1,custID);
        ResultSet rs = statement.executeQuery();

        while (rs.next()){ //using string-builder because if concatenating a string in a loop, it creates a new object everytime
                            // https://stackoverflow.com/questions/7817951/string-concatenation-in-java-when-to-use-stringbuilder-and-concat
            payeeList.append(" PAYEE ID: ").append(rs.getString("payeeID")).append(" ---").append(" PAYEE NAME: ").append(rs.getString("payeeName")).append(" ---").append(" ACCOUNT NUMBER: ").append(rs.getInt("payeeAccNo")).append("\n");
        }
        return payeeList.toString();
    }

    public boolean payeeExists(int payeeID) throws SQLException {
        PreparedStatement statement = dbConnect().prepareStatement("SELECT payeeID from payee where payeeID = ?");
        statement.setInt(1,payeeID);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            if(rs.getInt("payeeID") == payeeID) return true;
        }
        return false;
    }
    public void removePayees(int payeeID) throws SQLException {
        if(payeeExists(payeeID)){
            PreparedStatement statement = dbConnect().prepareStatement("DELETE FROM payee WHERE payeeID = ?;");
            statement.setInt(1,payeeID);
            statement.executeUpdate();
        }
        else System.out.println("Payee ID does not exist.");
    }

    public void sendPayment(int payeeID, int custID) throws SQLException {
        //PreparedStatement statement = dbConnect().prepareStatement("");
        System.out.println("THIS IS WORKING IF THIS SHOWS");
    }

    private void updateCustomerBalance(){}

    private void updatePayeeBalance(){}
}
