// ReverseIntegerComparator.java
//
// Sample Comparator class
// compares two Integers based on reverse order

public class ReverseIntegerComparator implements java.util.Comparator<Integer>
{
    public int compare(Integer a, Integer b)
    // pre: a and b are valid Integers
    // post: returns a value less than, equal to, or greater than 0
    //       if a is greater than, equal to, or less than b
    {
        return -a.compareTo(b);
        // OR: return b.compareTo(a);
    }
}
