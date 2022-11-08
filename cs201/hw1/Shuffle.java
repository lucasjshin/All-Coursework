// Shuffle.java
// Will Bogatyrenko and Lucas Shin
// Skeleton for CS 201  HW 1, Problem 3

public class Shuffle {


    public static void main(String[] args) {
        printShufflePeriods(2,100);
    }

    // Prints even shuffle periods between lo and hi.
    public static void printShufflePeriods(int lo, int hi) {

        // Assume lo and hi are even;
        for(int i = lo; i <= hi; i += 2){
          System.out.println("shufflePeriod(" + i + ") = " + shufflePeriod(i));
        }
    }

    // Computes shuffle period for a deck of size L.
    // (The shuffle period is the smallest number of perfect shuffles
    // necessary to bring the deck back into its originial order.)
    public static int shufflePeriod(int L) {
        // Assume L is even.
        int period = 1;
        int[] arr = perfectShuffle(identity(L));
        while(!isIdentity(arr)){
          arr = perfectShuffle(arr);
          period++;
        }
        return period;
    }

    // Performs a perfect shuffle.
    // Returns a new array that's a perfect shuffle of A
    public static int[] perfectShuffle(int[] A) {

        // Assume A has even length
        int[] firstHalf = new int[A.length/2];
        int[] secondHalf = new int[A.length/2];
        for(int i = 0; i < firstHalf.length; i++){
          firstHalf[i] = A[i];
        }
          for(int i = 0; i < secondHalf.length; i++){
            secondHalf[i] = A[i + firstHalf.length];
          }
        // allocate result array
        int[] result = new int[A.length];

        int j = 0;
        for (int i = 0; i < result.length - 1; i += 2){
          result[i] = firstHalf[j];
          result[i + 1] = secondHalf[j];
          j++;
        }
        return result;
    }

    // Returns an identity array of length L
    public static int[] identity(int L) {
        int [] LArray = new int [L];
        for(int i = 0; i < L; i++){
          LArray[i] = i;
        }
        return LArray;
        // create new array of length L
        // fill it with numbers 0 .. L-1
        // return it
    }

    // Returns whether array A is the identity array.
    public static boolean isIdentity(int[] A) {
        boolean isIt = true;
        int i = 0;
        while(isIt == true && i < A.length){
          if (A[i] != i){
            isIt = false;
          }
          else{
            i++;
          }
        }
        return isIt;
    }

    // Creates a string representation of integer array A.
    // (useful for debugging)
    public static String intArrayToString(int[] a) {
        String s = "[";
        if (a.length > 0) {
            s += a[0];
            for (int i = 1; i < a.length; i++) {
                s += (", " + a[i]);
            }
        }
        s += "]";
        return s;
    }

}
