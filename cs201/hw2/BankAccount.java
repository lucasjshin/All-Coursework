// BankAccount.java
// Defines BankAccount objects that have individual IDs and balances
// CS 201 HW 2 problem 4

public class BankAccount {

    // instance variables

    protected int id;
    protected double balance;


    // constructors

    public BankAccount(int id, double balance) {
        // We use this.id to denote the instance variable named id rather
        // than the parameter named id.  When no local variable has the
        // same name as an instance variable, using "this" is optional
        this.id = id;
        this.balance = balance;
    }

    public BankAccount(int id) {
        // we call the other constructor with 0 as the initial balance
        this(id, 0);
    }


    // instance methods

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + ".");
        } else {
            System.err.println("You must deposit a positive amount of money.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew $" + amount + ".");
        } else {
            System.err.println("Sorry, you must withdraw a positive amount" +
                               " of money up to $" + balance + ".");
        }
    }

    public void printBalance() {
        System.out.println("Your balance is $" + balance + ".");
    }

    public String toString() {
        return ("Account #" + id);
    }

}
