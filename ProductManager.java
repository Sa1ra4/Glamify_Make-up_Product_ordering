import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

public class ProductManager {

    private ArrayList<Product> products;
    private HashMap<Integer, Product> productMap;

    public ProductManager() {
        products = new ArrayList<>();
        productMap = new HashMap<>();

        addProduct(new MakeupProduct(1, "Lipstick", "Lips", 250, 20,
                "Images/Amazon_com _ Careline Color Code Lipstick P35 Candy Pink, 1 count _ Beauty & Personal Care.jpg"));

        addProduct(new MakeupProduct(2, "Foundation", "Face", 450, 15,
                "Images/d1d0abc4-bec9-4d21-a40c-b3c6ce6ebda7.jpg"));

        addProduct(new MakeupProduct(3, "Blush", "Cheeks", 300, 18,
                "Images/0833d824-d83a-4982-a8d8-ccbbc2556a88.jpg"));

        addProduct(new MakeupProduct(4, "Mascara", "Eyes", 200, 25,
                "Images/sg-11134202-22120-gci8tpn3x5kv83.jpg"));
        addProduct(new MakeupProduct(5, "Eyeshadow", "Eyes", 350, 12,
                "Images/download.jpg"));

        addProduct(new MakeupProduct(6, "Lip Gloss", "Lips", 180, 30,
                "Images/rhode.jpg"));

        addProduct(new MakeupProduct(7, "Concealer", "Face", 280, 15,
                "Images/f331c1d0-1cdb-45ca-b34d-f625b4402872.jpg"));

        addProduct(new MakeupProduct(8, "Powder", "Face", 320, 10,
                "Images/Best Selling Makeup & Beauty Products.jpg"));
    }

    public void addProduct(Product product) {
        products.add(product);
        productMap.put(product.getId(), product);
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public Product findProduct(int id) {
        return productMap.get(id);
    }

    public ArrayList<Product> searchProduct(String keyword) {
        ArrayList<Product> result = new ArrayList<>();

        for (Product product : products) {
            if (product.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                result.add(product);
            }
        }

        return result;
    }

    public void sortByPrice() {
        products.sort(Comparator.comparingDouble(Product::getPrice));
    }

    public void deleteProduct(int id) {
        Product product = productMap.remove(id);

        if (product != null) {
            products.remove(product);
        }
    }

    public int getNextId() {
        int highest = 0;

        for (Product product : products) {
            if (product.getId() > highest) {
                highest = product.getId();
            }
        }

        return highest + 1;
    }
}
