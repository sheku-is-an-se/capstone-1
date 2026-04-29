
package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class TrackerApp {
    public static Scanner scanner = new Scanner(System.in);
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you, come back soon!");
    }

    private static void printTransaction(Transaction t) {
        String formatted = String.format("%s|%s|%s|%s|%.2f",
                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        System.out.println(formatted);
    }

    private static void pause() {
        String input = "";

        while (!input.equalsIgnoreCase("B")) {
            System.out.println();
            System.out.print("Enter B to go back: ");
            input = scanner.nextLine().trim();
        }
    }

    private static String promptForString(String input) {
        String result = "";

        //// Keep looping as long as the result is just whitespace or empty
        while (result.trim().isEmpty()) {
            System.out.println(input);
            result = scanner.nextLine();


        }
        return result;
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
            String userMenu = scanner.nextLine().toUpperCase();


            switch (userMenu) {
                case "D":
                    addDeposit();
                    System.out.println("Your deposit was successfully submitted!!");
                    break;
                case "P":
                    makePayment();
                    System.out.println("Your payment was successfully submitted!!");
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
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            System.out.println("What is the date of this transaction?(yyyy-MM-dd)");
            String userInputString = scanner.nextLine();
            LocalDate depositDate = LocalDate.parse(userInputString, dateFormatter);

            System.out.println("What is the time of this transaction?(HH:mm:ss)");
            userInputString = scanner.nextLine();
            LocalTime depositTime = LocalTime.parse(userInputString, timeFormatter);

            //prompt user for description
            System.out.println("What's the description of the payment?");
            String depDesc = scanner.nextLine();
            //prompt user for vendor information
            System.out.println("Who's the vendor for this payment?");
            String depVend = scanner.nextLine();
            //prompt user for amount
            System.out.println("What's the amount?");
            String payment = scanner.nextLine();
            Double depAmount = Double.parseDouble(payment);

            //in case the user types in a negative
            depAmount = Math.abs(depAmount);

            // Create the format that is going to enter transactions.csv
            String trans = depositDate + "|" + depositTime + "|" + depDesc + "|" + depVend + "|" + depAmount;

            //Write to file
            bufferedWriter.write(trans);

            //Make new space for the next deposit(go to the next line)
            bufferedWriter.newLine();
            //close bufferedWriter
            bufferedWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void makePayment() {
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;


        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true);
            bufferedWriter = new BufferedWriter(fileWriter);


            //Create pattern
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            //Prompt use for date
            System.out.println("What is the date of this transaction?(yyyy-MM-dd)");
            String depDate = scanner.nextLine();
            LocalDate depositDate = LocalDate.parse(depDate, dateFormatter);

            //Prompt user for time
            System.out.println("What is the time of this transaction?(HH:mm:ss)");
            String depTime = scanner.nextLine();
            LocalTime depositTime = LocalTime.parse(depTime, timeFormatter);

            //prompt user for description
            System.out.println("What's the description of the payment?");
            String payDesc = scanner.nextLine();
            //prompt user for vendor information
            System.out.println("Who's the vendor for this payment?");
            String payVend = scanner.nextLine();
            //prompt user for amount
            System.out.println("What's the amount?");
            String payment = scanner.nextLine();
            Double payAmount = Double.parseDouble(payment);

            //in case the user types in a negative
            payAmount = -Math.abs(payAmount);


            // Create the format that is going to enter transactions.csv
            String trans = depositDate + "|" + depositTime + "|" + payDesc + "|" + payVend + "|" + payAmount;

            //Write to file
            bufferedWriter.write(trans);

            //Go to the next line
            bufferedWriter.newLine();

            //close bufferedWriter
            bufferedWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static ArrayList<Transaction> loadTransactions(String fileName) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            // skip the header row
            line = bufferedReader.readLine();

            while (line != null) {
                Transaction transaction = parseTransactions(line);
                transactions.add(transaction);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException fne) {
            System.err.println("File not found: " + fileName);
        } catch (IOException ex) {
            System.out.println("Error loading inventory file: " + ex);
            throw new RuntimeException(ex);
        }

        return transactions;
    }

    private static Transaction parseTransactions(String line) {
        String[] parts = line.split("\\|");

        //todo make an exception for whitespace
        if (parts.length != 5) {
            throw new RuntimeException("Unexpected number of fields");

        }

        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transaction(date, time, description, vendor, amount);
    }


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
            String userMenu = scanner.nextLine().toUpperCase();



            switch (userMenu) {

                case "A":
                    displayLedger("A");
                    break;
                case "D": //deposits
                    displayLedger("D");
                    break;
                case "P": //payments
                    displayLedger("P");
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


    private static void displayLedger(String choice) {
        ArrayList<Transaction> inventory = loadTransactions(TRANSACTIONS_FILE_NAME);

        for (int i = inventory.size() - 1; i >= 0; i--) {
            Transaction s = inventory.get(i);
            switch (choice.toUpperCase()) {
                case "A":
                    printTransaction(s);
                    break;

                case "D":
                    if (s.getAmount() > 0) {
                        printTransaction(s);
                    }
                    break;

                case "P":
                    if (s.getAmount() < 0) {
                        printTransaction(s);
                    }
                    break;


            }
        }
        pause();
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
                    monthToDate();
                    break;
                case "2":
                    previousMonth();
                    break;
                case "3":
                    yearToDate();
                    break;
                case "4": //previous year
                    displayPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    //customSearch();
                    break;
                case "0": //Go back to ledger menu
                    running = false;
                    break;
                default:
                    System.err.println(("Oops! That wasn't a valid option."));
                    break;
            }
        } while (running);


    }


    private static void monthToDate() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction s = transactions.get(i);
            int transactionMonth = s.getDate().getMonthValue();
            int transactionYear = s.getDate().getYear();

            if (transactionMonth == currentMonth && transactionYear == currentYear && !s.getDate().isAfter(today)) {
                printTransaction(s);
            }
        }
        pause();
    }

    private static void previousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            int transactionMonth = t.getDate().getMonthValue();
            if (transactionMonth == lastMonth.getMonthValue() && t.getDate().getYear() == lastMonth.getYear()) {
                printTransaction(t);
            }
        }
        pause();
    }

    private static void yearToDate() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            int transactionYear = t.getDate().getYear();
            if (transactionYear == currentYear && !t.getDate().isAfter(today)) {
                printTransaction(t);
            }
        }
        pause();
    }

    private static void displayPreviousYear() {

        LocalDate today = LocalDate.now();
        int lastYear = today.getYear() - 1;
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            int transactionYear = t.getDate().getYear();
            if (transactionYear == lastYear) {
                printTransaction(t);
            }

        }
        pause();
    }


    private static void searchByVendor() {
        System.out.println("Which vendor would you like to search for?");
        String userInputString = scanner.nextLine();
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);

        boolean found = false;


        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            String vendorName = t.getVendor();
            if (userInputString.equalsIgnoreCase(vendorName)) {
                printTransaction(t);
                found = true;
            }

        }
        if (!found) {
            System.err.println("The vendor does not exist please try again");
        }
        pause();

    }


}


