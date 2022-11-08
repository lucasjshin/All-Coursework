// Mortgage.java
// CS 201 HW 1 problem 1
// Lucas Shin and William Bogatyrenko

public class Mortgage {

    //this method calculates and returns mortgage from the given equation taking the parameters principal, annual interest rate, and number of years
    public static double mortgage(double principal, double interest, double years) {
        double mortgage = (principal*(interest/1200))/(1-Math.pow((1/(1+(interest/1200))),12*years));
        return mortgage;
    }

    //this method takes principal, annual interest rate, and number of years and prints the values principal, interest, years, mortgage, and total
    public static void print(double principal, double interest, double years) {
        double mortgage= mortgage(principal, interest, years);
        double total = mortgage * 12 * years;
        System.out.printf("principal= %.0f; interest= %.2f; years= %.0f; mortgage= %.2f; total= %.0f", principal, interest, years, mortgage, total);
        System.out.println();
    }

    // this is the main method where the principal and years are defined
    public static void main (String[] args) {

        double principal = 250000;
        double years = 30;

        //runs the method print for every interest rate between 5% and 10% in increments of 0.25
        for(double interest=5; interest<=10; interest+=0.25){
            print(principal, interest, years);
        }

    }
}
