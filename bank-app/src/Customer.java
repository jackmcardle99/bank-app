import jdk.swing.interop.SwingInterOpUtils;

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
    /*public void logIn(Connection conn) throws SQLException {
        try{
            String query = "SELECT * FROM CUSTOMERS;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            //System.out.println(rs.toString());
            while (rs.next()){
                custID = rs.getInt("custID");
                prefix = rs.getString("prefix");
                forename = rs.getString("forename");
                surname = rs.getString("surname");
                gender = rs.getString("gender");
                dob = rs.getDate("dob");
                Customer cust = new Customer(custID,prefix,forename,surname,gender,dob);
                System.out.println(cust);
            }

        }
        catch (Exception e ){
            System.out.println(e);
        }

    }*/

    public boolean findUser(Connection conn,String user) throws SQLException {
        String passQuery = "select * from (SELECT username FROM credentials" + //sql query to search database for username passed
                " WHERE username =" + " " + "\"" + user + "\") cu";
        Statement statement = conn.createStatement(); //
        ResultSet rs = statement.executeQuery(passQuery); //save results of statement to rs
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
        String passQuery = "select * from (SELECT password FROM CREDENTIALS WHERE password = " + "\"" + pass + "\"" + " AND " +
                "username = " + "\"" + user + "\") Cp";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(passQuery);
            if(rs.next()){
                do {
                    System.out.println("ye");
                    return true;
                }while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e);;
        }
        return false;
    }

    public Customer setCurrentUser(){
        //this method will set the an object of this class, of the current user logged in
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
