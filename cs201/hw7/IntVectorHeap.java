// Complete heap of integers, implemented using Vector
//
// based on Bailey's "VectorHeap"
//
// CS 201 HW 7
//
// Lucas Shin

import java.util.*;     // uses Java's Vectors so that browser can find them

public class IntVectorHeap implements IntHeap {

    // the values in the heap, stored in level-order
    protected Vector<Integer> data;
    
    // index of "current" root, so that left() and right() can be implemented
    protected int root;

    // NOTE: add() and remove() will only work on the original heap
    // (i.e., if root==0).  Their behavior on the "subtrees" return by
    // left() or right() is unspecified.

    // constructor
    public IntVectorHeap() {
        data = new Vector<Integer>();
        root = 0;
    }

    // private constructor (used by left() and right())
    protected IntVectorHeap(Vector<Integer> d, int r) {
        data = d;
        root = r;
    }


    // returns true iff heap has no values
    public boolean isEmpty() {
        return root >= data.size();
    }

    // returns the minimum int at the root of the heap ( = Bailey's getFirst())
    // pre: !isEmpty()
    public int value() {
        return data.get(root);
    }

    /************************************************************/

    // Moves node up into its appropriate position in the heap
    protected void percolateUp(int leaf) {
        int parent = parent(leaf);
        int value = data.get(leaf);
        while (leaf > 0 && (value < data.get(parent))) {
            data.set(leaf, data.get(parent));
            leaf = parent;
            parent = parent(leaf);
        }
        data.set(leaf, value);
    }
    
    // Moves the node down and into its appropriate position 
    // within the subheap.
    protected void pushDownRoot(int root) {
        int heapSize = data.size();
        int value = data.get(root);
        while (root < heapSize) {
            int childpos = left(root);
            if (childpos < heapSize) {
                if ((right(root) < heapSize) &&
                  (data.get(childpos+1) < data.get(childpos))) {
                    childpos++;
                
                // Assert: childpos indexes smaller of two children
                } if ((data.get(childpos)) < value) {
                    data.set(root, data.get(childpos));
                    root = childpos; // keep moving down
                } else { // found right location
                    data.set(root, value);
                    return;
                }
            } else { // at a leaf! insert and halt
                data.set(root, value);
                return;
            }       
        }
    }
    
    // Gets the lowest value item from the queue.
    public int getFirst() {
        return data.get(0);
    }

    // Adds value to the heap and calls percolateUp() to put
    // it in the correct spot.
    public void add(int value) {
    	 data.add(value);
         percolateUp(data.size() - 1);
    }

    // Removes and returns the minimum integer at the root 
    // of the heap and calls pushDownRoot() to move every
    // other value into its correct position.
    public int remove() {
    	int minVal = getFirst();
        data.set(0, data.get(data.size() - 1));
        data.setSize(data.size() - 1);
        if (data.size() > 1) pushDownRoot(0);
        return minVal;
    }
    
    /************************************************************/

    // removes all elements from the heap
    public void clear() {
        data = new Vector<Integer>();
        root = 0;
    } 

    // returns left "subtree" of "current" root of heap
    public IntHeap left() {
        return new IntVectorHeap(data, left(root));
    }

    // returns right "subtree" of "current" root of heap
    public IntHeap right() {
        return new IntVectorHeap(data, right(root));
    }


    // code adapted from Bailey's "VectorHeap" below ---------------
    // (assumes root is at index 0!)
    
    // returns index of parent of value at i
    protected static int parent(int i) {
        return (i-1)/2;
    }

    // returns index of left child of value at i
    protected static int left(int i) {
        return 2*i+1;
    }

    // returns index of right child of value at i
    protected static int right(int i) {
        return 2*(i+1);
    }
    
}
