// Lucas Shin
// CS 201 HW 4 problem 2
// An implementation of the Vector class that provides 3 sorting mechanisms: selection
// sort, insertion sort, and quick sort.

import structure5.Vector;
import java.util.Comparator;

public class MyVector<T> extends Vector<T> {

	// Constructs a new empty MyVector
	public MyVector() {
		super(); // calls constructor of super class
	}

	// Constructs a new MyVector with initial capacity n
	public MyVector(int n) {
		super(n); // calls constructor of super class
	}

	// Implements the selection sort algorithm 
	public void selectionSort(Comparator<T> c) {
		for (int numUnsorted = this.size(); numUnsorted > 0; numUnsorted--) {
			// determines location of maximum value in array
			int maxIndex = 0;
			for (int index = 1; index < numUnsorted; index++) {
				if (c.compare(this.get(maxIndex), this.get(index)) < 0)
					maxIndex = index;
			}
			swap(maxIndex, numUnsorted - 1);
		}
	}

	// Switches the positions of the objects i and j in the vector
	public void swap(int i, int j) {
		T temp = this.get(i);
		this.set(i, this.get(j));
		this.set(j, temp);
	}

	// Implements the insertion sort algorithm
	public void insertionSort(Comparator<T> c) {
		for (int numSorted = 1; numSorted < this.size(); numSorted++) {
			int index; // need to declare here since it's used outside of loop
			// gets the first unsorted value ...
			T val = this.get(numSorted);
			// ... and inserts it among the sorted
			for (index = numSorted; index > 0; index--) {
				if (c.compare(val, this.get(index - 1)) < 0) {
					this.set(index, this.get(index-1));
				} else {
					break; // quits out of smallest enclosing loop
				}
			}
			// reinserts value
			this.set(index, val);
		}
	}

	// Implements the quick sort algorithm
	public void quickSort(Comparator<T> c) {
		// calls the recursive part of quick sort
		quickSortRecursive(0, this.size() - 1,c);
	}
	
	// Recursively sorts using the quick sort algorithm
	public void quickSortRecursive(int low, int high, Comparator<T> c) {
		int pivot;
		if (low < high) {
			pivot = partition(low, high,c);
			quickSortRecursive(low, pivot - 1,c);
			quickSortRecursive(pivot + 1, high,c);
		}
	}

	// Creates partitions within the vector and moves the pivot.
	// Initializes the comparator c.
	public int partition(int left, int right,Comparator<T> c) {
		while (true) {
			// moves the right "pointer" toward left
			while ((left < right) && (c.compare(this.get(left), this.get(right)) < 0))
				right--;
			if (left < right) {
				swap(left++, right);
			} else {
				return left;
			}
			// moves the left "pointer" toward right
			while ((left < right) && (c.compare(this.get(left), this.get(right)) < 0)) 
				left++;
			if (left < right) {
				swap(left, right--);
			} else {
				return right;
			}
		}
	}
}