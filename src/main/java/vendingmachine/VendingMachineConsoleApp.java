package vendingmachine;


import vendingmachine.exception.ExceedMaxFundException;
import vendingmachine.exception.NotEnoughFundException;
import vendingmachine.exception.NotEnoughInventoryException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Display menu and instruction for the vending machine
 */
public class VendingMachineConsoleApp {

    private static NumberFormat CURRENCY_FORMAT = new DecimalFormat("#0.00");

    private VendingMachine vendingMachine;

    private Scanner console;


    public VendingMachineConsoleApp() {
        this.vendingMachine = new VendingMachine();
        this.console = new Scanner(System.in);
    }

    /**
     * Show instruction for coin input, read in the user input character, convert into the corresponding coin denomination
     */

    public void start() {

        try {
            while (true) {
                System.out.println("Enter N for 5 cent, D for 10 cent, Q for 25 cent, F for finish adding coin, R for reset, E for exit ");

                String userInput = console.nextLine();

                switch (userInput.toUpperCase()) {
                    case "N":
                        addFundToVendorMachine(0.05);
                        break;
                    case "D":
                        addFundToVendorMachine(0.1);
                        break;
                    case "Q":
                        addFundToVendorMachine(0.25);
                        break;
                    case "F":
                        // Display product item list
                        promptForPurchaseItemAndQuantity();
                        console.nextLine();
                        break;
                    case "R":
                        BigDecimal currentBalance = vendingMachine.getCurrentBalance();
                        vendingMachine.resetBalance();
                        System.out.println("Refunding $" + currentBalance);
                        break;
                    case "E":
                        System.out.println("Exit the vending machine console app...");
                        break;
                    default:
                        System.out.println("Invalid command!");
                }

                if ("E".equalsIgnoreCase(userInput)) {
                    break;
                }
            }
        } finally {
            console.close();
        }

    }

    /**
     *
     * @param amount - a double number given by user for adding funds
     */
    private void addFundToVendorMachine(double amount) {
        try {
            vendingMachine.addFund(amount);
        } catch (ExceedMaxFundException ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("Current balance: " + vendingMachine.getCurrentBalance().doubleValue());
        }
    }

    /**
     * Display product menu and show message to ask user input the desired product and quantity
     */
    private void promptForPurchaseItemAndQuantity() {
        Map<Product, Integer> inventoryByProduct = vendingMachine.getInventoryByProduct();
        Set<Integer> validProductIds = inventoryByProduct.keySet().stream()
                                                         .map(product -> product.getId())
                                                         .collect(Collectors.toSet());

        StringBuilder itemListMessageStringBuilder = new StringBuilder();
        inventoryByProduct.forEach((k, v) -> itemListMessageStringBuilder.append(String.format("Id [%s], Item name: %s, Item price: %s, Available quantity: %d", k.getId(), k.getName(), CURRENCY_FORMAT.format(k.getPrice()), v))
                                                                         .append("\n"));
        itemListMessageStringBuilder.append("Please select an item by entering the id:");


        Product selectedProduct;
        while (true) {
            System.out.println(itemListMessageStringBuilder.toString());

            try {
                int selectedProductId = console.nextInt();
                // validate the entered id
                if (validProductIds.contains(selectedProductId)) {
                    selectedProduct = vendingMachine.getInventoryProductById(selectedProductId).orElseThrow(() -> new RuntimeException("Invalid product id"));
                    break;
                } else {
                    // Invalid product id
                    System.out.println("You entered an invalid product id!");
                }
            } catch (Exception e) {
                System.out.println("You entered an invalid product id!");
                console.nextLine();
            }
        }

        int desiredQuantity;

        while (true) {
            System.out.println("Please enter the desired quantity");
            try {
                desiredQuantity = console.nextInt();
                if (desiredQuantity <= 0) {
                    throw new IllegalStateException();
                }
                break;
            } catch (Exception e) {
                System.out.println("You entered an invalid quantity!");
                console.nextLine();
            }
        }

        // Done selection, start purchase
        try {
            double remainingFund = vendingMachine.purchaseProduct(selectedProduct, desiredQuantity);
            System.out.println("Dispensing " + selectedProduct.getName() + " (Quantity: " + desiredQuantity + ")");
            System.out.println("Returning back $" + remainingFund);
            vendingMachine.resetBalance();
        } catch (NotEnoughInventoryException | NotEnoughFundException ex) {
            System.out.println(ex.getMessage());
        }


    }


    public static void main(String[] args) {
        VendingMachineConsoleApp vendingMachineConsoleApp = new VendingMachineConsoleApp();
        vendingMachineConsoleApp.start();
    }
}
