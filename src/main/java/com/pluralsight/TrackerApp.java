
package com.pluralsight;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;



public class TrackerApp {
    static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/products.csv";


    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you, come back soon!");



    }




    private static void mainMenu() {
        String prompt =
                """
                        ========================================
                                WELCOME TO ONLINE STORE
                        ========================================
                        
                        Please choose an option:
                        
                        D) Add Deposit
                        P) Make Payment(Debit)
                        L) Ledger
                        X) Exit
                        
                        Enter your choice:\s""";

        boolean running = true;



        do {
            System.out.println(prompt);
            String userMenu = scanner.nextLine();

            switch (userMenu) {
                case "D":
                    //addDeposit();
                    break;
                case "P":
                    System.out.println("Make Payment");
                    //makePayment();
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Oops! That wasn't a valid option.");
                    break;
            }
        } while (running);



    }

    private static void ledgerMenu() {
        String prompt =
                """
                         ========================================
                                         Ledger
                         ========================================
                        \s
                         Please choose an option:
                        \s
                         A) All
                         D) Deposits
                         P) Payments
                         R) Reports
                         H) Home
                        \s
                         Enter your choice:\s""";

        boolean running = true;


        do {
            System.out.println(prompt);
            String userMenu = scanner.nextLine();

            switch (userMenu) {
                case "A":
                    //DisplayProduct();
                    break;
                case "D": //deposits
                    //depositTransaction();
                    break;
                case "P": //payements
                    break;
                case "R":
                    showReportMenu();
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.err.println(("Oops! That wasn't a valid option."));
                    break;
            }
        } while (running);

    }

        private static void showReportMenu() {
            String prompt =
                    """
                            ========================================
                                            Reports
                            ========================================
                           \s
                            Please choose an option:
                           \s
                            1) Month to Date
                            2) Previous Month
                            3) Year To Year
                            4) Previous Year
                            5) Search by Vendor
                            0) Back
                           \s
                            Enter your choice:\s""";

            boolean running = true;



            do {
                System.out.println(prompt);
                String userMenu = scanner.nextLine();

                switch (userMenu) {
                    case "1":
                        //MonthtoMonth
                        //monthToMonth();
                        break;
                    case "2":
                        //previousMonth();
                        break;
                    case "3":
                        //yearToYear();
                        break;
                    case "4": //previous year
                        //showReports();
                        break;
                    case "5":
                        //searchByVendor();
                        break;
                    case "0": //Go back to ledger menu
                        running = false;;
                    default:
                        System.err.println(("Oops! That wasn't a valid option."));
                        break;
                }
            } while (running);



    }


}
