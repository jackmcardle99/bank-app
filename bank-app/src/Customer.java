import java.lang.reflect.Array;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public boolean isEmptyOrNull(String word){
        boolean test = "".equals(word);  // true
        boolean emptyOrNull = false;
        if (word == null || word.equals(test) ){
            emptyOrNull = true;
        }
        return emptyOrNull;
    }

    public boolean validateApplication(String answer, int count) {
        //MAYBE SWITCH CASE WITH COUNT ++?
        switch (count){
            case 0:
                if (!answer.equals("Mr") && !answer.equals("Ms") && !answer.equals("Mrs")){
                    System.out.println("Incorrect format for prefix - please ensure (Mr/Ms/Mrs)");
                    return false;
                }
                break;
            case 1:
                if (answer.length() > 15 || this.isAlpha(answer) == false || this.isEmptyOrNull(answer)){
                    System.out.println("Incorrect format for forename - please ensure name contains no numerals and is" +
                            " 15 or less characters.");
                    return false;
                }
                break;
            case 2:
                if (answer.length() > 15 || this.isAlpha(answer) == false || this.isEmptyOrNull(answer)){
                    System.out.println("Incorrect format for surname - please ensure name contains no numerals and is" +
                            " 15 or less characters.");
                    return false;
                }
                break;
            case 3:
                if (!answer.equals("Male") || !answer.equals("Female")){
                    System.out.println("Incorrect format for gender - please ensure (Male/Female)");
                    return false;
                }
                break;
            case 4:
//                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = simpleDate.parse(answer);
//                String strDate = date.toString();
                //if ()
        }
        return true;
    }

    public boolean isAlpha(String word) {
        return word.matches("[a-zA-Z]+");
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
