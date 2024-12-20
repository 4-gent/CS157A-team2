-- Inserting Authors
INSERT INTO Authors (authorID, name) VALUES
(1, 'George Orwell'),
(2, 'J.K. Rowling'),
(3, 'J.R.R. Tolkien'),
(4, 'Agatha Christie'),
(5, 'Stephen King'),
(6, 'Harper Lee'),
(7, 'F. Scott Fitzgerald'),
(8, 'Ernest Hemingway'),
(9, 'Mark Twain'),
(10, 'Jane Austen');

-- Inserting Genres
INSERT INTO Genres (genreID, name) VALUES
(1, 'Fiction'),
(2, 'Fantasy'),
(3, 'Mystery'),
(4, 'Thriller');

-- Inserting Books
INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES
('9780451524935', '1984', 1949, 'Secker & Warburg', TRUE),
('9780747532743', 'Harry Potter and the Philosopher\'s Stone', 1997, 'Bloomsbury', TRUE),
('9780261102385', 'The Hobbit', 1937, 'George Allen & Unwin', TRUE),
('9780007119314', 'Murder on the Orient Express', 1934, 'Collins Crime Club', FALSE),
('9780743273565', 'The Great Gatsby', 1925, 'Charles Scribner\'s Sons', TRUE),
('9780684830421', 'The Old Man and the Sea', 1952, 'Charles Scribner\'s Sons', FALSE),
('9780451527999', 'The Adventures of Tom Sawyer', 1876, 'Chatto & Windus', FALSE),
('9781501160827', 'To Kill a Mockingbird', 1960, 'J.B. Lippincott & Co.', TRUE),
('9780452284234', 'It', 1986, 'Viking Press', TRUE),
('9780446310789', 'The Client', 1993, 'Putnam', FALSE);

-- Inserting Inventory Items
INSERT INTO InventoryItems (inventoryItemID, ISBN, price, qty, description) VALUES
(1, '9780451524935', 9.99, 50, 'Classic dystopian novel by George Orwell'),
(2, '9780747532743', 12.99, 100, 'First book in the Harry Potter series'),
(3, '9780261102385', 10.99, 70, 'A timeless adventure by J.R.R. Tolkien'),
(4, '9780007119314', 8.99, 30, 'Agatha Christie mystery novel'),
(5, '9780743273565', 7.99, 40, 'F. Scott Fitzgerald classic'),
(6, '9780684830421', 6.99, 20, 'Short novel by Ernest Hemingway'),
(7, '9780451527999', 5.99, 15, 'Mark Twain\'s adventure story'),
(8, '9781501160827', 11.99, 80, 'Harper Lee\'s Pulitzer-winning novel'),
(9, '9780452284234', 14.99, 60, 'Stephen King\'s horror masterpiece'),
(10, '9780446310789', 9.49, 25, 'Legal thriller by John Grisham');


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

INSERT INTO Addresses (addressID, street, city, state, zip, country) VALUES
(101, '123 Main St', 'Cityville', 'CA', '12345', 'USA'),
(102, '456 Elm St', 'Townsville', 'TX', '67890', 'USA'),
(103, '789 Oak St', 'Villagetown', 'FL', '11223', 'USA'),
(104, '101 Pine St', 'Metrocity', 'NY', '33445', 'USA'),
(105, '202 Maple St', 'Capetown', 'IL', '55667', 'USA'),
(106, '303 Cedar St', 'Rivertown', 'WA', '77889', 'USA'),
(107, '404 Birch St', 'Hilltown', 'NV', '99100', 'USA'),
(108, '505 Spruce St', 'Beachcity', 'SC', '22334', 'USA');

INSERT INTO PaymentDetails (paymentID, username, cardNumber, exp, cardHolderName, cvv, addressID, isDeleted) VALUES
(1, 'johndoe', '1234567812345678', '12/25', 'John Doe', '123', 101, 0),
(2, 'janedoe', '2345678923456789', '11/24', 'Jane Doe', '234', 102, 0),
(3, 'customer1', '3456789034567890', '01/26', 'Customer One', '345', 103, 0),
(4, 'customer2', '4567890145678901', '05/25', 'Customer Two', '456', 104, 0),
(5, 'customer3', '5678901256789012', '07/26', 'Customer Three', '567', 105, 0),
(6, 'customer4', '6789012367890123', '08/29', 'Customer Four', '678', 106, 0),
(7, 'customer5', '7890123478901234', '03/25', 'Customer Five', '789', 107, 0),
(8, 'customer6', '8901234589012345', '08/27', 'Customer Six', '890', 108, 0);