// BankAccountTest.java
// Tests BankAccount class
// CS 201 HW 2 problem 4

public class BankAccountTest {

    public static void main(String[] args) {

        BankAccount mary = new BankAccount (7, 100);
        p("*** Created new account with initial balance $100");
        p("mary=" + mary);

        p("*** depositing $50.25");
        mary.deposit(50.25);

        p("*** trying to withdraw $200");
        mary.withdraw(200);

        p("*** withdrawing $140");
        mary.withdraw(140);

        p("*** printing balance");
        mary.printBalance();

    }

    // shorthand for System.out.println
    public static void p(String s) {
        System.out.println(s);
    }

}
