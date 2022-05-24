import java.time.*;

public class Customer {

    /*private final Database db = new Database();*/
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
        if (word == null || test == true){
            return true;
        }
        return false;
    }

    //this method validates the input from user for dob
    public boolean validateDob(String strDay, String strMonth, String strYear){
            int day = Integer.parseInt(strDay);
            int month = Integer.parseInt(strMonth);  // 1-12 for January-December.
            int year = Integer.parseInt(strYear);
            LocalDate now = LocalDate.now();//setting date to now
            int latestYear = now.getYear() - 18;//minimum age for applicant

        //decision structure validates int inputs
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
               return true; //return true if age passes
            }
        return false; //return false if any of above conditions are met
    }

    // method validates user input for profile application
    public boolean validateApplication(String answer, int count) {
        switch (count){
            case 0: // switchcase covers all questions stored in array, cases correspond to count in array
                if (!answer.equals("Mr") && !answer.equals("Ms") && !answer.equals("Mrs")){
                    System.out.println("Incorrect format for prefix - please ensure (Mr/Ms/Mrs)");
                    return false;//returns false if any case is incorrect
                }
                break;
            case 1:
                if (answer.length() > 15 || !this.isAlpha(answer) || this.isEmptyOrNull(answer)){
                    System.out.println("Incorrect format for forename - please ensure name contains no numerals and is" +
                            " 15 or less characters.");
                    return false;
                }
                break;
            case 2:
                if (answer.length() > 15 || !this.isAlpha(answer) || this.isEmptyOrNull(answer)){
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
        return true; //method returns true if string validation passes
    }

    // method used to check if string is a-z only
    public boolean isAlpha(String word) {
        return word.matches("[a-zA-Z]+");
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
