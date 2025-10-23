
package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
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

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        File file = new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
                System.out.println("No File found....Successfully created a new file: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Failed to create a new file");
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line =reader.readLine()) !=null) {
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

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart

        while(true) {
            System.out.println("Available Products");
            for (Product p : inventory) {
                System.out.printf("%s | %s | $%.2f%n", p.getId(), p.getProductName(), p.getPrice());
            }
            System.out.println("Enter the id of the product you want to add to cart or enter X to go back to the home menu.");
            String userChoice = scanner.nextLine();

            if (userChoice.equalsIgnoreCase("x")) {
                System.out.println("Returning to the home menu.");
                break;
            }

            if(userChoice.isEmpty()){
                System.out.println("Please enter a product ID or X to go back to the home menu.");
                continue;
            }
            boolean found = false;
            for (Product p : inventory){
                if (userChoice.equalsIgnoreCase(p.getId())){
                    cart.add(p);
                    System.out.println("You have successfully added " + p.getProductName() + " to your cart!");
                    found = true;
                }
            }
            if(!found){
                System.out.println("no product found with that id " + userChoice + ", going back to the home menu");
                break;
            }
            System.out.println("Do you want to add another product to your cart? Y for yes...N for no");
            String userSecondChoice = scanner.nextLine();
            if(!userSecondChoice.equalsIgnoreCase("y")){
                break;
            }
        }
    }
    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)
        while(true) {
            if (cart.isEmpty()) {
                System.out.println("Your cart is currently empty, going back to home menu.");
                return;
            }

            System.out.println("Your Cart");
            double total = 0.0;
            for (Product c : cart) {
                System.out.printf("%s | %s | $%.2f%n", c.getId(), c.getProductName(), c.getPrice());
                total += c.getPrice();
            }
            System.out.printf("Your Total: $%.2f%n", total);

            System.out.println("Enter C to checkout or X to go back to the home menu");
            String userChoice = scanner.nextLine();

            if (userChoice.equalsIgnoreCase("x")) {
                System.out.println("Returning to the home menu.");
                break;
            }
            if(userChoice.isEmpty()){
                System.out.println("Please enter C to checkout or X to go back to the home menu.");
                continue;
            }
            if(userChoice.equalsIgnoreCase("c")){
                checkOut(cart, total, scanner);
                break;
            }
        }
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above

    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        return null;
    }
}

 