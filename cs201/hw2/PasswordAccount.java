// PasswordAccount.java
// Implements a "secure" account that requires a password for both creating
// an account and also invoking the withdraw and printBalance methods
// CS 201 HW 2 problem 4

class PasswordAccount {

    protected int id;
    protected double balance;
    protected String password;

    // constructors

    public PasswordAccount(int id, String password, double balance) {
        // We use this.id to denote the instance variable named id rather
        // than the parameter named id.  When no local variable has the
        // same name as an instance variable, using "this" is optional.
        // We do the same for this.balance and this.password.
        this.id = id;
        this.balance = balance;
        this.password = password;
    }

    public PasswordAccount(int id, String password) {
        // we call the other constructor with 0 as the initial balance
        this(id, password, 0);
    }

    // instance methods

    public void deposit(double amount) {
        // This method allows the id to deposit an amount to their account
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + ".");
        } else {
            System.err.println("You must deposit a positive amount of money.");
        }
    }

    public void withdraw(double amount, String password) {
        // This method allows the id to withdraw a specific amount and subtracts
        // from their account balance.  Password required.
        if (password.equals(this.password)) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("Withdrew $" + amount + ".");
            } else {
                System.err.println("Sorry, you must withdraw a positive amount" +
                " of money up to $" + balance + ".");
            }
        } else {
            System.out.println("Sorry, incorrect password.");
        }
    }

    public void printBalance(String password) {
        //This method prints the total account balance for the instance of id.
        if (password.equals(this.password)) {
            System.out.println("Your balance is $" + balance + ".");
        } else {
            System.out.println("Sorry, incorrect password.");
        }
    }

    public String toString() {
        return ("Account #" + id);
    }
}
