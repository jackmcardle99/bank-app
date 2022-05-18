import org.w3c.dom.css.CSSUnknownRule;

import java.sql.*;

public class Customer {

    private int custID;
    private String prefix,forename,surname,gender;
    //date of birth thing

    public Customer(int custID, String prefix, String forename, String surname, String gender){
        this.custID = custID;
        this.prefix = prefix;
        this.forename = forename;
        this.surname = surname;
        this.gender = gender;
    }

    public Customer(){

    }

    public String toString(){
        return custID + " " + prefix + " " + forename + " " + surname + " " + gender;
    }
    public void logIn(Connection conn) throws SQLException {
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
                Customer cust = new Customer(custID,prefix,forename,surname,gender);
                System.out.println(cust);
            }

        }
        catch (Exception e ){
            System.out.println(e);
        }

//        String query = "SELECT * FROM CUSTOMERS;";
//        Statement statement = conn.createStatement();
//        ResultSet rs = statement.executeQuery(query);
//        //System.out.println(rs.toString());
//        custID = rs.getInt("custID");
//        prefix = rs.getString("prefix");
//        forename = rs.getString("forename");
//        surname = rs.getString("surname");
//        gender = rs.getString("gender");
//        Customer cust = new Customer(custID,prefix,forename,surname,gender);
//        System.out.println(cust.toString());

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
