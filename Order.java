
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {

    private static int nextOrderId = 1001;

    private int orderId;
    private LinkedHashMap<Product, Integer> items;
    private double total;
    private String status;
    private Customer customer;

    public Order(LinkedHashMap<Product, Integer> items,
                 Customer customer) {

        this.orderId = nextOrderId++;
        this.items = new LinkedHashMap<>(items);
        this.customer = customer;
        this.status = "Completed";

        calculateTotal();
    }

    private void calculateTotal() {

        total = 0;

        for (Map.Entry<Product, Integer> item : items.entrySet()) {
            total += item.getKey().getPrice() * item.getValue();
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LinkedHashMap<Product, Integer> getItems() {
        return new LinkedHashMap<>(items);
    }

    public void cancelOrder() {
        status = "Cancelled";
    }

    @Override
    public String toString() {

        return "Order #" + orderId
                + " | Customer: " + customer.getFullName()
                + " | Total: ₱" + String.format("%.2f", total)
                + " | Status: " + status;
    }
}
