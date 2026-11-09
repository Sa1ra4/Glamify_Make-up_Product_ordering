
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminGUI extends JFrame {

    private ProductManager manager;
    private Runnable refreshCustomer;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    private JLabel productCount;
    private JLabel stockCount;
    private JLabel imagePreview;
    private JLabel selectedProductLabel;

    private final Color DARK_PINK = new Color(159, 65, 105);
    private final Color PINK = new Color(224, 112, 151);
    private final Color LIGHT_PINK = new Color(255, 239, 244);
    private final Color CREAM = new Color(255, 250, 248);
    private final Color TEXT = new Color(75, 55, 62);

    public AdminGUI(ProductManager manager, Runnable refreshCustomer) {

        this.manager = manager;
        this.refreshCustomer = refreshCustomer;

        setTitle("GLAMIFY | Admin Dashboard");
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        refreshTable();

        setVisible(true);
    }

    // MAIN INTERFACE

    private void createInterface() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(CREAM);

        add(createSidebar(), BorderLayout.WEST);
        add(createDashboard(), BorderLayout.CENTER);
    }

    // SIDEBAR

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(205, 0));
        sidebar.setBackground(DARK_PINK);

        sidebar.setLayout(
            new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBorder(
            new EmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("GLAMIFY");

        logo.setFont(new Font("Serif", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("ADMIN PANEL");

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitle.setForeground(new Color(255, 215, 228));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);

        sidebar.add(Box.createVerticalStrut(55));

        JLabel menu = new JLabel("   MANAGEMENT");

        menu.setForeground(new Color(255, 215, 228));
        menu.setFont(new Font("SansSerif", Font.BOLD, 11));

        sidebar.add(menu);
        sidebar.add(Box.createVerticalStrut(18));

        JButton dashboardButton = sidebarButton("  ♡  Dashboard");
        JButton inventoryButton = sidebarButton("  ✿  Inventory");
        JButton addButton = sidebarButton("  +  Add Product");
        JButton closeButton = sidebarButton("  ←  Close Panel");

        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(inventoryButton);
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(addButton);
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(closeButton);

        dashboardButton.addActionListener(e -> refreshTable());

        inventoryButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        addButton.addActionListener(e -> addProduct());

        closeButton.addActionListener(e -> dispose());

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Beauty Store Management");

        footer.setForeground(Color.WHITE);
        footer.setFont(new Font("Serif", Font.ITALIC, 12));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(footer);

        return sidebar;
    }

    private JButton sidebarButton(String text) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(180, 42));

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBackground(DARK_PINK);
        button.setForeground(Color.WHITE);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));

        button.setBorder(
            new EmptyBorder(10, 12, 10, 5)
        );

        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // DASHBOARD

    private JPanel createDashboard() {

        JPanel main = new JPanel(new BorderLayout(15, 18));

        main.setBackground(CREAM);

        main.setBorder(
            new EmptyBorder(25, 25, 20, 25)
        );

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(CREAM);

        content.add(createOverview(), BorderLayout.NORTH);
        content.add(createInventoryPanel(), BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // HEADER

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(CREAM);

        JLabel title = new JLabel("Admin Dashboard");

        title.setFont(new Font("Serif", Font.BOLD, 29));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Manage your makeup products and inventory."
        );

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 3, 3));

        textPanel.setBackground(CREAM);

        textPanel.add(title);
        textPanel.add(subtitle);

        JLabel adminLabel = new JLabel("ADMINISTRATOR  ♡");

        adminLabel.setForeground(DARK_PINK);
        adminLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        header.add(textPanel, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);

        return header;
    }

    // DASHBOARD OVERVIEW

    private JPanel createOverview() {

        JPanel overview = new JPanel(new GridLayout(1, 2, 15, 15));

        overview.setBackground(CREAM);

        JPanel productCard = createStatCard(
            "TOTAL PRODUCTS",
            "0",
            "✿"
        );

        JPanel stockCard = createStatCard(
            "TOTAL STOCK",
            "0",
            "▣"
        );

        productCount = (JLabel)
            ((JPanel) productCard.getComponent(1))
                .getComponent(0);

        stockCount = (JLabel)
            ((JPanel) stockCard.getComponent(1))
                .getComponent(0);

        overview.add(productCard);
        overview.add(stockCard);

        return overview;
    }

    private JPanel createStatCard(
        String title, String value, String symbol) {

        JPanel card = new JPanel(new BorderLayout(10, 5));

        card.setBackground(Color.WHITE);

        card.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(240, 210, 220)),
                new EmptyBorder(15, 18, 15, 18)
            )
        );

        JLabel heading = new JLabel(title);

        heading.setFont(new Font("SansSerif", Font.BOLD, 12));
        heading.setForeground(Color.GRAY);

        JLabel icon = new JLabel(symbol);

        icon.setFont(new Font("Serif", Font.BOLD, 30));
        icon.setForeground(PINK);

        JLabel number = new JLabel(value);

        number.setFont(new Font("SansSerif", Font.BOLD, 27));
        number.setForeground(DARK_PINK);

        JPanel valuePanel = new JPanel(new BorderLayout());

        valuePanel.setBackground(Color.WHITE);

        valuePanel.add(number, BorderLayout.WEST);
        valuePanel.add(icon, BorderLayout.EAST);

        card.add(heading, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    // INVENTORY PANEL

    private JPanel createInventoryPanel() {

        JPanel panel = new JPanel(new BorderLayout(12, 12));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(240, 210, 220)),
                new EmptyBorder(15, 15, 15, 15)
            )
        );

        // INVENTORY HEADER

        JPanel header = new JPanel(new BorderLayout(10, 10));

        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Product Inventory");

        title.setFont(new Font("Serif", Font.BOLD, 21));
        title.setForeground(DARK_PINK);

        searchField = new JTextField(15);

        searchField.setPreferredSize(new Dimension(170, 34));

        searchField.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(230, 200, 210)),
                new EmptyBorder(5, 8, 5, 8)
            )
        );

        JButton searchButton = createButton("Search");

        searchButton.addActionListener(e -> searchProducts());

        searchField.addActionListener(e -> searchProducts());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        // TABLE

        String[] columns = {
            "ID",
            "Product Name",
            "Category",
            "Price",
            "Stock"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);

        productTable.setRowHeight(35);
        productTable.setFont(new Font("SansSerif", Font.PLAIN, 12));

        productTable.setSelectionBackground(LIGHT_PINK);
        productTable.setSelectionForeground(TEXT);

        productTable.setGridColor(new Color(245, 225, 232));

        JTableHeader tableHeader = productTable.getTableHeader();

        tableHeader.setBackground(DARK_PINK);
        tableHeader.setForeground(Color.WHITE);

        tableHeader.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );

        tableHeader.setPreferredSize(new Dimension(0, 38));

        JScrollPane scroll = new JScrollPane(productTable);

        scroll.setBorder(new LineBorder(
            new Color(240, 210, 220)
        ));

        panel.add(scroll, BorderLayout.CENTER);

        // IMAGE PREVIEW AND BUTTONS

        JPanel bottom = new JPanel(new BorderLayout(10, 10));

        bottom.setBackground(Color.WHITE);

        imagePreview = new JLabel(
            "Product Preview",
            SwingConstants.CENTER
        );

        imagePreview.setPreferredSize(new Dimension(120, 100));
        imagePreview.setOpaque(true);
        imagePreview.setBackground(LIGHT_PINK);

        imagePreview.setForeground(DARK_PINK);

        imagePreview.setBorder(
            new LineBorder(new Color(240, 210, 220))
        );

        selectedProductLabel = new JLabel("Select a product to preview.");

        selectedProductLabel.setFont(
            new Font("SansSerif", Font.PLAIN, 12)
        );

        selectedProductLabel.setForeground(TEXT);

        JPanel previewPanel = new JPanel(new BorderLayout(10, 5));

        previewPanel.setBackground(Color.WHITE);

        previewPanel.add(imagePreview, BorderLayout.WEST);
        previewPanel.add(selectedProductLabel, BorderLayout.CENTER);

        JButton addButton = createButton("+ Add Product");
        JButton editButton = createButton("Edit Product");
        JButton deleteButton = createButton("Delete Product");
        JButton refreshButton = createButton("Refresh");

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        JPanel buttons = new JPanel(
            new GridLayout(2, 2, 7, 7)
        );

        buttons.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        bottom.add(previewPanel, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.EAST);

        panel.add(bottom, BorderLayout.SOUTH);

        productTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                showSelectedProduct();
            }
        });

        return panel;
    }

    // REFRESH TABLE AND DASHBOARD

    private void refreshTable() {

        tableModel.setRowCount(0);

        int totalStock = 0;

        for (Product product : manager.getProducts()) {

            tableModel.addRow(new Object[]{

                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("₱%.2f", product.getPrice()),
                product.getStock()

            });

            totalStock += product.getStock();
        }

        productCount.setText(
            String.valueOf(manager.getProducts().size())
        );

        stockCount.setText(
            String.valueOf(totalStock)
        );

        imagePreview.setIcon(null);
        imagePreview.setText("Product Preview");

        selectedProductLabel.setText(
            "Select a product to preview."
        );
    }

    // ADD PRODUCT

    private void addProduct() {

        JTextField name = new JTextField();
        JTextField category = new JTextField();
        JTextField price = new JTextField();
        JTextField stock = new JTextField();
        JTextField image = new JTextField();

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));

        form.add(new JLabel("Product Name:"));
        form.add(name);

        form.add(new JLabel("Category:"));
        form.add(category);

        form.add(new JLabel("Price:"));
        form.add(price);

        form.add(new JLabel("Stock:"));
        form.add(stock);

        form.add(new JLabel("Image Path:"));
        form.add(image);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "ADD NEW PRODUCT",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String productName = name.getText().trim();
            String productCategory = category.getText().trim();

            double productPrice = Double.parseDouble(
                price.getText().trim()
            );

            int productStock = Integer.parseInt(
                stock.getText().trim()
            );

            String imagePath = image.getText().trim();

            if (productName.isEmpty()
                    || productCategory.isEmpty()
                    || productPrice <= 0
                    || productStock < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid product information."
                );

                return;
            }

            int id = 100 + (int) (Math.random() * 900000);

            while (manager.findProductById(id) != null) {
                id++;
            }

            Product product = new Product(
                id,
                productName,
                productCategory,
                productPrice,
                productStock,
                imagePath
            );

            manager.addProduct(product);

            refreshTable();
            refreshCustomer.run();

            JOptionPane.showMessageDialog(
                this,
                "Product added successfully!",
                "GLAMIFY",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid price and stock number."
            );
        }
    }

    // EDIT PRODUCT

    private void editProduct() {

        int row = productTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a product to edit."
            );

            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        Product product = manager.findProductById(id);

        if (product == null) {
            return;
        }

        JTextField name = new JTextField(product.getName());
        JTextField category = new JTextField(product.getCategory());

        JTextField price = new JTextField(
            String.valueOf(product.getPrice())
        );

        JTextField stock = new JTextField(
            String.valueOf(product.getStock())
        );

        JTextField image = new JTextField(
            product.getImagePath()
        );

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));

        form.add(new JLabel("Product Name:"));
        form.add(name);

        form.add(new JLabel("Category:"));
        form.add(category);

        form.add(new JLabel("Price:"));
        form.add(price);

        form.add(new JLabel("Stock:"));
        form.add(stock);

        form.add(new JLabel("Image Path:"));
        form.add(image);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "EDIT PRODUCT",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String newName = name.getText().trim();
            String newCategory = category.getText().trim();

            double newPrice = Double.parseDouble(
                price.getText().trim()
            );

            int newStock = Integer.parseInt(
                stock.getText().trim()
            );

            String newImage = image.getText().trim();

            if (newName.isEmpty()
                    || newCategory.isEmpty()
                    || newPrice <= 0
                    || newStock < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid product information."
                );

                return;
            }

            product.setName(newName);
            product.setCategory(newCategory);
            product.setPrice(newPrice);
            product.setStock(newStock);
            product.setImagePath(newImage);

            refreshTable();
            refreshCustomer.run();

            JOptionPane.showMessageDialog(
                this,
                "Product updated successfully!"
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid price or stock number."
            );
        }
    }

    // DELETE PRODUCT

    private void deleteProduct() {

        int row = productTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a product to delete."
            );

            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete " + name + " from inventory?",
            "CONFIRM DELETE",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            manager.removeProduct(id);

            refreshTable();
            refreshCustomer.run();

            JOptionPane.showMessageDialog(
                this,
                "Product deleted successfully!"
            );
        }
    }

    // SEARCH PRODUCTS

    private void searchProducts() {

        String keyword = searchField.getText().trim();

        ArrayList<Product> results =
            manager.searchProduct(keyword);

        tableModel.setRowCount(0);

        for (Product product : results) {

            tableModel.addRow(new Object[]{

                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("₱%.2f", product.getPrice()),
                product.getStock()

            });
        }
    }

    // PRODUCT IMAGE PREVIEW

    private void showSelectedProduct() {

        int row = productTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        Product product = manager.findProductById(id);

        if (product == null) {
            return;
        }

        selectedProductLabel.setText(
            "<html><b>" + product.getName()
            + "</b><br>"
            + product.getCategory()
            + "<br>₱" + String.format("%.2f", product.getPrice())
            + "<br>Stock: " + product.getStock()
            + "</html>"
        );

        imagePreview.setIcon(null);

        String path = product.getImagePath();

        if (path != null && !path.trim().isEmpty()) {

            ImageIcon icon = new ImageIcon(path);

            if (icon.getIconWidth() > 0) {

                Image image = icon.getImage().getScaledInstance(
                    110, 90, Image.SCALE_SMOOTH
                );

                imagePreview.setText("");
                imagePreview.setIcon(new ImageIcon(image));

            } else {

                imagePreview.setText("No Image");
            }

        } else {

            imagePreview.setText("No Image");
        }
    }

    // REUSABLE BUTTON DESIGN

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(PINK);
        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("SansSerif", Font.BOLD, 11)
        );

        button.setFocusPainted(false);

        button.setBorder(
            new EmptyBorder(9, 12, 9, 12)
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }
}
