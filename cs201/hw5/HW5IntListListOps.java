import sun.security.util.Length;

// CS 201 HW 5 Problem 2
// Lucas Shin

public class HW5IntListListOps extends IntListListOps {
	
	// Returns a list of all elements of L that are sorted lists.
    public static IntListList filterSorted (IntListList L) {
    	if (isEmpty(L)) {
    		return L;
    	} else {
    		if (HW5IntListOps.isSorted(head(L)) == true) {
    			return prepend(head(L), filterSorted(tail(L)));
    		} else {
    			return filterSorted(tail(L));
    		}
    	}
    }
    
    // Returns a list the same length as L in which each element
    // is the corresponding element of L with the integer i
    // prepended to the front.
    public static IntListList mapPrepend (int i, IntListList L) {
    	if (isEmpty(L)) {
    		return L;
    	} else {
    		return prepend(IntList.prepend(i, head(L)), mapPrepend(i, tail(L)));
    	}
    }
    
    // Returns a list of all subsequences of L.  Subsequences are
    // lists obtained from L by deleting zero or more elements 
    // and maintaining the relative order of undeleted elements.  
    public static IntListList subsequences (IntList L) {  
    	if (IntList.isEmpty(L)) {
    		return prepend(L, empty());
    	} else {
    		int sL = IntList.head(L);
    		IntListList iLL = subsequences(IntList.tail(L));
    		return append(iLL, mapPrepend(sL, iLL));
    	}
    } 

    // Returns a list containing all the IntLists that have the 
    // longest length in L.
    public static IntListList longest (IntListList L) {
    	if (isEmpty(L)) {
    		return L;
    	} 
    	if (isEmpty(tail(L)))
    			return new IntListList (head(L), IntListList.empty());
    	else {
    		if ((IntListOps.length(head(L)) > IntListOps.length(head(longest(tail(L)))))) {
    			return new IntListList (head(L), IntListList.empty());
    		} 
    		if(IntListOps.length(head(L))==IntListOps.length(head(longest(tail(L))))) {
    			return prepend(head(L),longest(tail(L)));
    		} else {
    			return longest(tail(L));
    		}
    	}
    }
}
