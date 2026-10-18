import java.util.Scanner;

public class Calculator1 {
   public static void main(String [] args){
       Scanner scanner = new Scanner(System.in);

       System.out.println("Enter the principal loan amount");
       double Principal = scanner.nextDouble();

       System.out.println("Enter the annual interest rate");
       double AnnualInterestPercent = scanner.nextDouble();

       System.out.println("Enter the loan term in years");
       int LoanYears = scanner.nextInt();

       double AnnualInterestRate = AnnualInterestPercent / 100.0;
       double MonthlyInteresteRate = AnnualInterestRate / 12.0;
       int TotalMonthlyPayements = 12 * LoanYears;


       Math math;
       double BaseValue = 1 + MonthlyInteresteRate;
       double Numerator = MonthlyInteresteRate * Math.pow(BaseValue, TotalMonthlyPayements);
       double Denominator = Math.pow( BaseValue ,TotalMonthlyPayements) - 1;
       double MonthlyPayment = Principal * (Numerator / Denominator);

       double TotalInterest = (MonthlyPayment * TotalMonthlyPayements) - Principal;

       System.out.println("\n--- Loan Summary ---");
       System.out.printf("Monthly Payment: $%s%n", String.format("%.2f", MonthlyPayment));
       System.out.printf("Total Interest Paid: $%.2f%n", TotalInterest);

       scanner.close();













   }





    
}
