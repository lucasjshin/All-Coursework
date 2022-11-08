// Josephus.java
// Lucas Shin
// Runs a simulation of the Josephus problem
// CS 201 HW 3 problem 2

public class Josephus {

    protected int numToSkip;
    protected CircularVector jCircle;

    // Initializes numPeople and numToSkip at integers depending on the command-
    // line input
    public Josephus(int numPeople, int numToSkip) {
        this.numToSkip = numToSkip;
        this.jCircle = new CircularVector();
        for (int i = 1 ; i <= numPeople; i++) {
            jCircle.addAfterCurrent(new Integer(i));
        }
        jCircle.next();
    }

    // this method skips numToSkip people and removes the element after the one
    // removed. Repeats the process until there is one person remaining.  Last,
    // prints the # corresponding to the single remaining messenger
    public void findMessenger(){
        while (jCircle.size() != 1){
            for (int i = 0 ; i < this.numToSkip; i++) {
                p("Skip " + jCircle.getCurrent());
                jCircle.next();
            }
            p("Remove " + jCircle.getCurrent());
            jCircle.removeCurrent();
        }
        p("The messenger is " + jCircle.getCurrent());
    }

    // useful shorthand for System.out.println to save some typing
    public static void p(String s) {
        System.out.println(s);
    }

    // Takes the command line input and changes the two given str int's into
    // object Integers. Then runs findMessenger.
    public static void main(String[] args) {
        int num1 = Integer.parseInt(args[0]);
        int num2 = Integer.parseInt(args[1]);
        // if there are not two inputs provided, prints a usage statement
        if (args.length != 2) {
            p("usage: Josephus <numPeople> <numToSkip>");
        } else {
            Josephus pinCircle = new Josephus(num1, num2);
            p("Solving Hosephus problem with " + num1 + " people," +
                " skipping " + num2);
            pinCircle.findMessenger();
        }
    }
}
