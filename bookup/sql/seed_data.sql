-- Insert Statements for Users
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('alice.johnson@example.com', '123-456-7890', 'Alice Johnson');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('bob.smith@example.com', '234-567-8901', 'Bob Smith');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('charlie.brown@example.com', '345-678-9012', 'Charlie Brown');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('diana.prince@example.com', '456-789-0123', 'Diana Prince');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('edward.nygma@example.com', '567-890-1234', 'Edward Nygma');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('fiona.apple@example.com', '678-901-2345', 'Fiona Apple');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('george.harrison@example.com', '789-012-3456', 'George Harrison');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('hannah.montana@example.com', '890-123-4567', 'Hannah Montana');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('ivan.drago@example.com', '901-234-5678', 'Ivan Drago');
INSERT INTO User (Email, PhoneNumber, Name) VALUES ('juliet.capulet@example.com', '012-345-6789', 'Juliet Capulet');

-- Insert Statements for Authentication
INSERT INTO Authentication (Username, Password, UserID) VALUES ('alice_j', 'alice123', 1);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('bob_s', 'bobPass', 2);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('charlie_b', 'charliePwd', 3);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('diana_p', 'wonderWoman1', 4);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('edward_n', 'riddler123', 5);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('fiona_a', 'appleFan', 6);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('george_h', 'guitarHero', 7);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('hannah_m', 'bestOfBoth', 8);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('ivan_d', 'ivanTheGreat', 9);
INSERT INTO Authentication (Username, Password, UserID) VALUES ('juliet_c', 'romeoLover', 10);

-- Insert Statements for Employee
INSERT INTO Employee (Name) VALUES ('John Doe');
INSERT INTO Employee (Name) VALUES ('Jane Smith');
INSERT INTO Employee (Name) VALUES ('Michael Johnson');
INSERT INTO Employee (Name) VALUES ('Emily Davis');
INSERT INTO Employee (Name) VALUES ('William Brown');
INSERT INTO Employee (Name) VALUES ('Sophia Martinez');
INSERT INTO Employee (Name) VALUES ('James Wilson');
INSERT INTO Employee (Name) VALUES ('Olivia Garcia');
INSERT INTO Employee (Name) VALUES ('Henry Lee');
INSERT INTO Employee (Name) VALUES ('Charlotte Harris');

-- Insert Statements for Catalog
INSERT INTO Catalog (CatalogName, Description) VALUES ('Fiction', 'A collection of fictional works.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Non-Fiction', 'A collection of non-fictional books.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Science Fiction', 'Books covering futuristic science and technology.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Biographies', 'A collection of biographies of famous personalities.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Fantasy', 'Fantasy novels with magical elements.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Historical', 'Books set in historical time periods.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Mystery & Thriller', 'A collection of mystery and thriller novels.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Children\'s Books', 'Books for children of all ages.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Romance', 'Romantic novels and love stories.');
INSERT INTO Catalog (CatalogName, Description) VALUES ('Self-Help', 'Books to improve oneself and personal development.');

-- Insert Statements for Listing
INSERT INTO Listing (ListingName) VALUES ('The Great Gatsby');
INSERT INTO Listing (ListingName) VALUES ('1984');
INSERT INTO Listing (ListingName) VALUES ('To Kill a Mockingbird');
INSERT INTO Listing (ListingName) VALUES ('Moby Dick');
INSERT INTO Listing (ListingName) VALUES ('War and Peace');
INSERT INTO Listing (ListingName) VALUES ('Pride and Prejudice');
INSERT INTO Listing (ListingName) VALUES ('The Catcher in the Rye');
INSERT INTO Listing (ListingName) VALUES ('The Lord of the Rings');
INSERT INTO Listing (ListingName) VALUES ('The Hobbit');
INSERT INTO Listing (ListingName) VALUES ('Harry Potter and the Sorcerer\'s Stone');

-- Insert Statements for Category
INSERT INTO Category (Name, Version) VALUES ('Fiction', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Non-Fiction', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Science Fiction', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Fantasy', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Biographies', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Romance', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Mystery & Thriller', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Historical', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Children\'s Books', '1.0');
INSERT INTO Category (Name, Version) VALUES ('Self-Help', '1.0');

-- Insert Statements for Books
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780141182636', 'The Great Gatsby', 'Fiction', 'Penguin Books', 'F. Scott Fitzgerald');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780451524935', '1984', 'Dystopian', 'Signet Classics', 'George Orwell');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780061120084', 'To Kill a Mockingbird', 'Fiction', 'Harper Perennial', 'Harper Lee');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9781503280786', 'Moby Dick', 'Adventure', 'CreateSpace Independent', 'Herman Melville');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780199232765', 'War and Peace', 'Historical Fiction', 'Oxford University Press', 'Leo Tolstoy');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780141439518', 'Pride and Prejudice', 'Romance', 'Penguin Classics', 'Jane Austen');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780316769488', 'The Catcher in the Rye', 'Fiction', 'Little, Brown and Company', 'J.D. Salinger');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780544003415', 'The Lord of the Rings', 'Fantasy', 'Houghton Mifflin Harcourt', 'J.R.R. Tolkien');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780261103283', 'The Hobbit', 'Fantasy', 'HarperCollins', 'J.R.R. Tolkien');
INSERT INTO Books (ISBN, Name, Genre, Publisher, Author) VALUES ('9780439708180', 'Harry Potter and the Sorcerer\'s Stone', 'Fantasy', 'Scholastic', 'J.K. Rowling');

