
public class Order {

    private int orderId;
    private String customerName;
    private Product product;
    private int quantity;
    private double total;

    public Order(int orderId, String customerName,
                 Product product, int quantity) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.product = product;
        this.quantity = quantity;
        this.total = product.getPrice() * quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }
}
