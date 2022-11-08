// CaselessComparator.java
//
// Sample Comparator class
// compares two Strings without regards to capitalization
//
// adapted from Bailey's book


public class CaselessComparator implements java.util.Comparator<String>
{
    public int compare(String a, String b)
    // pre: a and b are valid Strings
    // post: returns a value less than, equal to, or greater than 0
    //       if a is less than, equal to, or greater than b, without
    //       consideration of case
    {
        String upperA = a.toUpperCase();
        String upperB = b.toUpperCase();
        return upperA.compareTo(upperB);

        // OR in one line:
        // return a.toUpperCase().compareTo(b.toUpperCase());
    }
}
