// TestMyVector.java
// tests MyVector implementation for CS 201, HW 4 problem 2

import java.util.Comparator;
import java.util.Random;

public class TestMyVector {

    public static void main(String[] args) {
        MyVector<Integer> v1 = randomIntVector(15);
        testSorting(v1, new IntegerComparator());
        testSorting(v1, new ReverseIntegerComparator());
        testSorting(v1, new AbsoluteIntegerComparator());

        MyVector<String> v2 = unsortedStringVector();
        testSorting(v2, new StringComparator());
        testSorting(v2, new CaselessComparator());
    }

    // sorts v using three different sorting algorithms
    private static <T> void testSorting(MyVector<T> v, Comparator<T> c) {
        System.out.println("Original order: " + v);
        System.out.println("Sorting using " + c);
        
        MyVector<T> v2 = copyMyVector(v);
        v2.selectionSort(c);
        System.out.println("Selection Sort: " + v2);
        
        v2 = copyMyVector(v);
        v2.insertionSort(c);
        System.out.println("Insertion Sort: " + v2);
        
        v2 = copyMyVector(v);
        v2.quickSort(c);
        System.out.println("Quick Sort:     " + v2);

        System.out.println();
    }

    // returns vector containing some random integers between -99 and 99
    private static MyVector<Integer> randomIntVector(int size) {
        Random rand = new Random();
        MyVector<Integer> v = new MyVector<Integer>(size);
        for (int i = 0; i < size; i++) {
            v.add(rand.nextInt(199)-99);
        }
        return v;
    }

    // returns vector containing some random strings
    private static MyVector<String> unsortedStringVector() {
        MyVector<String> v = new MyVector<String>();
        v.add("A");
        v.add("b");
        v.add("c");
        v.add("-");
        v.add("die");
        v.add("Katze");
        v.add("lief");
        v.add("im");
        v.add("Schnee.");
        v.add("Und");
        v.add("als");
        v.add("sie");
        v.add("dann");
        v.add("...");
        return v;
    }

    // copies a vector
    private static <T> MyVector<T> copyMyVector(MyVector<T> v) {
        int n = v.size();
        MyVector<T> v2 = new MyVector<T>(n);
        for (int i = 0; i < n; i++)
            v2.add(v.get(i));
        
        return v2;
    }

}
