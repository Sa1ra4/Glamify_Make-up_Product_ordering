
public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String imagePath;

    public Product(int id, String name, String category,
                   double price, int stock, String imagePath) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
    }
    public void reduceStock(int quantity) {
        if (quantity > 0 && quantity <= stock) {
            stock -= quantity;
        }
    }

    @Override
    public String toString() {
        return name + " - ₱" + price;
    }
}

