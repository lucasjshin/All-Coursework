// Interface for heaps of integers
//
// CS 201 HW 7

public interface IntHeap {

    // returns true iff heap has no values
    public boolean isEmpty();

    // adds value to heap
    public void add(int value);

    // removes and returns the minimum int at the root of the heap
    // pre: !isEmpty()
    public int remove();

    // returns the minimum int at the root of the heap ( = Bailey's getFirst())
    // pre: !isEmpty()
    public int value();

    // removes all elements from the heap
    public void clear();

    // The following two methods left() and right() are provided to
    // enable printing of the heap.  They are not part of a
    // "traditional" heap interface, which should hide the actual heap
    // structure.  Only the methods isEmpty(), value(), left(), and
    // right() should be used on sub-heaps returned by left() and
    // right().  The methods add() and remove() should NOT be used on
    // sub-heaps!

    // returns left "sub-heap" of heap
    // pre: !isEmpty()
    public IntHeap left();

    // returns right "sub-heap" of heap
    // pre: !isEmpty()
    public IntHeap right();

}
