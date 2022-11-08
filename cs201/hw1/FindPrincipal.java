// It's the Principal of the Thing
// CS 201 HW 1 problem 2
// Lucas Shin and William Bogatyrenko

public class FindPrincipal {


    //Defers to FindBetween to find principal between 0 and upper bound value
    //finds principal whose mortgage is within one dollar of thew mortgae parameter of findBetween
    public static double find(double mortgage, double interest,
    double years) {

        return findBetween(mortgage, interest, years,
        0, upperBound(mortgage, interest, years));
    }

    //uses binary search to find principal between 0 and upper bound values
    public static double findBetween(double mortgage, double interest,
    double years, double lo, double hi) {
        while(Math.abs(mortgage-Mortgage.mortgage((lo+hi)/2, interest, years))>1){
            if(mortgage>Mortgage.mortgage((lo+hi)/2, interest, years)){
                lo=(lo+hi)/2;
            }
            if(mortgage<Mortgage.mortgage((lo+hi)/2, interest, years)){
                hi=(lo+hi)/2;
            }
        }
        return (lo+hi)/2;

    }

    //determines uppoer bound principal that returns a mortgage greater
    //than then mortgage limit
    //Starting with principal 1, double it until reaching a principal whose
    //moregage is greater than the mortgage limit
    public static double upperBound(double mortgage, double interest,
    double years) {
        int p=1;
        double m= Mortgage.mortgage(p, interest, years);

        while(m < mortgage){
            p=p*2;
            m=Mortgage.mortgage(p, interest, years);

        }
        return p;
    }

    //uses the find method to find principal, then prints it
    public static void testFind(double m, double i, double y) {
        System.out.println("find(" + m + ", " + i + ", " + y + ") = "
        + find(m, i, y));
    }

    public static void main(String [] args) {
        testFind(1200.00, 7.00, 30.0);
    }

}
