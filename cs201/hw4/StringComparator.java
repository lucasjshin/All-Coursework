// StringComparator.java
//
// Sample Comparator class
// compares two Strings based on normal order

public class StringComparator implements java.util.Comparator<String>
{
    public int compare(String a, String b)
    // pre: a and b are valid Strings
    // post: returns a value less than, equal to, or greater than 0
    //       if a is less than, equal to, or greater than b
    {
        return a.compareTo(b);
    }
}
