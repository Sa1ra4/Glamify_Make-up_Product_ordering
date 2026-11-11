
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlamifyGUI extends JFrame {

    private ProductManager manager;
    private ArrayList<Order> orderHistory;
    private HashMap<Integer, Integer> cart;

    private JPanel cardsPanel;
    private JPanel cartPanel;

    private DefaultTableModel cartModel;
    private DefaultTableModel historyModel;

    private JTable cartTable;
    private JTable historyTable;

    private JLabel totalLabel;
    private JLabel productCountLabel;
    private JTextField searchField;

    private final Color pink = new Color(255, 225, 237);
    private final Color darkPink = new Color(205, 85, 140);
    private final Color background = new Color(255, 247, 251);
    private final Color white = Color.WHITE;
    private final Color textColor = new Color(90, 55, 75);

    public GlamifyGUI() {

        manager = new ProductManager();
        orderHistory = new ArrayList<>();
        cart = new HashMap<>();

        setTitle("GLAMIFY | Beauty Shopping");
        setSize(1250, 800);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(background);
        setLayout(new BorderLayout());

        createHeader();
        createProductPanel();
        createCartPanel();
        createBottomPanel();

        refreshProducts();
        refreshCart();
    }

    // HEADER
    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkPink);
        header.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("💗 GLAMIFY");
        title.setForeground(white);
        title.setFont(new Font("Serif", Font.BOLD, 34));

        JLabel subtitle = new JLabel(
                "BEAUTY STARTS WITH YOU | Your Beauty, Your Style ✨");

        subtitle.setForeground(white);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        JLabel slogan = new JLabel("✨ BEAUTY COLLECTION ✨");
        slogan.setForeground(white);
        slogan.setFont(new Font("Arial", Font.BOLD, 16));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(slogan, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // PRODUCT SECTION
    private void createProductPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBackground(background);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 15, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("💄 BEAUTY PRODUCTS");
        heading.setFont(new Font("Serif", Font.BOLD, 25));
        heading.setForeground(darkPink);

        productCountLabel = new JLabel(
                "Find your favorite makeup products");

        productCountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        productCountLabel.setForeground(textColor);

        headingPanel.add(heading);
        headingPanel.add(productCountLabel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(13);
        searchField.setPreferredSize(new Dimension(160, 35));

        JButton searchButton = createButton("Search 🔍");
        JButton sortButton = createButton("Price: Low to High");
        JButton showAllButton = createButton("Show All");

        searchButton.addActionListener(e -> searchProduct());
        searchField.addActionListener(e -> searchProduct());

        sortButton.addActionListener(e -> {
            manager.sortByPrice();
            refreshProducts();
        });

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshProducts();
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(sortButton);
        searchPanel.add(showAllButton);

        topPanel.add(headingPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        cardsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        cardsPanel.setBackground(background);
        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 15, 5));

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(background);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // PRODUCT CARD
    private JPanel createProductCard(Product product) {

        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBackground(white);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(pink, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel imageLabel = new JLabel(
                getProductImage(product), SwingConstants.CENTER);

        imageLabel.setPreferredSize(new Dimension(180, 145));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(255, 240, 247));

        JPanel details = new JPanel(new GridLayout(4, 1, 2, 3));
        details.setOpaque(false);

        JLabel name = new JLabel(product.getName());
        name.setFont(new Font("Arial", Font.BOLD, 16));
        name.setForeground(textColor);

        JLabel category = new JLabel(
                "Category: " + product.getCategory());

        category.setFont(new Font("Arial", Font.PLAIN, 12));
        category.setForeground(Color.GRAY);

        JLabel price = new JLabel(
                String.format("₱%.2f", product.getPrice()));

        price.setFont(new Font("Arial", Font.BOLD, 17));
        price.setForeground(darkPink);

        JLabel stock = new JLabel("Stock: " + product.getStock());
        stock.setFont(new Font("Arial", Font.PLAIN, 12));
        stock.setForeground(textColor);

        details.add(name);
        details.add(category);
        details.add(price);
        details.add(stock);

        JButton addButton = createButton("ADD TO CART 🛒");
        addButton.setEnabled(product.getStock() > 0);

        addButton.addActionListener(e -> addProductToCart(product));

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }

    // LOAD PRODUCT IMAGE
    private ImageIcon getProductImage(Product product) {

        String imagePath = product.getImagePath();

        if (imagePath != null && !imagePath.trim().isEmpty()) {

            File file = new File(imagePath);

            if (file.exists()) {

                ImageIcon original = new ImageIcon(imagePath);

                Image scaled = original.getImage().getScaledInstance(
                        160, 130, Image.SCALE_SMOOTH);

                return new ImageIcon(scaled);
            }
        }

        return createPlaceholderImage();
    }

    // IMAGE PLACEHOLDER
    private ImageIcon createPlaceholderImage() {

        BufferedImage image = new BufferedImage(
                160, 130, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(255, 225, 237));
        g.fillRoundRect(0, 0, 160, 130, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(darkPink);
        g.drawString("GLAMIFY", 25, 60);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("BEAUTY PRODUCT", 32, 90);

        g.dispose();

        return new ImageIcon(image);
    }

    // REFRESH PRODUCTS
    private void refreshProducts() {
        displayProducts(manager.getProducts());
    }

    private void displayProducts(ArrayList<Product> products) {

        cardsPanel.removeAll();

        for (Product product : products) {
            cardsPanel.add(createProductCard(product));
        }

        productCountLabel.setText(
                products.size() + " beauty products available");

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // CART SECTION
    private void createCartPanel() {

        cartPanel = new JPanel(new BorderLayout(10, 12));
        cartPanel.setPreferredSize(new Dimension(330, 0));
        cartPanel.setBackground(pink);
        cartPanel.setBorder(
                BorderFactory.createEmptyBorder(18, 15, 18, 15));

        JLabel heading = new JLabel("🛍️ MY SHOPPING BAG");
        heading.setFont(new Font("Arial", Font.BOLD, 19));
        heading.setForeground(darkPink);

        cartModel = new DefaultTableModel(
                new String[]{"ID", "Product", "Qty", "Subtotal"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartModel);
        styleTable(cartTable);

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(30);

        totalLabel = new JLabel("TOTAL: ₱0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(darkPink);

        JButton removeButton = createButton("Remove Item");
        JButton checkoutButton = createButton("CHECKOUT 💗");
        JButton historyButton = createButton("Order History");

        removeButton.addActionListener(e -> removeFromCart());
        checkoutButton.addActionListener(e -> checkout());
        historyButton.addActionListener(e -> showHistory());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 8));
        buttonPanel.setOpaque(false);

        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(historyButton);

        JPanel bottom = new JPanel(new BorderLayout(5, 15));
        bottom.setOpaque(false);
        bottom.add(totalLabel, BorderLayout.NORTH);
        bottom.add(buttonPanel, BorderLayout.CENTER);

        cartPanel.add(heading, BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        cartPanel.add(bottom, BorderLayout.SOUTH);

        add(cartPanel, BorderLayout.EAST);
    }

    // REFRESH CART
    private void refreshCart() {

        cartModel.setRowCount(0);

        double total = 0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

            Product product = manager.findProduct(entry.getKey());

            if (product != null) {

                int quantity = entry.getValue();
                double subtotal = product.getPrice() * quantity;

                total += subtotal;

                cartModel.addRow(new Object[]{
                        product.getId(),
                        product.getName(),
                        quantity,
                        String.format("₱%.2f", subtotal)
                });
            }
        }

        totalLabel.setText(String.format("TOTAL: ₱%.2f", total));
    }

    // ADD TO CART
    private void addProductToCart(Product product) {

        if (product.getStock() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Sorry! This product is out of stock.");
            return;
        }

        String input = JOptionPane.showInputDialog(
                this, "Enter quantity for " + product.getName() + ":");

        if (input == null) return;

        try {

            int quantity = Integer.parseInt(input);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity must be greater than zero!");
                return;
            }

            int currentQuantity = cart.getOrDefault(product.getId(), 0);

            if (currentQuantity + quantity > product.getStock()) {
                JOptionPane.showMessageDialog(this,
                        "Not enough stock available!");
                return;
            }

            cart.put(product.getId(), currentQuantity + quantity);

            refreshCart();

            JOptionPane.showMessageDialog(this,
                    product.getName() + " added to your cart! 💗");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Please enter a valid quantity!");
        }
    }

    // REMOVE FROM CART
    private void removeFromCart() {

        int row = cartTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item first!");
            return;
        }

        int id = (int) cartModel.getValueAt(row, 0);

        cart.remove(id);
        refreshCart();
    }

    // SEARCH
    private void searchProduct() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            refreshProducts();
            return;
        }

        displayProducts(manager.searchProduct(keyword));
    }

    // CHECKOUT
    private void checkout() {

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your shopping cart is empty!");
            return;
        }

        String customerName = JOptionPane.showInputDialog(
                this, "Enter customer name:");

        if (customerName == null ||
                customerName.trim().isEmpty()) {
            return;
        }

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

            Product product = manager.findProduct(entry.getKey());

            if (product == null ||
                    entry.getValue() > product.getStock()) {

                JOptionPane.showMessageDialog(this,
                        "Not enough stock available!");
                return;
            }
        }

        int orderId = orderHistory.size() + 1;
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

            Product product = manager.findProduct(entry.getKey());
            int quantity = entry.getValue();

            product.setStock(product.getStock() - quantity);

            Order order = new Order(
                    orderId,
                    customerName,
                    product,
                    quantity
            );

            orderHistory.add(order);
            total += order.getTotal();
        }

        cart.clear();

        refreshProducts();
        refreshCart();

        JOptionPane.showMessageDialog(this,
                String.format(
                        "Thank you, %s! 💗\n" +
                        "Your order was successful!\n" +
                        "TOTAL: ₱%.2f",
                        customerName, total));
    }

    // ORDER HISTORY
    private void showHistory() {

        historyModel = new DefaultTableModel(
                new String[]{
                        "Order ID", "Customer", "Product",
                        "Quantity", "Total"
                }, 0);

        historyTable = new JTable(historyModel);
        styleTable(historyTable);

        for (Order order : orderHistory) {

            historyModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getProduct().getName(),
                    order.getQuantity(),
                    String.format("₱%.2f", order.getTotal())
            });
        }

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(historyTable),
                "💗 GLAMIFY ORDER HISTORY",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ADMIN MENU
    private void adminMenu() {

        String[] choices = {
                "Add Product",
                "Delete Product",
                "View Inventory"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an admin action:",
                "GLAMIFY ADMIN",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]
        );

        if (choice == 0) {
            addNewProduct();
        } else if (choice == 1) {
            deleteProduct();
        } else if (choice == 2) {
            refreshProducts();
        }
    }

    // ADD NEW PRODUCT
    private void addNewProduct() {

        try {

            String name = JOptionPane.showInputDialog(
                    this, "Product name:");

            if (name == null || name.trim().isEmpty()) return;

            String category = JOptionPane.showInputDialog(
                    this, "Category:");

            if (category == null || category.trim().isEmpty()) return;

            String priceInput = JOptionPane.showInputDialog(
                    this, "Price:");

            if (priceInput == null) return;

            String stockInput = JOptionPane.showInputDialog(
                    this, "Stock:");

            if (stockInput == null) return;

            double price = Double.parseDouble(priceInput);
            int stock = Integer.parseInt(stockInput);

            if (price < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this,
                        "Price and stock cannot be negative!");
                return;
            }

            int id = manager.getNextId();

            String imageName = name.trim()
                    .toLowerCase()
                    .replace(" ", "_");

            String imagePath = "images/" + imageName + ".png";

            MakeupProduct newProduct = new MakeupProduct(
                    id, name, category, price, stock, imagePath);

            manager.addProduct(newProduct);

            refreshProducts();

            JOptionPane.showMessageDialog(this,
                    "New makeup product added! 💗\n"
                    + "Image: " + imagePath);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers!");
        }
    }

    // DELETE PRODUCT
    private void deleteProduct() {

        String input = JOptionPane.showInputDialog(
                this, "Enter product ID:");

        if (input == null) return;

        try {

            int id = Integer.parseInt(input);

            if (manager.findProduct(id) == null) {
                JOptionPane.showMessageDialog(this,
                        "Product not found!");
                return;
            }

            manager.deleteProduct(id);
            cart.remove(id);

            refreshProducts();
            refreshCart();

            JOptionPane.showMessageDialog(this,
                    "Product deleted successfully!");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Enter a valid product ID!");
        }
    }

    // FOOTER
    private void createBottomPanel() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(background);
        footer.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JLabel footerText = new JLabel(
                "💗 GLAMIFY BEAUTY STORE | Thank you for shopping with us! ✨");

        footerText.setFont(new Font("Arial", Font.BOLD, 12));
        footerText.setForeground(darkPink);

        JButton adminButton = createButton("ADMIN ⚙");
        adminButton.addActionListener(e -> adminMenu());

        footer.add(footerText);
        footer.add(adminButton);

        add(footer, BorderLayout.SOUTH);
    }

    // BUTTON DESIGN
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(darkPink);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkPink),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)
        ));

        return button;
    }

    // TABLE DESIGN
    private void styleTable(JTable table) {

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        table.getTableHeader().setBackground(darkPink);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12));

        table.setSelectionBackground(pink);
        table.setSelectionForeground(textColor);
        table.setGridColor(new Color(245, 205, 225));
    }
}
