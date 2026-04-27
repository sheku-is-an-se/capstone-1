
package com.pluralsight;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class TrackerApp {
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Transactions> transactions = new ArrayList<>();
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/products.csv";


    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you, come back soon!");


    }


    private static void mainMenu() {
        String prompt = """
╔══════════════════════════════════════════════╗
║            LEDGERPRO FINANCE HUB            ║
╠══════════════════════════════════════════════╣
║   Track deposits, payments, and history     ║
╚══════════════════════════════════════════════╝

             MAIN MENU
----------------------------------------------
[D] Add Deposit
[P] Make Payment (Debit)
[L] View Ledger
[X] Exit Application
----------------------------------------------
Enter your choice: 
""";

        boolean running = true;


        do {
            System.out.println(prompt);
            String userMenu = scanner.nextLine();

            switch (userMenu) {
                case "D":
                    addDeposit();
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

    private static void addDeposit() {
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;


        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME,true);
            bufferedWriter = new BufferedWriter(fileWriter);


            System.out.println("What's the description of the payment?");
            String payDesc = scanner.nextLine();

            System.out.println("Who's the vendor for this payment?");
            String payVend = scanner.nextLine();

            System.out.println("What's the amount?");
            Double payAmount = scanner.nextDouble();

            String trans  =  payDesc + "|" + payVend + "|" + payAmount;



            bufferedWriter.write(trans);

            bufferedWriter.close();




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
        private static Transactions parseProduct (String line){
            String[] parts = line.split("\\|");

            String date = parts[0];
            String time = parts[1];
            String description = parts[2];
            String vendor = parts[3];
            double amount = Double.parseDouble(parts[4]);

            return new Transactions(date, time, description, vendor, amount);
        }

     */



    private static void ledgerMenu() {
        String prompt = """
╔══════════════════════════════════════════════╗
║               LEDGER CENTER                 ║
╠══════════════════════════════════════════════╣
║      Review transactions and reports        ║
╚══════════════════════════════════════════════╝

                LEDGER MENU
------------------------------------------------
[A] View All Transactions
[D] View Deposits
[P] View Payments
[R] Open Reports
[H] Return Home
------------------------------------------------
Enter your choice:
""";

        boolean running = true;


        do {
            System.out.println(prompt);
            String userMenu = scanner.nextLine();

            switch (userMenu) {
                case "A":
                    //displayAll
                    break;
                case "D": //deposits
                    //depositTransaction();
                    break;
                case "P": //payements
                    //makePayments
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
        String prompt = """
╔══════════════════════════════════════════════╗
║              LEDGERPRO FINANCE HUB          ║
╠══════════════════════════════════════════════╣
║          Reporting and search tools         ║
╚══════════════════════════════════════════════╝

                REPORTS MENU
------------------------------------------------
[1] Month To Date
[2] Previous Month
[3] Year To Date
[4] Previous Year
[5] Search By Vendor
[0] Return to Ledger
------------------------------------------------
Enter your choice:
""";

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
                    //previousYear();
                    break;
                case "5":
                    //searchByVendor();
                    break;
                case "0": //Go back to ledger menu
                    running = false;
                    ;
                default:
                    System.err.println(("Oops! That wasn't a valid option."));
                    break;
            }
        } while (running);


    }


}

