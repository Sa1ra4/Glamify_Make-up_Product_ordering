import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class ProductManager {

    private ArrayList<Product> products;
    private HashMap<Integer, Product> productMap;

    public ProductManager() {

        products = new ArrayList<>();
        productMap = new HashMap<>();

        loadProducts();
    }

    private void loadProducts() {

        // ==================== LIPSTICK (4) ====================
        addProduct(new Product(101, "Maybelline Lipstick",
                "Lipstick", 399.00, 20, "images/Maybelline Super Stay Vinyl Ink Longwear No-Budge Liquid Lipcolor.jpg"));

        addProduct(new Product(102, "Careline Matte Lipstick",
                "Lipstick", 250.00, 15, "images/Amazon_com _ Careline Color Code Lipstick P35 Candy Pink, 1 count _ Beauty & Personal Care.jpg"));

        addProduct(new Product(103, "Ever Bilena Lip Tint",
                "Lipstick", 179.00, 30, "images/Ever Bilena Serum Tinted Lip Balms (New Shades).jpg"));

        addProduct(new Product(104, "Sunnies Face Fluffmatte",
                "Lipstick", 445.00, 12, "images/Sunnies Face Fluffmate - Rosas.jpg"));

        // ==================== BLUSH (3) ====================
        addProduct(new Product(105, "Careline Powder Blush",
                "Blush", 250.00, 15, "images/0833d824-d83a-4982-a8d8-ccbbc2556a88.jpg"));

        addProduct(new Product(106, "Detail Cream Blush",
                "Blush", 349.00, 18, "images/ph-11134207-7rasg-m8yvkb6lh8dad4.jpg"));

        addProduct(new Product(107, "Ever Bilena Cheek Tint",
                "Blush", 199.00, 25, "images/Ever Bilena Lip and Cheek Stain in 15ml or 20ml.jpg"));

        // ==================== FOUNDATION (4) ====================
        addProduct(new Product(108, "Maybelline Fit Me Foundation",
                "Foundation", 599.00, 10, "images/d1d0abc4-bec9-4d21-a40c-b3c6ce6ebda7.jpg"));

        addProduct(new Product(109, "L'Oreal True Match Foundation",
                "Foundation", 749.00, 8, "images/0c545998-bf96-4940-be92-fe99cdc8fa4c.jpg"));

        addProduct(new Product(110, "Careline BB Cream",
                "Foundation", 379.00, 15, "images/9435a792-b605-46e9-8ecd-e22436b977f8.jpg"));

        addProduct(new Product(111, "Sunnies Face Skin So Good",
                "Foundation", 899.00, 6, "images/9aa4d6eb-b0e0-4a13-8ca3-2563cb9764ef.jpg"));

        // ==================== HIGHLIGHTER (3) ====================
        addProduct(new Product(112, "Issy Highlighter",
                "Highlighter", 450.00, 12, "images/1349cb95-dd3e-4bb7-8539-d68c732bdf0a.jpg"));

        addProduct(new Product(113, "Lucky Beauty Highlighter",
                "Highlighter", 399.00, 10, "images/7f3d9ac6-95a0-4bc5-8ca8-a91c6725b249.jpg"));

        addProduct(new Product(114, "Ever Bilena Shimmer Palette",
                "Highlighter", 299.00, 14, "images/ba5cfb72-6d61-42dd-aecb-3ee33323068e.jpg"));

        // ==================== MASCARA (3) ====================
        addProduct(new Product(115, "Ever Bilena Mascara",
                "Mascara", 199.00, 25, "images/sg-11134202-22120-gci8tpn3x5kv83.jpg"));

        addProduct(new Product(116, "Maybelline Lash Sensational",
                "Mascara", 449.00, 18, "images/07f2b0c2-eefc-4bd7-9fdc-0c9b0c356c02.jpg"));

        addProduct(new Product(117, "Careline Waterproof Mascara",
                "Mascara", 229.00, 22, "images/d4c5f1b7-5ccf-4174-8a40-68ba3a53a095.jpg"));

        // ==================== CONCEALER (3) ====================
        addProduct(new Product(118, "Detail Cosmetics Concealer",
                "Concealer", 350.00, 18, "images/df13bbdb-f033-49da-9238-8c3fb06df033.jpg"));

        addProduct(new Product(119, "Maybelline Instant Age Rewind",
                "Concealer", 499.00, 12, "images/bd9d30a3-35ba-4377-bcfa-9ab8986bdb0f.jpg"));

        addProduct(new Product(120, "Careline Color Corrector",
                "Concealer", 279.00, 20, "images/f331c1d0-1cdb-45ca-b34d-f625b4402872.jpg"));
    }

    public void addProduct(Product product) {

        if (product != null && !productMap.containsKey(product.getId())) {
            products.add(product);
            productMap.put(product.getId(), product);
        }
    }

    public void removeProduct(int id) {

        Product product = productMap.remove(id);

        if (product != null) {
            products.remove(product);
        }
    }

    public Product findProductById(int id) {
        return productMap.get(id);
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public ArrayList<Product> searchProduct(String keyword) {

        ArrayList<Product> results = new ArrayList<>();

        for (Product product : products) {

            if (product.getName().toLowerCase()
                    .contains(keyword.toLowerCase())
                    || product.getCategory().toLowerCase()
                    .contains(keyword.toLowerCase())) {

                results.add(product);
            }
        }

        return results;
    }

    public ArrayList<Product> sortByPrice() {

        ArrayList<Product> sorted = getProducts();

        Collections.sort(sorted,
                Comparator.comparingDouble(Product::getPrice));

        return sorted;
    }

    public ArrayList<Product> sortByHighestPrice() {

        ArrayList<Product> sorted = getProducts();

        Collections.sort(sorted,
                Comparator.comparingDouble(Product::getPrice).reversed());

        return sorted;
    }

    // Helper for AdminGUI "next ID" generation (optional)
    public int nextId() {
        int max = 100; // start after 120 baseline
        for (Product p : products) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }
}