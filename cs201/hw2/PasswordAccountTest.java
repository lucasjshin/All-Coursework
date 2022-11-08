// PasswordAccountTest.java
// Tests PasswordAccount class
// CS 201 HW 2 problem 4

public class PasswordAccountTest {

    public static void main(String[] args) {

        PasswordAccount jane = new PasswordAccount(135, "apple", 2000);
        PasswordAccount john = new PasswordAccount(277, "pear", 500);
        p("*** Created two new accounts");
        p("jane=" + jane);
        p("john=" + john);

        p("*** depositing $50 to jane");
        jane.deposit(50);

        p("*** trying to deposit -$80 to jane");
        jane.deposit(-80);

        p("*** trying to print jane's balance, password 'aapl'");
        jane.printBalance("aapl");

        p("*** trying to print jane's balance, password 'apple'");
        jane.printBalance("apple");

        p("*** trying to withdraw $200 from john, password 'pare'");
        john.withdraw(200, "pare");

        p("*** trying to withdraw $200 from john, password 'pear'");
        john.withdraw(200, "pear");

        p("*** trying to withdraw $500 from john, password 'pear'");
        john.withdraw(500, "pear");

        p("*** trying to withdraw $300 from john, password 'pear'");
        john.withdraw(300, "pear");

        p("*** depositing $2000 to john");
        john.deposit(2000);

        // this should also work (make sure you use 'equals' when comparing Strings!)
        String x = "PEAR".toLowerCase();
        p("*** trying to print john's balance, password '" + x + "'");
        john.printBalance(x);
    }

    // shorthand for System.out.println
    public static void p(String s) {
        System.out.println(s);
    }

}
