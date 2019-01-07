package vendingmachine;

import org.junit.jupiter.api.Test;
import vendingmachine.exception.ExceedMaxFundException;
import vendingmachine.exception.NotEnoughFundException;
import vendingmachine.exception.NotEnoughInventoryException;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    @Test
    void givenEnoughFund_whenPurchaseProduct_returnRemainingRefund() throws ExceedMaxFundException, NotEnoughFundException, NotEnoughInventoryException {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addFund(1.0);

        // Act
        Product product = new Product(1, "Product 1", 0.55);
        double refund = vendingMachine.purchaseProduct(product, 1);

        // Assert
        assertEquals(0.45, refund);
        assertEquals(0.45, vendingMachine.getCurrentBalance().doubleValue());
    }

    @Test
    void givenNotEnoughFund_whenPurchaseProduct_throwsNotEnoughFundException() throws ExceedMaxFundException {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();

        // Act & Assert
        Product product = new Product(1, "Product 1", 0.55);
        vendingMachine.addFund(0.5);
        assertThrows(NotEnoughFundException.class, () -> vendingMachine.purchaseProduct(product, 1));
    }


    @Test
    void givenNotEnoughInventory_whenPurchaseProduct_throwsNotEnoughInventoryException() throws ExceedMaxFundException {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();
        Product product = new Product(1, "Product 1", 0.55);
        vendingMachine.getInventoryByProduct().put(product, 0);
        vendingMachine.addFund(1.00);

        // Act & Assert
        assertThrows(NotEnoughInventoryException.class, () -> vendingMachine.purchaseProduct(product, 1));
    }

    @Test
    void givenEnoughFund_whenPurchaseInvalidProduct_throwsIllegalArgumentException() throws ExceedMaxFundException {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addFund(1.0);

        // Act & Assert
        Product product = new Product(4, "Product 4", 1.0);
        assertThrows(IllegalArgumentException.class, () -> vendingMachine.purchaseProduct(product, 1));
    }

    @Test
    void givenMaxOneDollarBalanceConstraint_whenAddFund_expectAddedToCurrentBalance() throws ExceedMaxFundException {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();

        // Act
        vendingMachine.addFund(1.0);

        // Assert
        assertEquals(1.0, vendingMachine.getCurrentBalance().doubleValue());
    }

    @Test
    void givenMaxOneDollarBalanceConstraint_whenAddFund_throwsExceedMaxFundException() {

        // Arrange
        VendingMachine vendingMachine = new VendingMachine();

        // Act & Assert
        assertThrows(ExceedMaxFundException.class, () -> vendingMachine.addFund(2.0));
    }


}