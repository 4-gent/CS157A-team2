-- Inserting Users (2 Admins and 8 Customers)
INSERT INTO Users (username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID) VALUES
('johndoe', 'password123', 'john@example.com', '123-456-7890', FALSE, 1, 1),
('janedoe', 'password123', 'jane@example.com', '123-456-7891', FALSE, 2, 2),
('admin1', 'adminpass', 'admin1@bookstore.com', '555-123-4567', TRUE, NULL, NULL),
('admin2', 'adminpass', 'admin2@bookstore.com', '555-765-4321', TRUE, NULL, NULL),
('customer1', 'custpass1', 'cust1@store.com', '987-654-3210', FALSE, 3, 3),
('customer2', 'custpass2', 'cust2@store.com', '876-543-2109', FALSE, 4, 4),
('customer3', 'custpass3', 'cust3@store.com', '765-432-1098', FALSE, 5, 1),
('customer4', 'custpass4', 'cust4@store.com', '654-321-0987', FALSE, 6, 2),
('customer5', 'custpass5', 'cust5@store.com', '543-210-9876', FALSE, 7, 3),
('customer6', 'custpass6', 'cust6@store.com', '432-109-8765', FALSE, 8, 4);

-- Inserting Authors
INSERT INTO Authors (name) VALUES
('George Orwell'),
('J.K. Rowling'),
('J.R.R. Tolkien'),
('Agatha Christie'),
('Stephen King'),
('Harper Lee'),
('F. Scott Fitzgerald'),
('Ernest Hemingway'),
('Mark Twain'),
('Jane Austen');

