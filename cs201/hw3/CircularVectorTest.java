// CircularVectorTest.java
//
// test program for CS 201, HW 3 part 1

public class CircularVectorTest
{
    // shorthand for println
    public static void p(String s) {
        System.out.println(s);
    }

    // test function, prints s and an int object
    // prints warning if the int object differs from expected
    public static void p2(String s, Object obj, int expected) {
        int x = (Integer)obj;
        if (x == expected)
            p(s + x);
        else
            p(s + x + " *** " + expected + " expected");
    }

    // main method for testing
    public static void main(String[] args) {
        Circular myList = new CircularVector();
        p2("size = ", myList.size(), 0);
        p("adding 7, 8, 9");
        myList.addAfterCurrent(new Integer(7));
        myList.addAfterCurrent(new Integer(8));
        myList.addAfterCurrent(new Integer(9));
        p2("current = ", myList.getCurrent(), 9);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 7);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 8);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 9);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 7);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 8);
        myList.removeCurrent();
        p2("removing, current = ", myList.getCurrent(), 9);
        myList.removeCurrent();
        p2("removing, current = ", myList.getCurrent(), 7);
        p2("size = ", myList.size(), 1);
        myList.removeCurrent();
        p("removing");
        p2("size = ", myList.size(), 0);
        p("adding 5");
        myList.addAfterCurrent(new Integer(5));
        p2("current = ", myList.getCurrent(), 5);
        myList.next();
        p2("next, current = ", myList.getCurrent(), 5);
        myList.clear();
        p2("clearing, size = ", myList.size(), 0);
    }
}
