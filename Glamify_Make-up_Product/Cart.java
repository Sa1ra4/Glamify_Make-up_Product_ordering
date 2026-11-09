
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private LinkedHashMap<Product, Integer> items;

    public Cart() {
        items = new LinkedHashMap<>();
    }

    public boolean addProduct(Product product, int quantity) {

        if (product == null || quantity <= 0) {
            return false;
        }

        int currentQuantity = items.getOrDefault(product, 0);

        if (currentQuantity + quantity > product.getStock()) {
            return false;
        }

        items.put(product, currentQuantity + quantity);
        return true;
    }

    public void removeProduct(Product product) {
        items.remove(product);
    }

    public void clearCart() {
        items.clear();
    }

    public LinkedHashMap<Product, Integer> getItems() {
        return new LinkedHashMap<>(items);
    }

    public double getTotal() {

        double total = 0;

        for (Map.Entry<Product, Integer> item : items.entrySet()) {
            total += item.getKey().getPrice() * item.getValue();
        }

        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

