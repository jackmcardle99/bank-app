# bank-app - what is it?

This program is my attempt at creating a banking application which allows the user to interact with it in various ways. It is written fully in Java and is intended to cement my understanding of object oriented programming. Data is stored in a mySQL database, and I also employ SQL statements and queries throughout the program, implemented as prepared statements to protect against SQL injections.

This application will likely be updated continuously as my knowledge of programming and application security progresses. 

## What are the aims?

### (1) - Allow the user to register a profile
The user must create a profile before opening any accounts. They are required to enter their personal details and create a username and password at the end.

### (2) - Allow the user to open an account
The user must be able to open one or more accounts with the bank. There are two accounts to choose from, a standard account and a professional account. Depending on the account chosen, a specific initial deposit amount is required, and the account type will affect fees.

### (3) - Allow the user to fund their account(s)
The user should be able to deposit money into their account whenever they would like.

### (4) - Allow the user to create/view/delete payee(s)
The user should have an option to create a payee on their account to be able to send money, and delete the payee when required. Whenever the user would like to view payees the application must provide a list.

### (5) - Allow the user to send and receive money
The application should ensure the user can send money to someone on their payee list, if the account number is wrong when sending then user should be made aware that the payment could not be processed. Likewise, the user should be able to receive money from other customers within the system.

### (6) - Allow the user to view their transaction list
When the user would like to view their incoming and outgoing transactions, it should give the user an option for last 30 days or all time and display a list accordingly.

### (7) - Allow the user to delete their account and profile
Finally, if the user decides that they no longer would like their accounts, or indeed their profile, they should have the option to delete either.

##### Note:
The database driver is included in the file structure, and the schema for the database in case it is needed. 

