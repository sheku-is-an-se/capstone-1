
package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


public class TrackerApp {
    public static Scanner scanner = new Scanner(System.in);
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you, come back soon!");
    }

    private static void printTransaction(Transaction transaction) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Date: " + transaction.getDate() + "   Time: " + transaction.getTime().format(timeFormatter));
        System.out.println("Desc: " + transaction.getDescription());
        System.out.println("Vendor: " + transaction.getVendor());

        if (transaction.getAmount() < 0) {
            System.out.println("Payment: $" + Math.abs(transaction.getAmount()));
        } else {
            System.out.println("Deposit: $" + transaction.getAmount());
        }
    }

    private static void pause() {
        String input = "";
        while (!input.equalsIgnoreCase("B")) {
            System.out.println();
            System.out.print("Enter B to go back: ");
            input = scanner.nextLine().trim();
        }
    }

    private static String promptForString(String prompt) {
        String result = "";
        // Keep looping until the user enters text
        while (result.trim().isEmpty()) {
            System.out.print(prompt);
            result = scanner.nextLine();


        }
        return result;
    }

    private static LocalDate promptForDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    private static double promptForAmount(String message) {

        //// Keep looping as long as the result is just whitespace or empty

        while (true) {
            try {
                System.out.println(message);
                String amountInput = scanner.nextLine();
                return Double.parseDouble(amountInput);

            } catch (NumberFormatException e) {
                System.err.println("Invalid selection, please type a number.");
            }
        }
    }

    private static LocalTime promptForTime(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid time format. Please use HH:mm:ss.");
            }
        }
    }


    private static void mainMenu() {
        String prompt = """
                                      
                                      𓅓
                ╔══════════════════════════════════════════════╗
                ║            LEDGERPRO FINANCE HUB             ║
                ╠══════════════════════════════════════════════╣
                ║   Track deposits, payments, and history      ║
                ╚══════════════════════════════════════════════╝
                
                                   MAIN MENU
                ------------------------------------------------
                [D] Add Deposit
                [P] Make Payment (Debit)
                [L] View Ledger
                [S] Summary
                [X] Exit Application
                ------------------------------------------------
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
                case "S":
                    showSummary();
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

    private static void saveTransaction(boolean isDeposit) {
        try {
            FileWriter fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            LocalDate date = promptForDate("Enter the date (yyyy-MM-dd): ");
            LocalTime time = promptForTime("Enter the time (HH:mm:ss): ");
            String description = promptForString("Enter the description: ");
            String vendor = promptForString("Enter the vendor: ");
            double amount = promptForAmount("What's the amount? ");

            if (isDeposit) {
                amount = Math.abs(amount);
            } else {
                amount = -Math.abs(amount);
            }

            String csvLine = date + "|" + time + "|" + description + "|" + vendor + "|" + amount;

            bufferedWriter.write(csvLine);
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addDeposit() {
        saveTransaction(true);
    }

    private static void makePayment() {
        saveTransaction(false);
    }

    private static ArrayList<Transaction> loadTransactions(String fileName) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            //skip the header
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            //While there is something to read, parse the line, add it to the array list and move on the next line
            while (line != null) {
                Transaction transaction = parseTransaction(line);
                transactions.add(transaction);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException fne) {
            System.err.println("File not found: " + fileName);
        } catch (IOException ex) {
            System.out.println("Error loading transactions file: " + ex);
            throw new RuntimeException(ex);
        }
        return transactions;
    }

    private static Transaction parseTransaction(String line) {
        //Split line by pipe
        String[] parts = line.split("\\|");
        if (parts.length != 5) {
            throw new RuntimeException("Unexpected number of fields");
        }
        //Turn into usable parts
        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);
        //Returns a transaction object using parameters defined in the Transaction Class
        return new Transaction(date, time, description, vendor, amount);
    }

    private static void ledgerMenu() {
        String prompt = """
                ╔══════════════════════════════════════════════╗
                ║               LEDGER CENTER                  ║
                ╠══════════════════════════════════════════════╣
                ║      Review transactions and reports         ║
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
        //Initialize a way to end this screen and go back based on user choice
        boolean running = true;


        do {
            System.out.println(prompt);
            String userMenu = scanner.nextLine().toUpperCase();


            switch (userMenu) {

                case "A":
                    System.out.println("========================================================================");
                    System.out.println("                          ALL TRANSACTIONS");
                    System.out.println("========================================================================");
                    displayLedger("A");
                    break;
                case "D":
                    System.out.println("========================================================================");
                    System.out.println("                             DEPOSITS");
                    System.out.println("========================================================================");
                    displayLedger("D");
                    break;
                case "P":
                    System.out.println("========================================================================");
                    System.out.println("                             PAYMENTS");
                    System.out.println("========================================================================");
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
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            switch (choice.toUpperCase()) {
                case "A":
                    printTransaction(transaction);
                    break;

                case "D":
                    if (transaction.getAmount() > 0) {
                        printTransaction(transaction);
                    }
                    break;

                case "P":
                    if (transaction.getAmount() < 0) {
                        printTransaction(transaction);
                    }
                    break;
            }
        }
        pause();
    }

    private static void showReportMenu() {
        String prompt = """
                ╔══════════════════════════════════════════════╗
                ║              LEDGERPRO FINANCE HUB           ║
                ╠══════════════════════════════════════════════╣
                ║          Reporting and search tools          ║
                ╚══════════════════════════════════════════════╝
                
                                REPORTS MENU
                ------------------------------------------------
                [1] Month To Date
                [2] Previous Month
                [3] Year To Date
                [4] Previous Year
                [5] Search By Vendor
                [6] Custom Search
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
                case "4":
                    displayPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customFilters();
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
        //Get today's date,month and year
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        //Loop the transactions and reverse list
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            int transactionMonth = transaction.getDate().getMonthValue();
            int transactionYear = transaction.getDate().getYear();
            //Condition to get month to date
            if (transactionMonth == currentMonth && transactionYear == currentYear && !transaction.getDate().isAfter(today)) {
                printTransaction(transaction);
            }
        }
        pause();
    }

    private static void previousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        //Loop and reverse list
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            int transactionMonth = transaction.getDate().getMonthValue();
            //A condition to get previous month
            if (transactionMonth == lastMonth.getMonthValue() && transaction.getDate().getYear() == lastMonth.getYear()) {
                printTransaction(transaction);
            }
        }
        pause();
    }

    private static void yearToDate() {
        //Get current year and today's date
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        //Loop through transactions and reverse the list
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            int transactionYear = transaction.getDate().getYear();
            //Condition for year to date
            if (transactionYear == currentYear && !transaction.getDate().isAfter(today)) {
                printTransaction(transaction);
            }
        }
        pause();
    }

    public static ArrayList<Transaction> customFilters() {
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        ArrayList<Transaction> transactions1 = new ArrayList<>();

        //ask for start date
        System.out.println("What is the start date?");
        String strDate = scanner.nextLine();


        //ask for end date
        System.out.println("What is the end date?");
        String eDate = scanner.nextLine();

        //ask for description
        System.out.println("What is the description?");
        String descPrompt = scanner.nextLine();

        //ask for vendor
        System.out.println("What is the vendor?");
        String vendPrompt = scanner.nextLine();

        //search for amount
        System.out.println("What is the amount?");
        String amount = scanner.nextLine();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction result = transactions.get(i);
            transactions1.add(result);
        }
        //If start date is blank, then print output
        if (strDate.isBlank()) {
            System.out.println("No start date applied");
        } else {
            try {
                //Turn the string into a local date to be able to use for comparing in a condition
                LocalDate.parse(strDate);
                transactions1 = filterByStartDate(transactions1, strDate);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid start date. Skipping start date filter.");
            }
        }

        if (eDate.isBlank()) {
            System.out.println("No end date applied");
        } else {
            try {
                LocalDate.parse(eDate);
                transactions1 = filterByEndDate(transactions1, eDate);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid end date. Skipping end date filter.");
            }
        }

        if (amount.isBlank()) {
            System.out.println("No amount applied");
        } else {
            try {
                Double.parseDouble(amount);
                transactions1 = filterByAmount(transactions1, amount);
            } catch (NumberFormatException e) {
                System.err.println("Invalid amount. Skipping amount filter.");
            }
        }

        if (descPrompt.isBlank()) {
            System.out.println("No description applied");
        } else {
            transactions1 = filterByDescription(transactions1, descPrompt);
        }

        if (vendPrompt.isBlank()) {
            System.out.println("No vendor applied");
        } else {
            transactions1 = filterByVendor(transactions1, vendPrompt);
        }


        for (Transaction transaction : transactions1) {
            printTransaction(transaction);
        }
        pause();
        return transactions1;

    }

    private static ArrayList<Transaction> filterByVendor(ArrayList<Transaction> result, String prompt) {
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        for (Transaction transaction : result) {
            if (prompt.equalsIgnoreCase(transaction.getVendor())) {
                transactions1.add(transaction);
            }

        }
        return transactions1;
    }


    private static ArrayList<Transaction> filterByAmount(ArrayList<Transaction> result, String prompt) {
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        double vendPrompt = Double.parseDouble(prompt);
        for (Transaction transaction : result) {
            if (Double.compare(vendPrompt, transaction.getAmount()) == 0) {
                transactions1.add(transaction);
            }
        }
        return transactions1;
    }

    private static ArrayList<Transaction> filterByDescription(ArrayList<Transaction> result, String prompt) {
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        for (Transaction transaction : result) {
            if (prompt.equalsIgnoreCase(transaction.getDescription())) {
                transactions1.add(transaction);
            }
        }
        return transactions1;
    }

    private static ArrayList<Transaction> filterByEndDate(ArrayList<Transaction> result, String prompt) {
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        LocalDate endDatePrompt = LocalDate.parse(prompt);
        for (Transaction transaction : result) {
            if (!endDatePrompt.isBefore(transaction.getDate())) {
                transactions1.add(transaction);
            }
        }
        return transactions1;
    }

    private static ArrayList<Transaction> filterByStartDate(ArrayList<Transaction> result, String prompt) {
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        LocalDate startDatePrompt = LocalDate.parse(prompt);
        for (Transaction transaction : result) {
            if (!startDatePrompt.isAfter(transaction.getDate())) {
                transactions1.add(transaction);
            }
        }
        return transactions1;
    }

    private static void displayPreviousYear() {
        LocalDate today = LocalDate.now();
        int lastYear = today.getYear() - 1;
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            int transactionYear = transaction.getDate().getYear();
            if (transactionYear == lastYear) {
                printTransaction(transaction);
            }
        }
        pause();
    }

    private static void searchByVendor() {
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);
        boolean keepRunning = true;

        do {
            boolean found = false; // Reset search status for each new name
            String vendorName = promptForString("Enter vendor name (or 'exit' to go back): ");

            if (vendorName.equalsIgnoreCase("exit")) {
                return;
            }

            //Searches and reverses the list
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction transaction = transactions.get(i);
                if (vendorName.equalsIgnoreCase(transaction.getVendor())) {
                    printTransaction(transaction);
                    found = true;
                }
            }

            //Handle the results
            if (found) {
                pause(); // Allow user to read transactions first
                String choice = promptForString("Do you want to search for more? (yes/no): ");
                if (choice.equalsIgnoreCase("no")) {
                    keepRunning = false; // Stop the loop
                }
            } else {
                System.err.println("Vendor not found. Try again, or type 'exit' to return.");

            }

        } while (keepRunning);
    }

    private static void showSummary() {
        ArrayList<Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);

        double balance = getCurrentBalance(transactions);
        double totalDeposits = getTotalDeposits(transactions);
        double totalPayments = getTotalPayments(transactions);
        double largestPayment = getLargestPayment(transactions);

        System.out.println("====================================");
        System.out.println("         FINANCIAL SUMMARY");
        System.out.println("====================================");
        System.out.println("Current Balance:  $" + String.format("%,.2f", balance));
        System.out.println("Total Deposits:   $" + String.format("%,.2f", totalDeposits));
        System.out.println("Total Payments:   $" + String.format("%,.2f", totalPayments));
        System.out.println("Largest Payment:  $" + String.format("%,.2f", largestPayment));
        System.out.println("====================================");

        pause();
    }


    private static double getCurrentBalance(ArrayList<Transaction> transactions) {
        double payments = 0.0;
        double deposits = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments += transaction.getAmount();
            } else {
                deposits += transaction.getAmount();
            }
        }
        return Double.sum(payments, deposits);
    }

    private static double getTotalDeposits(ArrayList<Transaction> transactions) {
        double deposits = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits += transaction.getAmount();
            }
        }
        return deposits;
    }

    private static double getTotalPayments(ArrayList<Transaction> transactions) {
        double payments = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments += Math.abs(transaction.getAmount());
            }
        }
        return payments;
    }

    private static double getLargestPayment(ArrayList<Transaction> transactions) {
        ArrayList<Double> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction.getAmount());
            }
        }
        if (payments.isEmpty()) {
            return 0.0;
        }
        //Start from the first index ( used to compare if the next number is bigger )
        double largest = payments.get(0);

        for (int s = 1; s < payments.size(); s++) {
            //all negatives so the lowest would technically be the highest
            if (payments.get(s) < largest) {
                largest = payments.get(s);
            }
        }
        return Math.abs(largest);
    }
}


