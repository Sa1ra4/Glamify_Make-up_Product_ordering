
import java.util.ArrayList;

public class OrderHistory {

    private ArrayList<Order> orders;

    public OrderHistory() {
        orders = new ArrayList<>();
    }

    public void addOrder(Order order) {

        if (order != null) {
            orders.add(order);
        }
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void cancelOrder(int orderId) {

        for (Order order : orders) {

            if (order.getOrderId() == orderId
                    && order.getStatus().equals("Completed")) {

                order.cancelOrder();
                break;
            }
        }
    }
}
