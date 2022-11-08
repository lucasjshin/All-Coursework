// CS 201 HW 5 Problem 1
// Lucas Shin

public class HW5IntListOps extends IntListOps {

	// Returns true if the integers in L are sorted from low to
	// high, and false if not sorted from low to high.
	public static boolean isSorted(IntList L) {
		if (isEmpty(L)) {
			return true;
		} else {
			if (isEmpty(tail(L))) {
				return true;
			} else if (head(tail(L)) < head(L)){
				return false;
			} else {
				return isSorted(tail(L));
			}
		}
	}
	
	// Returns a new list that contains all of the elements of L 
	// in the same order without integer i being included.
	public static IntList remove(int i, IntList L) {
		if (isEmpty(L)) {
			return L;
		} else {
			if (head(L) == i) {
				return remove(i, tail(L));
			} else {
				return prepend(head(L), remove(i, tail(L)));
			}
		}
	}

	// Returns a new list that contains only one occurrence of 
	// each integer that occurs in L (order does not matter).
	public static IntList removeDuplicates(IntList L) {
		if (isEmpty(L)) {
			return(L);
		} else {
			return prepend(head(L), removeDuplicates(remove(head(L), tail(L))));
		}
	}
} 

