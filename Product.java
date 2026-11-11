public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String imagePath; // Product image

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

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDetails() {
        return name + " - " + category;
    }
}

class MakeupProduct extends Product {

    public MakeupProduct(int id, String name, String category,
                         double price, int stock, String imagePath) {

        super(id, name, category, price, stock, imagePath);
    }

    @Override
    public String getDetails() {
        return "💄 " + super.getName()
                + " (" + super.getCategory() + ")";
    }
}

