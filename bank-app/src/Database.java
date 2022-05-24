import java.lang.reflect.Array;
import java.sql.*;

public class Database {

    //method for connecting to mysql database
    public Connection dbConnect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db","root","76647664");
        }
        catch (Exception e ){
            throw new RuntimeException("Can't connect to database.",e);
        }
    }

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

    public void createCustomerProfile(Connection conn, String[] answersArr, String strDob) throws SQLException {
        //Inserting new customer into customer table
        int newID = this.getNewID(this.dbConnect());
        PreparedStatement custStatement = conn.prepareStatement("INSERT INTO customers(custID,prefix, forename, surname, " +
                "gender, dob) VALUES (?,?,?,?,?,?);");
        custStatement.setInt(1,newID);
        custStatement.setString(2, (String) java.lang.reflect.Array.get(answersArr,0));
        custStatement.setString(3,(String) java.lang.reflect.Array.get(answersArr,1));
        custStatement.setString(4,(String) java.lang.reflect.Array.get(answersArr,2));
        custStatement.setString(5,(String) java.lang.reflect.Array.get(answersArr,3));
        custStatement.setString(6, strDob);
        custStatement.executeUpdate();

        //inserting customer details into credential tables
        PreparedStatement credStatement = conn.prepareStatement("INSERT INTO credentials (username,password,custID)" +
                "VALUES (?,?,?)");
        credStatement.setString(1,(String) java.lang.reflect.Array.get(answersArr,4));
        credStatement.setString(2,(String) Array.get(answersArr,5));
        credStatement.setInt(3,newID);
        credStatement.executeUpdate();
    }

    //this method is called when user tries to log in and enters username
    public boolean findUserProfile(Connection conn,String user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT username FROM credentials WHERE username = ?");
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
    public boolean findPass(Connection conn, String pass,String user){
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT password FROM credentials WHERE password = ? AND username = ?");
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
    public int getUserSessionID(Connection conn, String user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT custID FROM credentials WHERE username = ?");
        statement.setString(1,user);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){
            return rs.getInt("custID");
        }
        return 0;
    }

    // this method finds the profile of the customer who logged in
    public Customer getCustomerProfile(Connection conn,int userID) throws SQLException {
        Customer currentSession = null;
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM customers WHERE custID = ?");
        statement.setInt(1,userID);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
             currentSession = new Customer(rs.getInt("custID"),rs.getString("prefix")
                    ,rs.getString("forename"),rs.getString("surname"),
                    rs.getString("gender"),rs.getString("dob"));
        }
        return currentSession;
    }

}
