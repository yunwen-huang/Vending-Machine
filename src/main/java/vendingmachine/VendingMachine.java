package vendingmachine;

import vendingmachine.exception.ExceedMaxFundException;
import vendingmachine.exception.NotEnoughFundException;
import vendingmachine.exception.NotEnoughInventoryException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The functions for initializing the vending machine, reset balance, reduce stock,
 * update current funds, dispense product and check transaction is success or fail
 */
public class VendingMachine {

    private Map<Product, Integer> inventoryByProduct = new HashMap<>();

    private BigDecimal currentBalance = BigDecimal.ZERO;

    private static final BigDecimal MAX_ALLOWED_BALANCE = BigDecimal.ONE;

    public VendingMachine() {
        initializeProductInventory();
    }

    /**
     * Initialized the products and the corresponding stock in the vending machine
     */
    private void initializeProductInventory() {

        Product productOne = new Product(1, "Product 1", 0.55);
        Product productTwo = new Product(2, "Product 2", 0.70);
        Product productThree = new Product(3, "Product 3", 0.75);
        inventoryByProduct.put(productOne, 10);
        inventoryByProduct.put(productTwo, 10);
        inventoryByProduct.put(productThree, 10);
    }

    /**
     * Reset current balance to 0
     */
    public void resetBalance() {
        currentBalance = BigDecimal.ZERO;
    }

    /**
     *
     * * @return - current balance in vending machine
     */
    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    /**
     * Retrieve the product from the inventory with the same ID desired
     * @param id- An integer represent product ID
     * @return - the product in the inventory with this product ID
     */
    public Optional<Product> getInventoryProductById(int id) {
        return this.inventoryByProduct.keySet().stream().filter(product -> product.getId() == id).findFirst();
    }


    public Map<Product, Integer> getInventoryByProduct() {
        return inventoryByProduct;
    }

    /**
     * Sum up the current balance and check if that will be exceeding max allowance
     * @param amount - a double that will be sum up to current balance
     * @throws ExceedMaxFundException- If the sum up current balance exceed the max allowance
     */
    public void addFund(double amount) throws ExceedMaxFundException {
        BigDecimal newBalance = currentBalance.add(BigDecimal.valueOf(amount));

        if (MAX_ALLOWED_BALANCE.compareTo(newBalance) < 0) {
            throw new ExceedMaxFundException("Adding fund fails! Exceeding max allowed balance: $" + MAX_ALLOWED_BALANCE);
        } else {
            currentBalance = newBalance;
        }
    }

    /**
     * Reduce product inventory by the quantity bought
     * @param product - A product instance
     * @param quantity - An integer of desired quantity
     * @throws NotEnoughInventoryException - If there is not enough stock for user to purchase
     */
    private void dispenseProduct(final Product product, int quantity) throws NotEnoughInventoryException {
        if (inventoryByProduct.containsKey(product)) {
            Integer currentInventory = inventoryByProduct.get(product);
            if (quantity > currentInventory) {
                throw new NotEnoughInventoryException("Not enough inventory for the product " + product);
            } else {
                inventoryByProduct.put(product, currentInventory - quantity);
            }
        } else {
            throw new IllegalArgumentException("Invalid input product - " + product);
        }
    }

    /**
     * Dispense the desired purchasing product with the given quantity.
     *
     * @param product - A product instance
     * @param quantity - An integer of desired quantity
     * @return - the remaining fund after the successful purchase
     * @throws NotEnoughInventoryException - If the current inventory for the product is not enough for purchase
     * @throws NotEnoughFundException - If the current fund balance is not enough for the transaction
     */
    public double purchaseProduct(final Product product, int quantity) throws NotEnoughInventoryException, NotEnoughFundException {

        if (checkEnoughFundForPurchase(product, quantity)) {
            dispenseProduct(product, quantity);
            double purchasedAmount = product.getPrice() * quantity;
            currentBalance = currentBalance.subtract(BigDecimal.valueOf(purchasedAmount));
            return currentBalance.doubleValue();
        } else {
            throw new NotEnoughFundException("You don't have enough fund. Current balance: $" + currentBalance.toPlainString());
        }
    }

    /**
     * check if the money is enough to buy the desired product and quantity
     * @param product - A product instance
     * @param quantity - An integer of desired quantity
     * @return - A boolean represent if the money is enough to buy
     */
    private boolean checkEnoughFundForPurchase(final Product product, int quantity) {
        BigDecimal purchasingAmount = BigDecimal.valueOf(product.getPrice() * quantity);
        return this.currentBalance.compareTo(purchasingAmount) >= 0;
    }

}
