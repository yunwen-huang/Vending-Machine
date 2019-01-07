package vendingmachine;

/**
 * This product is going to be sold in vending machine, each has id, name and price
 */
public class Product {

    private int id;

    private String name;

    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return "Product(id=" + this.getId() + ", name=" + this.getName() + ", price=" + this.getPrice() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Product)) return false;
        final Product other = (Product) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        return result;
    }


}
