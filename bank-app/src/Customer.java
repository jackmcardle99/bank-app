import java.lang.reflect.Array;
import java.sql.*;
import java.time.*;

public class Customer {

    private Database db = new Database();
    private int custID;
    private String prefix,forename,surname,gender, dob;
    public Customer(int custID, String prefix, String forename, String surname, String gender, String dob){
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

    public boolean validateDob(String strDay, String strMonth, String strYear){
            int day = Integer.parseInt(strDay);
            int month = Integer.parseInt(strMonth);  // 1-12 for January-December.
            int year = Integer.parseInt(strYear);
            LocalDate now = LocalDate.now();//setting date to now
            int latestYear = now.getYear() - 18;//minimum age for applicant

            while(true){
                if (day < 1 || day > 31){
                    System.out.println("Incorrect format for day of birth - please ensure 01-31");
                }
                if (month < 1 || month > 12){
                    System.out.println("Incorrect format for month of birth - please ensure 01-12");
                }
                if (year > latestYear || year < 1900){
                    System.out.println("Incorrect format for year of birth - please ensure you are over 18 and enter a year" +
                            " after 1900.");
                }
                else{
                    break; //break loop so we can continue to return true
                }
                return false; //if all the conditions above are met, return false, do not continue
            }





        return true;//return true if age passes
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
                if (!answer.equals("Male") && !answer.equals("Female")){
                    System.out.println("Incorrect format for gender - please ensure (Male/Female)");
                    return false;
                }
                break;
            case 4:
                if (answer.length() > 15 || this.isEmptyOrNull(answer)){
                    System.out.println("Incorrect format for username - please ensure username is 15 or less " +
                            "characters.");
                    return false;
                }
            case 5:
                if (answer.length() > 15 || this.isEmptyOrNull(answer)){
                    System.out.println("Incorrect format for password - please ensure password is 15 or less " +
                            "characters.");
                    return false;
                }
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
    public void createCustomerProfile(Connection conn, String[] answersArr, String strDob) throws SQLException {
        //Inserting new customer into customer table
        int newID = this.getNewID(db.dbConnect());
        PreparedStatement custStatement = conn.prepareStatement("INSERT INTO customers(custID,prefix, forename, surname, " +
                "gender, dob) VALUES (?,?,?,?,?,?);");
        custStatement.setInt(1,newID);
        custStatement.setString(2, (String) Array.get(answersArr,0));
        custStatement.setString(3,(String) Array.get(answersArr,1));
        custStatement.setString(4,(String) Array.get(answersArr,2));
        custStatement.setString(5,(String) Array.get(answersArr,3));
        custStatement.setString(6, strDob);
        custStatement.executeUpdate();

        //inserting customer details into credential tables
        PreparedStatement credStatement = conn.prepareStatement("INSERT INTO credentials (username,password,custID)" +
                "VALUES (?,?,?)");
        credStatement.setString(1,(String) Array.get(answersArr,4));
        credStatement.setString(2,(String) Array.get(answersArr,5));
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
                dob = rs.getString("dob");
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