-- Insert Statements for Orders
INSERT INTO Orders (Status, DateOrdered) VALUES ('Pending', '2023-09-01');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Shipped', '2023-09-02');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Delivered', '2023-09-03');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Pending', '2023-09-04');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Canceled', '2023-09-05');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Processing', '2023-09-06');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Pending', '2023-09-07');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Delivered', '2023-09-08');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Shipped', '2023-09-09');
INSERT INTO Orders (Status, DateOrdered) VALUES ('Delivered', '2023-09-10');

-- Insert Statements for Cart
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (1, 2, 25.50);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (2, 3, 40.75);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (3, 1, 15.00);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (4, 4, 60.90);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (5, 2, 30.25);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (6, 1, 12.50);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (7, 5, 75.00);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (8, 2, 35.25);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (9, 3, 45.60);
INSERT INTO Cart (UserID, NumberBooks, PendingAmount) VALUES (10, 1, 18.99);

-- Insert Statements for Payment Details
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('1234567812345678', '123', '2025-08-01', 'Visa', 'Alice Johnson');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('8765432187654321', '456', '2024-09-15', 'MasterCard', 'Bob Smith');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('1111222233334444', '789', '2026-12-31', 'American Express', 'Charlie Brown');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('4444333322221111', '234', '2023-11-20', 'Discover', 'Diana Prince');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('5555666677778888', '567', '2027-01-01', 'Visa', 'Edward Nygma');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('9999888877776666', '890', '2025-05-05', 'MasterCard', 'Fiona Apple');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('3333444455556666', '345', '2026-03-25', 'Visa', 'George Harrison');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('7777666655554444', '678', '2023-10-10', 'American Express', 'Hannah Montana');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('8888777766665555', '901', '2024-02-28', 'Discover', 'Ivan Drago');
INSERT INTO PaymentDetails (CardNumber, CVV, ExpireDate, CardType, CardholderName) VALUES ('9999000011112222', '234', '2025-06-30', 'Visa', 'Juliet Capulet');

-- Insert Statements for Manages
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (1, 1);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (2, 2);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (3, 3);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (4, 4);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (5, 5);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (6, 6);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (7, 7);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (8, 8);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (9, 9);
INSERT INTO Manages (CatalogID, EmployeeID) VALUES (10, 10);

-- Insert Statements for Has
INSERT INTO Has (CatalogID, ListingID) VALUES (1, 1);
INSERT INTO Has (CatalogID, ListingID) VALUES (1, 2);
INSERT INTO Has (CatalogID, ListingID) VALUES (2, 3);
INSERT INTO Has (CatalogID, ListingID) VALUES (2, 4);
INSERT INTO Has (CatalogID, ListingID) VALUES (3, 5);
INSERT INTO Has (CatalogID, ListingID) VALUES (3, 6);
INSERT INTO Has (CatalogID, ListingID) VALUES (4, 7);
INSERT INTO Has (CatalogID, ListingID) VALUES (4, 8);
INSERT INTO Has (CatalogID, ListingID) VALUES (5, 9);
INSERT INTO Has (CatalogID, ListingID) VALUES (5, 10);

-- Insert Statements for Purchases
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (1, 1, 1, '2023-09-01', 'Pending');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (2, 2, 2, '2023-09-02', 'Shipped');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (3, 3, 3, '2023-09-03', 'Delivered');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (4, 4, 4, '2023-09-04', 'Pending');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (5, 5, 5, '2023-09-05', 'Canceled');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (6, 6, 6, '2023-09-06', 'Processing');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (7, 7, 7, '2023-09-07', 'Pending');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (8, 8, 8, '2023-09-08', 'Delivered');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (9, 9, 9, '2023-09-09', 'Shipped');
INSERT INTO Purchases (PurchaseID, UserID, ListingID, DateOrdered, Status) VALUES (10, 10, 10, '2023-09-10', 'Delivered');

-- Insert Statements for Includes
INSERT INTO Includes (CatalogID, ISBN) VALUES (1, '9780141182636');
INSERT INTO Includes (CatalogID, ISBN) VALUES (1, '9780451524935');
INSERT INTO Includes (CatalogID, ISBN) VALUES (2, '9780061120084');
INSERT INTO Includes (CatalogID, ISBN) VALUES (2, '9781503280786');
INSERT INTO Includes (CatalogID, ISBN) VALUES (3, '9780199232765');
INSERT INTO Includes (CatalogID, ISBN) VALUES (3, '9780141439518');
INSERT INTO Includes (CatalogID, ISBN) VALUES (4, '9780316769488');
INSERT INTO Includes (CatalogID, ISBN) VALUES (4, '9780544003415');
INSERT INTO Includes (CatalogID, ISBN) VALUES (5, '9780261103283');
INSERT INTO Includes (CatalogID, ISBN) VALUES (5, '9780439708180');

-- Insert Statements for Oversees
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (1, 1);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (2, 2);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (3, 3);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (4, 4);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (5, 5);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (6, 6);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (7, 7);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (8, 8);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (9, 9);
INSERT INTO Oversees (EmployeeID, OrderID) VALUES (10, 10);





