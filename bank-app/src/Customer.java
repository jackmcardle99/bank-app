import org.w3c.dom.css.CSSUnknownRule;

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
}
