// CircularVector.java
// Lucas Shin
// Implements a vector that holds objects
// CS 201 HW 3 problem 1

import structure.*;

public class CircularVector implements Circular
{
    protected Vector list;
    protected int current;

    // creates a new vector that can hold objects and sets the current value
    // to 0
    public CircularVector() {
        this.list = new Vector();
        this.current = 0;
    }

    // this method moves the current element to refer to the next element
    public void next() {
        if (this.current == list.size() - 1) {
            this.current = 0;
        } else {
            this.current++;
        }
    }

    // returns the number of elts in object (0 if empty)
    public int size() {
        return list.size();
    }

    // this method returns the current element
    public Object getCurrent() {
        return list.get(this.current);
    }

    // This method removes the current element. If the element is the last
    // in the vector, sets the current element to the first element
    public void removeCurrent() {

        if(this.current == list.size() - 1){
            list.remove(this.current);
            this.current = 0;
        } else {
            list.remove(this.current);
        }
    }

    // This method adds a new obj after the current element.  If the vector is
    // empty, obj is inserted as only elt
    public void addAfterCurrent(Object obj) {
        if (list.get(this.current) == null) {
            list.add(this.current, obj);
        } else {
            this.current ++;
            list.add(this.current, obj);
        }
    }

    // this method removes all elements from the vector
    public void clear() {
        list.removeAllElements();
        this.current = 0;
    }

    // useful shorthand for System.out.println to save some typing
    public static void p(String s) {
        System.out.println(s);
    }

    // use a main method like the following to test your implementation:
    public static void main(String[] args) {
        p("creating new circular vector");
        Circular myList = new CircularVector();
        p("size is " + myList.size());
        p("adding 4");
        myList.addAfterCurrent(4);
        myList.next();
        p("adding 5");
        myList.addAfterCurrent(5);
        p("adding 6");
        p("clearing current vector");
        myList.clear();
        p("size is " + myList.size());
        myList.addAfterCurrent(6);
        p("adding 7");
        myList.addAfterCurrent(7);
        p("adding 8");
        myList.addAfterCurrent(8);
        p("calling next");
        myList.next();
        p("current is now " + myList.getCurrent());
        p("calling next");
        myList.next();
        p("current is now " + myList.getCurrent());
        p("calling next");
        myList.next();
        p("current is now " + myList.getCurrent());
        p("calling next");
        myList.next();
        p("current is now " + myList.getCurrent());
        p("calling next");
        myList.next();
        p("current is now " + myList.getCurrent());
        p("size is " + myList.size());
        p("removing current");
        myList.removeCurrent();
        p("size is " + myList.size());
        p("current is now " + myList.getCurrent());
    }
}
