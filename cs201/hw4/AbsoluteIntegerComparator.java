// AbsoluteIntegerComparator.java
//
// Sample Comparator class
// compares two Integers based on their absolute values

public class AbsoluteIntegerComparator implements java.util.Comparator<Integer>
{
    public int compare(Integer a, Integer b)
    // pre: a and b are valid Integers
    // post: returns a value less than, equal to, or greater than 0
    //       if |a| is less than, equal to, or greater than |b|
    {
        Integer aa = Math.abs(a);
        Integer ab = Math.abs(b);
        return aa.compareTo(ab);

        // OR in one line:
        // return ((Integer)Math.abs(a)).compareTo((Integer)Math.abs(b));
    }
}
