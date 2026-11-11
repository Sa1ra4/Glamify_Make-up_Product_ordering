# 💗 GLAMIFY – Beauty Shopping System

Welcome to **GLAMIFY**, a Java-based beauty shopping system. This project allows users to view beauty products, search and sort products, add items to a shopping cart, check out orders, and view order history. It also includes product-management functions for adding and deleting products.

The application uses a graphical user interface (GUI) made with Java Swing. It was developed as a Java coursework project to apply Object-Oriented Programming (OOP) concepts and Data Structures and Algorithms (DSA).

---

## 📂 Project Directory

The project contains the following Java files and resources:

| File / Folder | Purpose |
|---|---|
| `Main.java` | Starts the application and opens the GLAMIFY window. |
| `GlamifyGUI.java` | Builds the graphical interface and handles user actions such as searching, adding to cart, checkout, and viewing order history. |
| `Product.java` | Defines the product information, including ID, name, category, price, stock, and image path. |
| `MakeupProduct` | A subclass of `Product` that represents makeup products and provides its own product-details format. |
| `ProductManager.java` | Stores and manages the product list. It includes adding, finding, searching, sorting, and deleting products. |
| `Order.java` | Represents an order and stores its order ID, customer name, product, quantity, and total amount. |
| `Images/` | Contains the product pictures displayed in the application. |

---

## 🛠️ Tech Stack & Prerequisites

- **Programming Language:** Java
- **GUI Library:** Java Swing (`javax.swing`)
- **Graphics / Layout:** Java AWT (`java.awt`)
- **Image Processing:** `BufferedImage`, `Graphics2D`, and `ImageIcon`
- **Data Structures:** `ArrayList`, `HashMap`, and `Map`
- **Sorting Utility:** `Comparator`
- **Development Environment:** VS Code, IntelliJ IDEA, Eclipse, or NetBeans
- **Required Software:** JDK installed on your computer

### Java Imports / Libraries Used

| Import | Purpose |
|---|---|
| `javax.swing.*` | Creates windows, buttons, labels, tables, text fields, panels, dialogs, and other GUI components. |
| `javax.swing.table.DefaultTableModel` | Adds and manages rows in the cart and order-history tables. |
| `java.awt.*` | Provides layouts, colors, fonts, dimensions, images, and graphics tools. |
| `java.awt.image.BufferedImage` | Creates a placeholder image when a product image cannot be loaded. |
| `java.io.File` | Checks whether the product image file exists. |
| `java.util.ArrayList` | Stores the products and order history in a list. |
| `java.util.HashMap` | Stores products by ID and stores the quantity of each product in the cart. |
| `java.util.Map` | Allows the program to access the entries in a map. |
| `java.util.Comparator` | Compares product prices when sorting products from lowest to highest. |

---

## 🧩 OOP Concepts / Four Pillars Used

The project demonstrates the following OOP concepts:

### 1. Encapsulation
The fields in `Product` and `Order` are declared `private`. Other classes access their values through public getter methods. The product stock can be updated using `setStock()`.

**Examples:**
- `private int id;`
- `private String name;`
- `getName()`
- `getPrice()`
- `setStock(int stock)`

### 2. Inheritance
`MakeupProduct` extends the `Product` class. This means it inherits the fields and methods of `Product` instead of defining them all again.

**Example:**
```java
class MakeupProduct extends Product
```

### 3. Polymorphism
`MakeupProduct` overrides the `getDetails()` method from `Product`. The same method name can provide a different result depending on the object that uses it.

**Example:**
```java
@Override
public String getDetails() {
    return "💄 " + super.getName()
            + " (" + super.getCategory() + ")";
}
```

### 4. Abstraction
The uploaded source code does **not** directly use an `abstract` class or an `interface`. The project mainly demonstrates encapsulation, inheritance, and polymorphism. Do not describe abstraction as implemented unless an abstract class or interface is added to the project.

---

## 📚 Data Structures & Algorithms (DSA) Used

### Data Structures

| Data Structure | Where It Is Used | Purpose |
|---|---|---|
| `ArrayList<Product>` | `ProductManager.java` | Stores the collection of beauty products. |
| `HashMap<Integer, Product>` | `ProductManager.java` | Finds a product using its ID as the key. |
| `HashMap<Integer, Integer>` | `GlamifyGUI.java` | Stores the product ID and its quantity in the shopping cart. |
| `ArrayList<Order>` | `GlamifyGUI.java` | Stores the order records during the running session. |
| `Map.Entry<Integer, Integer>` | `GlamifyGUI.java` | Reads each product ID and quantity from the cart map. |

### Algorithms / Operations

