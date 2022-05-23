import java.sql.Connection;
import java.sql.DriverManager;

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

}
