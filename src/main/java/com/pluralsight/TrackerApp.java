
package com.pluralsight;

import java.util.Locale;
import java.util.Scanner;



public class TrackerApp {
    static Scanner scanner = new Scanner(System.in);



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
                    //DisplayProduct();
                    break;
                case "P":
                    System.out.println("Make Payment");
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
                case "D":
                    System.out.println("Deposits");
                    break;
                case "P":
                    System.out.println("Payments.");
                    running = false;
                    break;
                case "R":
                    System.out.println("Reports.");
                    running = false;
                    break;
                case "H":
                    running = false;
                    mainMenu();
                    break;
                default:
                    System.err.println(("Oops! That wasn't a valid option."));
                    break;
            }
        } while (running);



    }


}
