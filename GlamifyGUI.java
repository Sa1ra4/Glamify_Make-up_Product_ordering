
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GlamifyGUI extends JFrame {

    private ProductManager manager;
    private Cart cart;
    private OrderHistory history;

    private JPanel productPanel;
    private DefaultListModel<String> cartModel;
    private JList<String> cartList;

    private JLabel totalLabel;
    private JLabel bagCountLabel;
    private JTextField searchField;
    private JComboBox<String> sortBox;

    private String selectedCategory = "All";

    private final Color pink = new Color(245, 65, 137);
    private final Color darkPink = new Color(173, 15, 83);
    private final Color deepPink = new Color(125, 10, 65);
    private final Color lightPink = new Color(255, 224, 237);
    private final Color background = new Color(255, 241, 247);

    public GlamifyGUI() {

        manager = new ProductManager();
        cart = new Cart();
        history = new OrderHistory();

        setTitle("GLAMIFY | Beauty Shopping");
        setSize(1450, 850);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        refreshProducts();

        setVisible(true);
    }

    // MAIN INTERFACE
    private void createInterface() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(background);

        // LEFT SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(deepPink);
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("👑 GLAMIFY");
        logo.setFont(new Font("Serif", Font.BOLD, 27));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel collection = new JLabel("✦ BEAUTY COLLECTION ✦");
        collection.setFont(new Font("SansSerif", Font.BOLD, 11));
        collection.setForeground(Color.WHITE);
        collection.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(collection);
        sidebar.add(Box.createVerticalStrut(35));

        sidebar.add(createSidebarButton("♡  Home", () -> {
            selectedCategory = "All";
            searchField.setText("");
            refreshProducts();
        }));

        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createSidebarButton("🛍  Products", () -> {
            selectedCategory = "All";
            searchField.setText("");
            refreshProducts();
        }));

        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createSidebarButton("▤  My Orders",
                this::showOrderHistory));

        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createSidebarButton("⚙  Admin Panel", () -> {
            try {
                Class<?> adminGUI = Class.forName("AdminGUI");
                adminGUI.getConstructor(ProductManager.class, Runnable.class)
                        .newInstance(manager, (Runnable) this::refreshProducts);
            } catch (ReflectiveOperationException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Admin panel is unavailable.",
                        "Admin Panel",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }));

        sidebar.add(Box.createVerticalGlue());

        JLabel quote = new JLabel(
                "<html><center><i>Be Confident<br>Be Beautiful<br>Be You ♡</i></center></html>"
        );
        quote.setFont(new Font("Serif", Font.ITALIC, 23));
        quote.setForeground(Color.WHITE);
        quote.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel decorations = new JLabel("💄 🌹 🎀 💅");
        decorations.setFont(new Font("SansSerif", Font.PLAIN, 25));
        decorations.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(quote);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(decorations);

        // CENTER PANEL
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(background);
        centerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        // BEAUTY BANNER
        GradientPanel banner = new GradientPanel(
                new Color(255, 190, 215),
                new Color(255, 225, 237)
        );

        banner.setLayout(new BorderLayout());
        banner.setPreferredSize(new Dimension(600, 145));
        banner.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel bannerText = new JPanel();
        bannerText.setOpaque(false);
        bannerText.setLayout(new BoxLayout(bannerText, BoxLayout.Y_AXIS));

        JLabel bannerTitle = new JLabel("Discover Your Beauty ♡");
        bannerTitle.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 35));
        bannerTitle.setForeground(darkPink);

        JLabel bannerSubtitle = new JLabel(
                "Curated makeup essentials just for you ✨"
        );
        bannerSubtitle.setFont(new Font("Serif", Font.ITALIC, 17));
        bannerSubtitle.setForeground(deepPink);

        bannerText.add(bannerTitle);
        bannerText.add(Box.createVerticalStrut(8));
        bannerText.add(bannerSubtitle);

        JLabel makeupArt = new JLabel("💄 💋 🎀 🪞");
        makeupArt.setFont(new Font("SansSerif", Font.PLAIN, 32));

        banner.add(bannerText, BorderLayout.WEST);
        banner.add(makeupArt, BorderLayout.EAST);

        // SEARCH
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(new CompoundBorder(
                new LineBorder(pink, 2, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JButton searchButton = createButton("🔍 Search", pink);
        searchButton.addActionListener(e -> refreshProducts());

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // SORT
        sortBox = new JComboBox<>(new String[]{
                "Default",
                "Price: Low to High",
                "Price: High to Low"
        });

        sortBox.addActionListener(e -> sortProducts());

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.setOpaque(false);
        sortPanel.add(new JLabel("Sort:"));
        sortPanel.add(sortBox);

        JPanel searchSort = new JPanel(new BorderLayout(10, 0));
        searchSort.setOpaque(false);
        searchSort.add(searchPanel, BorderLayout.CENTER);
        searchSort.add(sortPanel, BorderLayout.EAST);

        // CATEGORIES
        JPanel categoryPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 7, 5)
        );
        categoryPanel.setBackground(background);

        String[] categories = {
                "All", "Lipstick", "Blush", "Foundation",
                "Highlighter", "Mascara", "Concealer", "Powder"
        };

        for (String category : categories) {

            JButton button = new JButton(category);
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            button.setForeground(darkPink);
            button.setBackground(Color.WHITE);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(new CompoundBorder(
                    new LineBorder(pink, 1, true),
                    new EmptyBorder(8, 13, 8, 13)
            ));

            button.addActionListener(e -> {
                selectedCategory = category;
                refreshProducts();
            });

            categoryPanel.add(button);
        }

        JPanel controls = new JPanel(new BorderLayout());
        controls.setBackground(background);
        controls.add(searchSort, BorderLayout.NORTH);
        controls.add(categoryPanel, BorderLayout.CENTER);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(background);
        topArea.add(banner, BorderLayout.NORTH);
        topArea.add(controls, BorderLayout.CENTER);

        // PRODUCT PANEL
        productPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        productPanel.setBackground(background);
        productPanel.setBorder(new EmptyBorder(12, 5, 15, 5));

        JScrollPane productScroll = new JScrollPane(productPanel);
        productScroll.setBorder(null);
        productScroll.getVerticalScrollBar().setUnitIncrement(18);
        productScroll.getViewport().setBackground(background);

        centerPanel.add(topArea, BorderLayout.NORTH);
        centerPanel.add(productScroll, BorderLayout.CENTER);

        // RIGHT SHOPPING BAG
        JPanel bagPanel = new JPanel(new BorderLayout(8, 10));
        bagPanel.setPreferredSize(new Dimension(285, 0));
        bagPanel.setBackground(Color.WHITE);
        bagPanel.setBorder(new CompoundBorder(
                new LineBorder(pink, 2),
                new EmptyBorder(15, 13, 15, 13)
        ));

        JPanel bagHeader = new JPanel(new BorderLayout());
        bagHeader.setBackground(Color.WHITE);

        JLabel bagTitle = new JLabel("MY BAG ♡");
        bagTitle.setFont(new Font("Serif", Font.BOLD, 22));
        bagTitle.setForeground(darkPink);

        bagCountLabel = new JLabel("0 items");
        bagCountLabel.setForeground(darkPink);

        bagHeader.add(bagTitle, BorderLayout.WEST);
        bagHeader.add(bagCountLabel, BorderLayout.EAST);

        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartList.setFixedCellHeight(35);
        cartList.setBackground(new Color(255, 247, 251));

        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(new LineBorder(lightPink));

        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalLabel.setForeground(darkPink);

        JButton removeButton = createButton("✖ Remove Item", pink);
        removeButton.addActionListener(e -> removeCartItem());

        JButton checkoutButton = createButton("🛒 CHECKOUT →", darkPink);
        checkoutButton.addActionListener(e -> checkout());

        JButton historyButton = createButton("◷ View Order History", pink);
        historyButton.addActionListener(e -> showOrderHistory());

        JPanel bagBottom = new JPanel();
        bagBottom.setBackground(Color.WHITE);
        bagBottom.setLayout(new BoxLayout(bagBottom, BoxLayout.Y_AXIS));

        bagBottom.add(totalLabel);
        bagBottom.add(Box.createVerticalStrut(10));
        bagBottom.add(removeButton);
        bagBottom.add(Box.createVerticalStrut(8));
        bagBottom.add(checkoutButton);
        bagBottom.add(Box.createVerticalStrut(8));
        bagBottom.add(historyButton);

        bagPanel.add(bagHeader, BorderLayout.NORTH);
        bagPanel.add(cartScroll, BorderLayout.CENTER);
        bagPanel.add(bagBottom, BorderLayout.SOUTH);

        main.add(sidebar, BorderLayout.WEST);
        main.add(centerPanel, BorderLayout.CENTER);
        main.add(bagPanel, BorderLayout.EAST);

        add(main);
    }

    // GRADIENT PANEL
    class GradientPanel extends JPanel {

        private Color color1;
        private Color color2;

        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setPaint(new GradientPaint(
                    0, 0, color1,
                    getWidth(), getHeight(), color2
            ));

            g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 25, 25
            );

            g2.dispose();
        }
    }

    // SIDEBAR BUTTON
    private JButton createSidebarButton(String text, Runnable action) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(190, 48));
        button.setPreferredSize(new Dimension(190, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(190, 40, 105));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 175, 210), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        button.addActionListener(e -> action.run());

        return button;
    }

    // NORMAL BUTTON
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(new CompoundBorder(
                new LineBorder(color, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        return button;
    }

    // GET IMAGE PATH BASED ON PRODUCT CATEGORY
    private String getImagePath(String category) {

        String value = category.toLowerCase();

        if (value.contains("lipstick") || value.contains("lip")) {
            return "images/lipstick.png";
        }

        if (value.contains("blush")) {
            return "images/blush.png";
        }

        if (value.contains("foundation")) {
            return "images/foundation.png";
        }

        if (value.contains("highlighter")) {
            return "images/highlighter.png";
        }

        if (value.contains("mascara")) {
            return "images/mascara.png";
        }

        if (value.contains("concealer")) {
            return "images/concealer.png";
        }

        if (value.contains("powder")) {
            return "images/powder.png";
        }

        return "";
    }

    // PRODUCT EMOJI IF IMAGE IS MISSING
    private String getProductEmoji(String category) {

        String value = category.toLowerCase();

        if (value.contains("lipstick") || value.contains("lip")) {
            return "💄";
        } else if (value.contains("blush")) {
            return "🌸";
        } else if (value.contains("foundation")) {
            return "🧴";
        } else if (value.contains("highlighter")) {
            return "✨";
        } else if (value.contains("mascara")) {
            return "🖤";
        } else if (value.contains("concealer")) {
            return "💗";
        } else if (value.contains("powder")) {
            return "🎀";
        }

        return "💋";
    }

    // LOAD PRODUCT IMAGE
    private ImageIcon loadProductImage(String category) {

        String path = getImagePath(category);

        if (path.isEmpty()) {
            return null;
        }

        java.io.File file = new java.io.File(path);

        if (!file.exists()) {
            return null;
        }

        ImageIcon original = new ImageIcon(path);

        Image scaled = original.getImage().getScaledInstance(
                260, 170, Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaled);
    }

    // DISPLAY PRODUCTS
    private void refreshProducts() {

        productPanel.removeAll();

        String keyword = searchField.getText().trim().toLowerCase();

        for (Product product : manager.getProducts()) {

            boolean matchesSearch =
                    product.getName().toLowerCase().contains(keyword)
                    || product.getCategory().toLowerCase().contains(keyword);

            boolean matchesCategory =
                    selectedCategory.equals("All")
                    || product.getCategory().equalsIgnoreCase(selectedCategory);

            if (matchesSearch && matchesCategory) {
                productPanel.add(createProductCard(product));
            }
        }

        if (productPanel.getComponentCount() == 0) {

            productPanel.setLayout(new BorderLayout());

            JLabel noProducts = new JLabel(
                    "💗 No beauty products found!",
                    SwingConstants.CENTER
            );

            noProducts.setFont(new Font("Serif", Font.BOLD, 20));
            noProducts.setForeground(darkPink);

            productPanel.add(noProducts, BorderLayout.CENTER);

        } else {
            productPanel.setLayout(new GridLayout(0, 3, 15, 15));
        }

        productPanel.revalidate();
        productPanel.repaint();

        refreshCart();
    }

    // PRODUCT CARD WITH ACTUAL IMAGE
    private JPanel createProductCard(Product product) {

        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);

        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 170, 200), 2, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        // PRODUCT IMAGE
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 170));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(lightPink);

        ImageIcon productImage = loadProductImage(product.getCategory());

        if (productImage != null) {
            imageLabel.setIcon(productImage);
        } else {
            imageLabel.setText(
                    "<html><center><font size='7'>"
                    + getProductEmoji(product.getCategory())
                    + "</font><br><font color='#AD0F53'>GLAMIFY ♡</font></center></html>"
            );
        }

        // PRODUCT DETAILS
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(65, 20, 45));

        JLabel categoryLabel = new JLabel(product.getCategory());
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        categoryLabel.setForeground(pink);

        JLabel ratingLabel = new JLabel("★★★★★");
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        ratingLabel.setForeground(new Color(255, 165, 60));

        JLabel priceLabel = new JLabel(
                "₱" + String.format("%.2f", product.getPrice())
        );
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        priceLabel.setForeground(darkPink);

        JLabel stockLabel = new JLabel(
                "In stock: " + product.getStock()
        );
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(160, 65, 105));

        JPanel details = new JPanel();
        details.setBackground(Color.WHITE);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        details.add(nameLabel);
        details.add(Box.createVerticalStrut(5));
        details.add(categoryLabel);
        details.add(ratingLabel);
        details.add(Box.createVerticalStrut(12));

        JPanel priceStock = new JPanel(new BorderLayout());
        priceStock.setBackground(Color.WHITE);
        priceStock.add(priceLabel, BorderLayout.WEST);
        priceStock.add(stockLabel, BorderLayout.EAST);

        details.add(priceStock);

        // ADD TO CART
        JButton addButton = createButton("🛒 ADD TO CART", pink);

        addButton.addActionListener(e -> {

            if (product.getStock() <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "This product is out of stock!"
                );
                return;
            }

            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter quantity for " + product.getName() + ":",
                    "💗 Add to Beauty Bag",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) return;

            try {

                int quantity = Integer.parseInt(input);

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter a valid quantity."
                    );
                    return;
                }

                if (cart.addProduct(product, quantity)) {

                    refreshCart();

                    JOptionPane.showMessageDialog(
                            this,
                            "Added to your beauty bag! 💗"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Not enough stock available!"
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid number."
                );
            }
        });

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }

    // SORT PRODUCTS
    private void sortProducts() {

        String selected = (String) sortBox.getSelectedItem();

        if ("Price: Low to High".equals(selected)) {
            manager.sortByPrice();
        } else if ("Price: High to Low".equals(selected)) {
            manager.sortByHighestPrice();
        }

        refreshProducts();
    }

    // REFRESH CART
    private void refreshCart() {

        cartModel.clear();

        int totalItems = 0;

        for (Map.Entry<Product, Integer> entry :
                cart.getItems().entrySet()) {

            Product product = entry.getKey();
            int quantity = entry.getValue();

            totalItems += quantity;

            cartModel.addElement(
                    getProductEmoji(product.getCategory()) + " "
                    + product.getName() + " x" + quantity
                    + " - ₱"
                    + String.format("%.2f",
                    product.getPrice() * quantity)
            );
        }

        totalLabel.setText(
                "Total: ₱" + String.format("%.2f", cart.getTotal())
        );

        bagCountLabel.setText(totalItems + " items");
    }

    // REMOVE CART ITEM
    private void removeCartItem() {

        int selectedIndex = cartList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item to remove."
            );
            return;
        }

        Product selectedProduct = new ArrayList<>(
                cart.getItems().keySet()
        ).get(selectedIndex);

        cart.removeProduct(selectedProduct);
        refreshCart();
    }

    // CHECKOUT
    private void checkout() {

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your beauty bag is empty! 🛍️"
            );
            return;
        }

        for (Map.Entry<Product, Integer> entry :
                cart.getItems().entrySet()) {

            if (entry.getValue() > entry.getKey().getStock()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Not enough stock for "
                        + entry.getKey().getName()
                );

                return;
            }
        }

        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();
        JTextArea addressField = new JTextArea(3, 15);

        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));

        form.add(new JLabel("Full Name:"));
        form.add(nameField);

        form.add(new JLabel("Contact Number:"));
        form.add(contactField);

        form.add(new JLabel("Email Address:"));
        form.add(emailField);

        form.add(new JLabel("Delivery Address:"));
        form.add(new JScrollPane(addressField));

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "💗 Customer Information",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()
                || email.isEmpty() || address.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all customer information."
            );

            return;
        }

        Customer customer = new Customer(
                1, name, email, contact, address
        );

        Order order = new Order(cart.getItems(), customer);

        for (Map.Entry<Product, Integer> entry :
                cart.getItems().entrySet()) {

            entry.getKey().reduceStock(entry.getValue());
        }

        history.addOrder(order);

        StringBuilder receipt = new StringBuilder();

        receipt.append("       💄 GLAMIFY BEAUTY SHOP 💄\n");
        receipt.append("================================\n");
        receipt.append("             RECEIPT\n");
        receipt.append("================================\n");

        receipt.append("Order ID: ")
                .append(order.getOrderId()).append("\n");

        receipt.append("Customer: ")
                .append(customer.getFullName()).append("\n");

        receipt.append("Contact: ").append(contact).append("\n");
        receipt.append("Email: ").append(customer.getEmail()).append("\n");
        receipt.append("Address: ").append(customer.getAddress()).append("\n");

        receipt.append("--------------------------------\n");

        for (Map.Entry<Product, Integer> entry :
                order.getItems().entrySet()) {

            Product product = entry.getKey();
            int quantity = entry.getValue();

            receipt.append(product.getName())
                    .append(" x").append(quantity)
                    .append(" = ₱")
                    .append(String.format("%.2f",
                            product.getPrice() * quantity))
                    .append("\n");
        }

        receipt.append("--------------------------------\n");

        receipt.append("TOTAL: ₱")
                .append(String.format("%.2f", order.getTotal()))
                .append("\n");

        receipt.append("Status: ")
                .append(order.getStatus()).append("\n");

        receipt.append("================================\n");
        receipt.append("   Thank you for shopping! 💗\n");

        cart.clearCart();
        refreshProducts();

        JTextArea receiptArea = new JTextArea(receipt.toString());
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receiptArea.setBackground(lightPink);
        receiptArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(receiptArea),
                "💗 GLAMIFY Order Receipt",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ORDER HISTORY
    private void showOrderHistory() {

        ArrayList<Order> orders = history.getOrders();

        if (orders.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No orders have been placed yet. 💗"
            );

            return;
        }

        StringBuilder details = new StringBuilder();

        details.append("       💗 GLAMIFY ORDER HISTORY 💗\n");
        details.append("====================================\n\n");

        for (Order order : orders) {
            details.append(order.toString()).append("\n");
            details.append("------------------------------------\n");
        }

        JTextArea historyArea = new JTextArea(details.toString());
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        historyArea.setBackground(lightPink);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(historyArea),
                "📋 GLAMIFY Order History",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // MAIN METHOD
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GlamifyGUI::new);
    }
}