- **Linear Search** – `searchProduct()` checks the product names one by one to find a matching keyword.
- **Sorting** – `sortByPrice()` uses `Comparator.comparingDouble()` to arrange products by price, from lowest to highest.
- **Traversal / Iteration** – `for` loops visit products, cart entries, and order records.
- **Lookup by Key** – `productMap.get(id)` finds a product using its ID in the `HashMap`.
- **Finding the Next ID** – `getNextId()` checks the product IDs and returns the next available ID based on the highest ID found.
- **Cart Quantity Update** – `getOrDefault()` gets the current quantity, then the program updates the cart using `put()`.

---

## ⚙️ Main Functions and Their Uses

### `Main.java`
- `main()` – The starting point of the program. It uses `SwingUtilities.invokeLater()` to create and show the GUI on Swing's Event Dispatch Thread.

### `Product.java`
- `Product()` – Constructor that sets the product's ID, name, category, price, stock, and image path.
- `getId()` – Returns the product ID.
- `getName()` – Returns the product name.
- `getCategory()` – Returns the product category.
- `getPrice()` – Returns the product price.
- `getStock()` – Returns the available stock.
- `getImagePath()` – Returns the product image path.
- `setStock()` – Updates the product's stock.
- `getDetails()` – Returns the product name and category.
- `MakeupProduct.getDetails()` – Overrides the parent method and formats the details for a makeup product.

### `ProductManager.java`
- `ProductManager()` – Creates the product list and map, then adds the sample beauty products.
- `addProduct(Product product)` – Adds a product to the `ArrayList` and stores it in the `HashMap` using its ID.
- `getProducts()` – Returns a copy of the product list.
- `findProduct(int id)` – Finds and returns a product using its ID.
- `searchProduct(String keyword)` – Searches product names using a keyword.
- `sortByPrice()` – Sorts the products by price from lowest to highest.
- `deleteProduct(int id)` – Removes a product from the map and list.
- `getNextId()` – Finds the highest product ID and returns the next ID.

### `Order.java`
- `Order()` – Constructor that creates an order and calculates its total using product price multiplied by quantity.
- `getOrderId()` – Returns the order ID.
- `getCustomerName()` – Returns the customer's name.
- `getProduct()` – Returns the product included in the order.
- `getQuantity()` – Returns the ordered quantity.
- `getTotal()` – Returns the order total.

### `GlamifyGUI.java`
- `GlamifyGUI()` – Initializes the product manager, cart, order history, and main window.
- `createHeader()` – Creates the top header and title of the application.
- `createProductPanel()` – Creates the product section, search field, and sorting controls.
- `createProductCard()` – Displays one product's image, name, category, price, stock, and Add to Cart button.
- `getProductImage()` – Loads and resizes a product image.
- `createPlaceholderImage()` – Creates a default image when the product image is missing.
- `refreshProducts()` – Gets the current products and displays them again.
- `displayProducts()` – Clears the product panel and displays the products passed to it.
- `createCartPanel()` – Creates the shopping cart table and its buttons.
- `refreshCart()` – Updates the cart table and calculates the current total.
- `addProductToCart()` – Checks the stock and quantity, then adds the selected product to the cart.
- `removeFromCart()` – Removes the selected product from the cart.
- `searchProduct()` – Gets the keyword from the search field and displays matching products.
- `checkout()` – Gets the customer's name, checks stock, creates order records, reduces stock, clears the cart, and displays the final amount.
- `showHistory()` – Displays the order records in a table.
- `adminMenu()` – Shows the product-management choices.
- `addNewProduct()` – Gets product details from the user and adds a new product.
- `deleteProduct()` – Handles the product-deletion action.
- `createBottomPanel()` – Creates the bottom section of the application.
- `createButton()` – Creates a button with the project's design style.
- `styleTable()` – Applies the table design used in the application.

---

## 🔄 How the System Works

1. `Main.java` starts the application.
2. `GlamifyGUI.java` opens the GLAMIFY shopping window.
3. `ProductManager.java` provides the product list.
4. The GUI displays each product with its image, name, category, price, and stock.
5. The customer can search for products, sort by price, and add products to the cart.
6. The cart uses a `HashMap` to keep each product ID and its quantity.
7. During checkout, the system asks for the customer's name and checks if enough stock is available.
8. The system creates an `Order` object for each cart item, reduces the product stock, and adds the order to the order-history list.
9. The cart is cleared and the updated product list and total are displayed.

---

## ▶️ How to Run the Project

1. Install the Java Development Kit (JDK).
2. Open the project folder in your Java IDE or VS Code.
3. Make sure the `Images` folder is in the same project folder as the Java files.
4. Open `Main.java`.
5. Run the `Main` class.
---

## 👩‍💻 Authors

**Saira Jane A. Campo** 

**Kristel Charm Camano** 

**Bachelor of Science in Information Technology (BSIT)**  

**2nd Year**
