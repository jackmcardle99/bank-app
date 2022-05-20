import jdk.swing.interop.SwingInterOpUtils;

import java.lang.reflect.Array;
import java.sql.*;

public class Customer {

    private int custID;
    private String prefix,forename,surname,gender;
    private Date dob;

    public Customer(int custID, String prefix, String forename, String surname, String gender, Date dob){
        this.custID = custID;
        this.prefix = prefix;
        this.forename = forename;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
    }

    public Customer(){}

    public String toString(){
        return custID + " | " + prefix + "| " + forename + " | " + surname + " | " + gender + " | " + dob;
    }

    public void validateApplication(){};
    public int getNewID(Connection conn) throws SQLException {
        //This code below is getting the ID of the new customer, so we can add it to the credentials table
        PreparedStatement getMaxID = conn.prepareStatement("SELECT  MAX(custID) FROM customers");
        ResultSet rs = getMaxID.executeQuery();
        int id = 0;
        while(rs.next()){//set int as next ID num
            id = rs.getInt(1) +1;

        }
        return id;
    }
    public void createCustomerProfile(Connection conn, String[] answersArr) throws SQLException {
        Bank bankObj = new Bank();//obj for calling dbConnect method
        //Inserting new customer into customer table
        int newID = this.getNewID(bankObj.dbConnect());
        PreparedStatement custStatement = conn.prepareStatement("INSERT INTO customers(custID,prefix, forename, surname, " +
                "gender, dob) VALUES (?,?,?,?,?,?);");
        custStatement.setInt(1,newID);
        custStatement.setString(2, (String) Array.get(answersArr,0));
        custStatement.setString(3,(String) Array.get(answersArr,1));
        custStatement.setString(4,(String) Array.get(answersArr,2));
        custStatement.setString(5,(String) Array.get(answersArr,3));
        custStatement.setString(6, (String) Array.get(answersArr,4));
        custStatement.executeUpdate();

        //inserting customer details into credential tables
        PreparedStatement credStatement = conn.prepareStatement("INSERT INTO credentials (username,password,custID)" +
                "VALUES (?,?,?)");
        credStatement.setString(1,(String) Array.get(answersArr,5));
        credStatement.setString(2,(String) Array.get(answersArr,6));
        credStatement.setInt(3,newID);
        credStatement.executeUpdate();
    }
    public boolean findUser(Connection conn,String user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT username FROM credentials WHERE username = ?");
        statement.setString(1,user);
        ResultSet rs = statement.executeQuery(); //save results of statement to rs
        if (rs.next()) { //rs.next checks to see if results from query
            do {
                return true;
            } while (rs.next());//do-while there are results
        }
        else{
            System.out.println("Username not found.");
        }
        return false;
    }

    public boolean findPass(Connection conn, String pass,String user){
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT password FROM credentials WHERE password = ? AND username = ?");
            statement.setString(1,pass);
            statement.setString(2,user);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                do {
                    return true;
                }while (rs.next());
            }
            else{
                System.out.println("Password wrong, try again.");
            }
        } catch (SQLException e) {
            System.out.println(e);;
        }
        return false;
    }

    public int getUserID(Connection conn, String user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT custID FROM credentials WHERE username = ?");
        statement.setString(1,user);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            int userID = rs.getInt("custID");
            return userID;
        }
        return 0;
    }

    public Customer getCustomerProfile(Connection conn,int userID) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM customers WHERE custID = ?");
        statement.setInt(1,userID);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){
            do {
                custID = rs.getInt("custID");
                prefix = rs.getString("prefix");
                forename = rs.getString("forename");
                surname = rs.getString("surname");
                gender = rs.getString("gender");
                dob = rs.getDate("dob");
                Customer currentSession = new Customer(custID,prefix,forename,surname,gender,dob);
                return currentSession;
            }while (rs.next());
        }
        else{
            System.out.println("nay");
        }
        return null;
    }

    public int getCustID(){
        return this.custID;
    }

    public String getPrefix(){
        return this.prefix;
    }

    public String getForename(){
        return this.forename;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getGender(){
        return this.gender;
    }
}
