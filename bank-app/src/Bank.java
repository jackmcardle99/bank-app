import javax.sql.DataSource;
import java.util.Scanner;
import java.sql.*;
public class Bank {
    Customer cus1 = new Customer(01,"Mr", "Jack", "McArdle", "Male");
    Scanner scan = new Scanner(System.in);




    public static void main(String args[]){
        Bank app = new Bank();
        app.logIn();
    }

    private void logIn(){
        while (true) {
            System.out.println("(1) Log in\n(2) Apply for Account\n(3) Exit");
            String input = scan.nextLine();
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Please enter valid input, either 1 or 2.");
            }
            if (input.equals("2")){
                this.createAccount();
            }
            if (input.equals("3")) {
                System.out.println("Goodbye!");
            } else {
                this.menu();
            }
            break;
        }
    }

    private void createAccount(){

        try{
          //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("#DataSourceSettings#\n" +
                    "#LocalDataSource: bank_db\n" +
                    "#BEGIN#\n" +
                    "<data-source source=\"LOCAL\" name=\"bank_db\" uuid=\"d8045415-4f31-47f6-afdd-bdc92689693d\"><database-info product=\"MySQL\" version=\"8.0.29\" jdbc-version=\"4.2\" driver-name=\"MySQL Connector/J\" driver-version=\"mysql-connector-java-8.0.25 (Revision: 08be9e9b4cba6aa115f9b27b215887af40b159e0)\" dbms=\"MYSQL\" exact-version=\"8.0.29\" exact-driver-version=\"8.0\"><extra-name-characters>#@</extra-name-characters><identifier-quote-string>`</identifier-quote-string></database-info><case-sensitivity plain-identifiers=\"mixed\" quoted-identifiers=\"mixed\"/><driver-ref>mysql.8</driver-ref><synchronize>true</synchronize><jdbc-driver>com.mysql.cj.jdbc.Driver</jdbc-driver><jdbc-url>jdbc:mysql://localhost:3306/bank_db</jdbc-url><secret-storage>master_key</secret-storage><user-name>root</user-name><schema-mapping><introspection-scope><node kind=\"schema\" qname=\"@\"/></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>\n" +
                    "#END#\n" +
                    "\n");
        }
        catch (Exception e ){
            System.out.println(e);
        }

        System.out.println("Please enter your username");
        String username = scan.nextLine();
        System.out.println("Please enter your password");
        String password = scan.nextLine();
    }

    private void menu(){
        System.out.println("==================== Welcome to your Online Banking " + cus1.getPrefix() + " " + cus1.getSurname() +
                " =========================\n" +
                "Lists of accounts");
        Account acc1 = new Account("123","pro",01,200);
        System.out.println();
    }
}
