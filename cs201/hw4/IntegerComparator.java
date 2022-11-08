// IntegerComparator.java
//
// Sample Comparator class
// compares two Integers based on normal order

public class IntegerComparator implements java.util.Comparator<Integer>
{
    public int compare(Integer a, Integer b)
    // pre: a and b are valid Integers
    // post: returns a value less than, equal to, or greater than 0
    //       if a is less than, equal to, or greater than b
    {
        return a.compareTo(b);
    }
}
