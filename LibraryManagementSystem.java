package NAMANPROJECTS;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class LibraryManagementSystem extends JFrame {
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private DefaultTableModel booksTableModel, usersTableModel, transactionsTableModel;
    private JTabbedPane tabbedPane;
    private JTextField searchField, bookTitleField, bookAuthorField, bookIsbnField, bookQuantityField;
    private JTextField userNameField, userEmailField, userPhoneField;
    private JComboBox<String> userComboBox, bookComboBox;
    private JLabel totalBooksLabel, totalUsersLabel, activeBorrowsLabel, totalRevenueLabel;

    public LibraryManagementSystem() {
        initializeData();
        setupUI();
        refreshAllData();
    }

    private void initializeData() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        transactions = new ArrayList<>();

        // Sample data
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", 5));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", 3));
        books.add(new Book("1984", "George Orwell", "978-0-452-28423-4", 4));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", 2));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", 6));

        users.add(new User("John Doe", "john@email.com", "123-456-7890"));
        users.add(new User("Jane Smith", "jane@email.com", "987-654-3210"));
        users.add(new User("Bob Johnson", "bob@email.com", "555-123-4567"));
    }

    private void setupUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("📚 Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new Color(236, 240, 241));

        totalBooksLabel = createStatLabel("Total Books: 0", new Color(52, 152, 219));
        totalUsersLabel = createStatLabel("Total Users: 0", new Color(46, 204, 113));
        activeBorrowsLabel = createStatLabel("Active Borrows: 0", new Color(155, 89, 182));
        totalRevenueLabel = createStatLabel("Total Revenue: ₹0", new Color(231, 76, 60));

        statsPanel.add(totalBooksLabel);
        statsPanel.add(totalUsersLabel);
        statsPanel.add(activeBorrowsLabel);
        statsPanel.add(totalRevenueLabel);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());
        tabbedPane.addTab("Borrow/Return", createBorrowReturnPanel());

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new TitledBorder("Search Books"));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> searchBooks());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Books Table
        String[] columns = {"Title", "Author", "ISBN", "Available", "Total"};
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable booksTable = new JTable(booksTableModel);
        styleTable(booksTable);

        JScrollPane tableScrollPane = new JScrollPane(booksTable);

        // Add Book Form
        JPanel addBookPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        addBookPanel.setBorder(new TitledBorder("Add New Book"));

        addBookPanel.add(new JLabel("Title:"));
        bookTitleField = new JTextField();
        addBookPanel.add(bookTitleField);

        addBookPanel.add(new JLabel("Author:"));
        bookAuthorField = new JTextField();
        addBookPanel.add(bookAuthorField);

        addBookPanel.add(new JLabel("ISBN:"));
        bookIsbnField = new JTextField();
        addBookPanel.add(bookIsbnField);

        addBookPanel.add(new JLabel("Quantity:"));
        bookQuantityField = new JTextField();
        addBookPanel.add(bookQuantityField);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBackground(new Color(46, 204, 113));
        addBookButton.setForeground(Color.WHITE);
        addBookButton.addActionListener(e -> addBook());
        addBookPanel.add(addBookButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(addBookPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Users Table
        String[] columns = {"Name", "Email", "Phone", "Active Borrows"};
        usersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable usersTable = new JTable(usersTableModel);
        styleTable(usersTable);

        JScrollPane tableScrollPane = new JScrollPane(usersTable);

        // Add User Form
        JPanel addUserPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        addUserPanel.setBorder(new TitledBorder("Add New User"));

        addUserPanel.add(new JLabel("Name:"));
        userNameField = new JTextField();
        addUserPanel.add(userNameField);

        addUserPanel.add(new JLabel("Email:"));
        userEmailField = new JTextField();
        addUserPanel.add(userEmailField);

        addUserPanel.add(new JLabel("Phone:"));
        userPhoneField = new JTextField();
        addUserPanel.add(userPhoneField);

        JButton addUserButton = new JButton("Add User");
        addUserButton.setBackground(new Color(46, 204, 113));
        addUserButton.setForeground(Color.WHITE);
        addUserButton.addActionListener(e -> addUser());
        addUserPanel.add(addUserButton);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(addUserPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Transactions Table
        String[] columns = {"User", "Book", "Issue Date", "Due Date", "Return Date", "Late Fee"};
        transactionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable transactionsTable = new JTable(transactionsTableModel);
        styleTable(transactionsTable);

        JScrollPane tableScrollPane = new JScrollPane(transactionsTable);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Borrow Panel
        JPanel borrowPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        borrowPanel.setBorder(new TitledBorder("Borrow Book"));

        borrowPanel.add(new JLabel("Select User:"));
        userComboBox = new JComboBox<>();
        borrowPanel.add(userComboBox);

        borrowPanel.add(new JLabel("Select Book:"));
        bookComboBox = new JComboBox<>();
        borrowPanel.add(bookComboBox);

        JButton borrowButton = new JButton("Issue Book");
        borrowButton.setBackground(new Color(46, 204, 113));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.addActionListener(e -> issueBook());
        borrowPanel.add(borrowButton);

        // Return Panel
        JPanel returnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        returnPanel.setBorder(new TitledBorder("Return Book"));

        returnPanel.add(new JLabel("Select Transaction:"));
        JComboBox<String> transactionCombo = new JComboBox<>();
        returnPanel.add(transactionCombo);

        JButton returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(231, 76, 60));
        returnButton.setForeground(Color.WHITE);
        returnButton.addActionListener(e -> returnBook(transactionCombo));
        returnPanel.add(returnButton);

        panel.add(borrowPanel);
        panel.add(returnPanel);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(color);
        label.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 2),
                new EmptyBorder(8, 5, 8, 5)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        return label;
    }

    private void refreshAllData() {
        refreshBooksTable();
        refreshUsersTable();
        refreshTransactionsTable();
        refreshComboboxes();
        updateStats();
    }

    private void refreshBooksTable() {
        booksTableModel.setRowCount(0);
        for (Book book : books) {
            booksTableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getAvailableCopies(),
                    book.getTotalCopies()
            });
        }
    }

    private void refreshUsersTable() {
        usersTableModel.setRowCount(0);
        for (User user : users) {
            usersTableModel.addRow(new Object[]{
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getActiveBorrows()
            });
        }
    }

    private void refreshTransactionsTable() {
        transactionsTableModel.setRowCount(0);
        for (Transaction transaction : transactions) {
            transactionsTableModel.addRow(new Object[]{
                    transaction.getUserName(),
                    transaction.getBookTitle(),
                    transaction.getIssueDate(),
                    transaction.getDueDate(),
                    transaction.getReturnDate(),
                    transaction.getLateFee() > 0 ? "₹" + transaction.getLateFee() : "-"
            });
        }
    }

    private void refreshComboboxes() {
        userComboBox.removeAllItems();
        for (User user : users) {
            userComboBox.addItem(user.getName() + " (" + user.getEmail() + ")");
        }

        bookComboBox.removeAllItems();
        for (Book book : books) {
            if (book.getAvailableCopies() > 0) {
                bookComboBox.addItem(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    private void updateStats() {
        totalBooksLabel.setText("Total Books: " + books.size());
        totalUsersLabel.setText("Total Users: " + users.size());

        long activeBorrows = transactions.stream()
                .filter(t -> t.getReturnDate() == null)
                .count();
        activeBorrowsLabel.setText("Active Borrows: " + activeBorrows);

        double totalRevenue = transactions.stream()
                .mapToDouble(Transaction::getLateFee)
                .sum();
        totalRevenueLabel.setText("Total Revenue: ₹" + totalRevenue);
    }

    private void searchBooks() {
        String query = searchField.getText().toLowerCase();
        booksTableModel.setRowCount(0);

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query) ||
                    book.getAuthor().toLowerCase().contains(query) ||
                    book.getIsbn().toLowerCase().contains(query)) {

                booksTableModel.addRow(new Object[]{
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getAvailableCopies(),
                        book.getTotalCopies()
                });
            }
        }
    }

    private void addBook() {
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookIsbnField.getText().trim();
        String quantityStr = bookQuantityField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new NumberFormatException();
            }

            books.add(new Book(title, author, isbn, quantity));
            refreshAllData();
            clearBookFields();
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser() {
        String name = userNameField.getText().trim();
        String email = userEmailField.getText().trim();
        String phone = userPhoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        users.add(new User(name, email, phone));
        refreshAllData();
        clearUserFields();
        JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void issueBook() {
        if (userComboBox.getSelectedIndex() == -1 || bookComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select both user and book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userSelection = (String) userComboBox.getSelectedItem();
        String bookSelection = (String) bookComboBox.getSelectedItem();

        User user = findUserByName(userSelection.split("\\(")[0].trim());
        Book book = findBookByTitle(bookSelection.split(" by ")[0].trim());

        if (user != null && book != null && book.getAvailableCopies() > 0) {
            transactions.add(new Transaction(user, book));
            book.borrowCopy();
            user.incrementBorrows();
            refreshAllData();
            JOptionPane.showMessageDialog(this,
                    "Book issued successfully!\nDue Date: " +
                            LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void returnBook(JComboBox<String> transactionCombo) {
        // Implementation for returning books would go here
        JOptionPane.showMessageDialog(this, "Return functionality to be implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    private Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    private void clearBookFields() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookIsbnField.setText("");
        bookQuantityField.setText("");
    }

    private void clearUserFields() {
        userNameField.setText("");
        userEmailField.setText("");
        userPhoneField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LibraryManagementSystem system = new LibraryManagementSystem();
            system.setVisible(true);
        });
    }
}

class Book {
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;

    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
}

class User {
    private String name;
    private String email;
    private String phone;
    private int activeBorrows;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.activeBorrows = 0;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getActiveBorrows() { return activeBorrows; }

    public void incrementBorrows() {
        activeBorrows++;
    }

    public void decrementBorrows() {
        if (activeBorrows > 0) {
            activeBorrows--;
        }
    }
}

class Transaction {
    private String userName;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double lateFee;

    public Transaction(User user, Book book) {
        this.userName = user.getName();
        this.bookTitle = book.getTitle();
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusWeeks(2);
        this.returnDate = null;
        this.lateFee = 0.0;
    }

    public String getUserName() { return userName; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getLateFee() { return lateFee; }

    public void returnBook() {
        returnDate = LocalDate.now();
        if (returnDate.isAfter(dueDate)) {
            long daysLate = returnDate.toEpochDay() - dueDate.toEpochDay();
            lateFee = daysLate * 10.0; // ₹10 per day late fee
        }
    }
}
