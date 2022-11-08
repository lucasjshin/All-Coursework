// Skew heap of integers, implemented using IntTree
//
// based on Bailey's "SkewHeap"
//
// CS 201 HW 7
//
// Lucas Shin

public class IntSkewHeap implements IntHeap {

	protected IntTree t;        // the heap

	// constructor
	public IntSkewHeap() {
		t = empty();
	}

	// private constructor (used by left() and right())
	protected IntSkewHeap(IntTree tree) {
		t = tree;
	}


	// returns true iff heap has no values
	public boolean isEmpty() {
		return isEmpty(t);
	}

	// returns the minimum int at the root of the heap ( = Bailey's getFirst())
	public int value() {
		return t.value();
	}

	// removes all elements from the heap
	public void clear() {
		t = empty();
	}

	// returns left "sub-heap" of heap
	public IntHeap left() {
		return new IntSkewHeap(t.left());
	}

	// returns right "sub-heap" of heap
	public IntHeap right() {
		return new IntSkewHeap(t.right());
	}


	/************************************************************/

	protected static <E extends Comparable<E>>

	// Merges the left and right IntTrees and makes sure 
	// all values are in their correct positions
	IntTree merge(IntTree left, IntTree right) {
		if (isEmpty(left)) return right;
		if (isEmpty(right)) return left;
		int leftVal = left.value();
		int rightVal = right.value();
		IntTree result;
		if (rightVal < leftVal) {
			result = merge(right,left);
		} else {
			result = left;
			// assertion left side is smaller than right
			// left is new root
			if (isEmpty(result)) {
				result.setLeft(right);
			} else {
				IntTree temp = result.right();
				result.setRight(result.left());
				result.setLeft(merge(temp,right));
			}
		}
		return result;
	}
	
	// adds value to heap
	public void add(int value) {
		IntTree smallTree = new IntTree(value, empty(), empty());
		t = merge(smallTree,t);
	}

	// removes and returns the minimum int at the root of the heap
	public int remove() {
		int result = t.value();
		t = merge(t.left(),t.right());
		return result;  
	}



	/************************************************************/

	// Local abbreviations for static IntList methods

	public static IntTree empty() {
		return IntTree.empty();
	}

	public static boolean isEmpty(IntTree T) {
		return IntTree.isEmpty(T);
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
