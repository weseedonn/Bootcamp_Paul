import java.util.Scanner;


public class Calculator2 {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the initial deposit amount");
        double Principal = scanner.nextDouble();

        System.out.println("Enter the annual interest rate");
        double AnnualInterestPercent = scanner.nextDouble();

        System.out.println("Enter the term in years");
        int LoanYears = scanner.nextInt();

        double AnnualInterestRate = AnnualInterestPercent / 100.0;
        int DaysPerYear = 365;
        int TotalDays = DaysPerYear * LoanYears;

        double BaseValue = 1 + (AnnualInterestRate / DaysPerYear);
        double FutureValue = Principal * Math.pow(BaseValue, TotalDays);

        double TotalInterest = FutureValue - Principal;

        System.out.println("\n--- CD Summary ---");
        System.out.printf("Future Value: $%.2f%n", FutureValue);
        System.out.printf("Total Interest Earned: $%.2f%n", TotalInterest);

        scanner.close();





    }
}
