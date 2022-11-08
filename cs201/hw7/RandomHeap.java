// Random "heap" of integers, implemented using IntTree
//
// use this to test your tree drawing method before you write
// IntVectorHeap and IntSkewHeap
//
// CS 201 HW 7

import java.util.*; // for Random

public class RandomHeap implements IntHeap {

    protected Random rand = new Random(); 
    protected IntTree t;

    // constructor
    public RandomHeap() {
        t = empty();
    }

    // private constructor (used by left() and right())
    protected RandomHeap(IntTree tree) {
        t = tree;
    }

    // returns true iff heap has no values
    public boolean isEmpty() {
        return isEmpty(t);
    }

    // adds value as a new leaf to heap at random location
    public void add(int value) {
        IntTree newleaf = leaf(value);
        if (isEmpty(t)) {
            t = newleaf;
        } else {
            IntTree finger = t;
            IntTree next = t;
            boolean lr = false;
            while(!isEmpty(next)) {
                finger = next;
                lr = (rand.nextInt() % 2) == 0;
                next = (lr ? left(finger) : right(finger));
            }
            if (lr)
                setLeft(finger, newleaf);
            else
                setRight(finger, newleaf);
        }
    }

    // removes a random leaf of the tree and returns its value
    // pre: !isEmpty()
    public int remove() {
        int x;
        if (isLeaf(t)) {
            x = value(t);
            t = empty();
        } else {
            IntTree finger = t;
            IntTree next = t;
            boolean lr = false;
            while(!isLeaf(next)) {
                finger = next;
                if (isEmpty(left(finger))) lr = false;
                else if (isEmpty(right(finger))) lr = true;
                else lr = (rand.nextInt() % 2) == 0;
                next = (lr ? left(finger) : right(finger));
            }
            x = value(next);
            if (lr)
                setLeft(finger, empty());
            else
                setRight(finger, empty());
        }
        return x;
    }

    // returns the int at the root of the heap
    // pre: !isEmpty()
    public int value() {
        return t.value();
    }

    // removes all elements from the heap
    public void clear() {
        t = empty();
    }

    // returns left "sub-heap" of heap
    // pre: !isEmpty()
    public IntHeap left() {
        return new RandomHeap(t.left());
    }

    // returns right "sub-heap" of heap
    // pre: !isEmpty()
    public IntHeap right() {
        return new RandomHeap(t.right());
    }
        
    // Local abbreviations
        
    public static IntTree empty() {
        return IntTree.empty();
    }
        
    public static boolean isEmpty(IntTree T) {
        return IntTree.isEmpty(T);
    }
        
    public static boolean isLeaf(IntTree T) {
        return IntTree.isLeaf(T);
    }
        
    public static IntTree node(int val, IntTree lt, IntTree rt) {
        return IntTree.node(val, lt, rt);
    }
        
    public static IntTree leaf(int val) {
        return IntTree.leaf(val);
    }
        
    public static int value(IntTree T) {
        return IntTree.value(T);
    }
        
    public static IntTree left(IntTree T) {
        return IntTree.left(T);
    }
        
    public static IntTree right(IntTree T) {
        return IntTree.right(T);
    }
        
    public static void setValue(IntTree T, int newValue) {
        IntTree.setValue(T, newValue);
    }
        
    public static void setLeft(IntTree T, IntTree newLeft) {
        IntTree.setLeft(T, newLeft);
    }
        
    public static void setRight(IntTree T, IntTree newRight) {
        IntTree.setRight(T, newRight);
    }
}
