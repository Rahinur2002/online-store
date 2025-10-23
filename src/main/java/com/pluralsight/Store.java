
package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("No File found....Successfully created a new file: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Failed to create a new file");
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String productID = tokens[0];
                String productName = tokens[1];
                double price = Double.parseDouble(tokens[2]);

                Product product = new Product(productID, productName, price);

                inventory.add(product);
            }
            reader.close();

        } catch (IOException e) {
            System.err.println("error reading file: " + fileName);
        }
    }

    public static void receiptsFolder(String receipt) {
        File folder = new File("Receipts");

        try {
            if (!folder.exists()) {
                folder.mkdir();
                System.out.println("Successfully created a new file: " + folder);
            }
        } catch (Exception e) {
            System.err.println("error creating file: " + folder);
        }
        try {
            String timeStamp = LocalDate.now().toString().replace("-", "")
                    + String.format("%02d%02d%02d", LocalTime.now().getHour(),
                    LocalTime.now().getMinute(), LocalTime.now().getSecond());

            File receiptFolder = new File(folder, timeStamp + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFolder));
            writer.write(receipt);
            writer.close();
            System.out.println("Successfully saved the receipt");
        } catch (Exception e) {
            System.out.println("Error saving receipt" + e.getMessage());
        }
    }

    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        while (true) {
            System.out.println("Available Products");
            defaultHeader();
            for (Product p : inventory) {
                System.out.printf("%s | %s | $%.2f%n", p.getId(), p.getProductName(), p.getPrice());
            }
            longDashes();
            System.out.println("Enter the id of the product you want to add to cart or enter X to go back to the home menu.");
            String userChoice = scanner.nextLine();

            if (userChoice.equalsIgnoreCase("x")) {
                System.out.println("Returning to the home menu.");
                break;
            }

            if (userChoice.isEmpty()) {
                longDashes();
                System.out.println("Please enter a product ID or X to go back to the home menu.");
                continue;
            }
            boolean found = false;
            Product foundProduct = findProductById(userChoice, inventory);
            if (foundProduct != null) {
                cart.add(foundProduct);
                longDashes();
                System.out.println("You have successfully added " + foundProduct.getProductName() + " to your cart!");
                found = true;
            }

            if (!found) {
                System.out.println("no product found with that id " + userChoice + ", going back to the home menu");
                break;
            }
            longDashes();
            System.out.println("Do you want to add another product to your cart? Y for yes...N for no");
            String userSecondChoice = scanner.nextLine();
            if (!userSecondChoice.equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        while (true) {
            if (cart.isEmpty()) {
                System.out.println("Your cart is currently empty, going back to home menu.");
                return;
            }

            System.out.println("Your Cart");
            defaultHeader();
            double total = 0.0;
            for (Product c : cart) {
                System.out.printf("%s | %s | $%.2f%n", c.getId(), c.getProductName(), c.getPrice());
                total += c.getPrice();
            }
            longDashes();
            System.out.printf("Your Total: $%.2f%n", total);

            longDashes();
            System.out.println("Enter C to checkout or X to go back to the home menu");
            String userChoice = scanner.nextLine();

            if (userChoice.equalsIgnoreCase("x")) {
                System.out.println("Returning to the home menu.");
                break;
            }
            if (userChoice.isEmpty()) {
                longDashes();
                System.out.println("Please enter C to checkout or X to go back to the home menu.");
                continue;
            }
            if (userChoice.equalsIgnoreCase("c")) {
                checkOut(cart, total, scanner);
                break;
            }
        }
    }

    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        System.out.println("Are you sure you want to proceed to checkout? Y for yes...N for no");
        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("checkout cancelled");
            return;
        }
        longDashes();
        System.out.printf("Your Total Payment: $%.2f%n", totalAmount);
        double payment = 0.0;

        while (true) {
            longDashes();
            System.out.println("Enter the amount of money your paying");
            if (!scanner.hasNextDouble()) {
                longDashes();
                System.out.println("please enter a number");
                scanner.nextLine();
                continue;
            }
            payment = scanner.nextDouble();
            scanner.nextLine();

            if (payment < totalAmount) {
                longDashes();
                System.out.println("Insufficient amount, the total due is: " + totalAmount);
            } else {
                break;
            }
        }
        double change = payment - totalAmount;

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("Date: ").append(LocalDate.now()).append(" ").append(LocalTime.now().withNano(0)).append("\n");

        sb.append("Here's your receipt:\n");
        for (Product c : cart) {
            sb.append(String.format("%s $%.2f%n", c.getProductName(), c.getPrice()));
        }
        sb.append(String.format("TOTAL: $%.2f%n", totalAmount));
        sb.append(String.format("PAYMENT: $%.2f%n", payment));
        sb.append(String.format("CHANGE: $%.2f%n", change));
        sb.append("Thank you for shopping!");

        System.out.println(sb.toString());

        receiptsFolder(sb.toString());


        cart.clear();

    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product p : inventory) {
            if (id.equalsIgnoreCase(p.getId())) {
                return p;
            }
        }
        return null;

    }

    private static void defaultHeader() {
        System.out.printf("%s | %s | %S%n", "Product ID", "Product Name", "Price");
        System.out.println("==========================================================");
    }

    private static void longDashes() {
        System.out.println("==========================================================");
    }
}

 