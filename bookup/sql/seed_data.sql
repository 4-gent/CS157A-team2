-- Authors Table
INSERT INTO Authors (name) VALUES
('J.K. Rowling'),
('George Orwell'),
('J.R.R. Tolkien'),
('Agatha Christie'),
('Stephen King'),
('Jane Austen'),
('Mark Twain'),
('Charles Dickens'),
('Ernest Hemingway'),
('F. Scott Fitzgerald'),
('Leo Tolstoy'),
('Gabriel Garcia Marquez'),
('Oscar Wilde'),
('Haruki Murakami'),
('Isaac Asimov');

-- Genres Table
INSERT INTO Genres (name) VALUES
('Fantasy'),
('Science Fiction'),
('Mystery'),
('Thriller'),
('Romance'),
('Drama'),
('Adventure'),
('Horror'),
('Non-Fiction'),
('Classic'),
('Historical Fiction'),
('Biography'),
('Satire'),
('Young Adult'),
('Dystopian');

-- Users Table
INSERT INTO Users (username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID) VALUES
('user1', 'pass1', 'user1@example.com', '1234567890', 0, 1, 1),
('user2', 'pass2', 'user2@example.com', '1234567891', 0, 2, 2),
('user3', 'pass3', 'user3@example.com', '1234567892', 1, 3, 3),
('user4', 'pass4', 'user4@example.com', '1234567893', 0, 4, 4),
('user5', 'pass5', 'user5@example.com', '1234567894', 0, 5, 5),
('user6', 'pass6', 'user6@example.com', '1234567895', 1, 6, 6),
('user7', 'pass7', 'user7@example.com', '1234567896', 0, 7, 7),
('user8', 'pass8', 'user8@example.com', '1234567897', 0, 8, 8),
('user9', 'pass9', 'user9@example.com', '1234567898', 1, 9, 9),
('user10', 'pass10', 'user10@example.com', '1234567899', 0, 10, 10),
('user11', 'pass11', 'user11@example.com', '1234567800', 0, 11, 11),
('user12', 'pass12', 'user12@example.com', '1234567801', 1, 12, 12),
('user13', 'pass13', 'user13@example.com', '1234567802', 0, 13, 13),
('user14', 'pass14', 'user14@example.com', '1234567803', 0, 14, 14),
('user15', 'pass15', 'user15@example.com', '1234567804', 1, 15, 15);

-- Books Table
INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES
('9780007117116', 'Harry Potter', 2000, 'Bloomsbury', 1),
('9780451524935', '1984', 1949, 'Secker & Warburg', 0),
('9780618640157', 'The Hobbit', 1937, 'Allen & Unwin', 1),
('9780062073488', 'Murder on the Orient Express', 1934, 'Collins Crime Club', 1),
('9781501142970', 'The Shining', 1977, 'Doubleday', 0),
('9780141439518', 'Pride and Prejudice', 1813, 'T. Egerton', 1),
('9780142437179', 'Adventures of Huckleberry Finn', 1884, 'Chatto & Windus', 1),
('9780140439441', 'Great Expectations', 1861, 'Chapman & Hall', 0),
('9780684801223', 'The Old Man and the Sea', 1952, 'Charles Scribner\'s Sons', 1),
('9780743273565', 'The Great Gatsby', 1925, 'Charles Scribner\'s Sons', 0),
('9780679407584', 'War and Peace', 1869, 'The Russian Messenger', 1),
('9780060883287', 'One Hundred Years of Solitude', 1967, 'Harper & Row', 1),
('9780142437223', 'The Picture of Dorian Gray', 1890, 'Ward, Lock & Co.', 0),
('9780307476463', 'Kafka on the Shore', 2002, 'Shinchosha', 1),
('9780553382563', 'Foundation', 1951, 'Gnome Press', 0);

-- InventoryItems Table
INSERT INTO InventoryItems (ISBN, price, qty, description) VALUES
('9780007117116', 15.99, 50, 'A fantasy novel about a young wizard.'),
('9780451524935', 9.99, 70, 'A dystopian novel.'),
('9780618640157', 12.99, 100, 'A fantasy adventure.'),
('9780062073488', 10.99, 40, 'A classic mystery novel.'),
('9781501142970', 14.99, 30, 'A horror novel.'),
('9780141439518', 11.99, 60, 'A romantic classic.'),
('9780142437179', 13.99, 50, 'A humorous tale of a young boy.'),
('9780140439441', 12.49, 20, 'A classic tale of personal growth.'),
('9780684801223', 8.99, 80, 'A story about an old fisherman.'),
('9780743273565', 9.49, 90, 'A classic tale of love and ambition.'),
('9780679407584', 19.99, 10, 'A historical epic.'),
('9780060883287', 14.49, 25, 'A magical realist classic.'),
('9780142437223', 11.49, 15, 'A gothic tale.'),
('9780307476463', 13.49, 35, 'A surreal Japanese novel.'),
('9780553382563', 16.49, 45, 'A science fiction classic.');

-- Addresses Table
INSERT INTO Addresses (street, city, state, zip, country) VALUES
('123 Main St', 'New York', 'NY', '10001', 'USA'),
('456 Elm St', 'Los Angeles', 'CA', '90001', 'USA'),
('789 Oak St', 'Chicago', 'IL', '60601', 'USA'),
('101 Maple St', 'Houston', 'TX', '77001', 'USA'),
('202 Pine St', 'Phoenix', 'AZ', '85001', 'USA'),
('303 Birch St', 'Philadelphia', 'PA', '19019', 'USA'),
('404 Cedar St', 'San Antonio', 'TX', '78201', 'USA'),
('505 Walnut St', 'San Diego', 'CA', '92101', 'USA'),
('606 Ash St', 'Dallas', 'TX', '75201', 'USA'),
('707 Spruce St', 'San Jose', 'CA', '95101', 'USA'),
('808 Willow St', 'Austin', 'TX', '73301', 'USA'),
('909 Hickory St', 'Jacksonville', 'FL', '32201', 'USA'),
('1010 Fir St', 'Fort Worth', 'TX', '76101', 'USA'),
('1111 Palm St', 'Columbus', 'OH', '43004', 'USA'),
('1212 Cypress St', 'Charlotte', 'NC', '28201', 'USA');

-- PaymentDetails Table
INSERT INTO PaymentDetails (username, cardNumber, exp, cardHolderName, cvv, addressID, isDeleted) VALUES
('user1', '1234567812345678', '12/25', 'John Doe', '123', 1, 0),
('user2', '8765432187654321', '01/26', 'Jane Doe', '456', 2, 0),
('user3', '1111222233334444', '11/24', 'Alice Smith', '789', 3, 0),
('user4', '5555666677778888', '06/26', 'Bob Johnson', '101', 4, 0),
('user5', '9999000011112222', '08/25', 'Charlie Brown', '202', 5, 1),
('user6', '1234432112344321', '09/27', 'Emily White', '303', 6, 0),
('user7', '5678567856785678', '03/26', 'Tom Hanks', '404', 7, 0),
('user8', '4444333322221111', '07/25', 'Sarah Connor', '505', 8, 0),
('user10', '1010101010101010', '04/29', 'Clark Kent', '111', 9, 1),
('user11', '1414141414141414', '05/30', 'Bruce Wayne', '121', 10, 0),
('user12', '1616161616161616', '10/25', 'Diana Prince', '212', 11, 0),
('user13', '1818181818181818', '11/24', 'Barry Allen', '323', 12, 1),
('user14', '2020202020202020', '12/23', 'Hal Jordan', '343', 13, 0),
('user15', '2424242424242424', '01/26', 'Arthur Curry', '525', 14, 0),
('user9', '2424242424242479', '01/26', 'Arthur Curry', '525', 15, 0);

-- Orders Table
INSERT INTO Orders (username, addressID, paymentID, orderDate, orderStatus, total) VALUES
('user1', 1, 1, '2024-12-01', 'Delivered', 45.99),
('user2', 2, 2, '2024-12-02', 'Pending', 89.49),
('user3', 3, 3, '2024-12-03', 'Shipped', 39.99),
('user4', 4, 4, '2024-12-04', 'Cancelled', 120.50),
('user5', 5, 5, '2024-12-05', 'Delivered', 70.00),
('user6', 6, 6, '2024-12-06', 'Pending', 35.49),
('user7', 7, 7, '2024-12-07', 'Shipped', 55.99),
('user8', 8, 8, '2024-12-08', 'Delivered', 25.99),
('user9', 9, 9, '2024-12-09', 'Pending', 99.99),
('user10', 10, 10, '2024-12-10', 'Delivered', 49.99),
('user11', 11, 11, '2024-12-11', 'Cancelled', 150.00),
('user12', 12, 12, '2024-12-12', 'Shipped', 60.00),
('user13', 13, 13, '2024-12-13', 'Pending', 45.00),
('user14', 14, 14, '2024-12-14', 'Delivered', 85.00),
('user15', 15, 15, '2024-12-15', 'Cancelled', 25.00);

-- Cart Table
INSERT INTO Cart (username, cartTotal) VALUES
('user1', 45.99),
('user2', 89.49),
('user3', 39.99),
('user4', 120.50),
('user5', 70.00),
('user6', 35.49),
('user7', 55.99),
('user8', 25.99),
('user9', 99.99),
('user10', 49.99),
('user11', 150.00),
('user12', 60.00),
('user13', 45.00),
('user14', 85.00),
('user15', 25.00);

-- Owns Table
INSERT INTO Owns (username, cartID) VALUES
('user1', 1),
('user2', 2),
('user3', 3),
('user4', 4),
('user5', 5),
('user6', 6),
('user7', 7),
('user8', 8),
('user9', 9),
('user10', 10),
('user11', 11),
('user12', 12),
('user13', 13),
('user14', 14),
('user15', 15);

-- Includes Table
INSERT INTO Includes (cartID, inventoryItemID, quantity) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 1),
(4, 4, 3),
(5, 5, 2),
(6, 6, 4),
(7, 7, 1),
(8, 8, 2),
(9, 9, 1),
(10, 10, 3),
(11, 11, 1),
(12, 12, 2),
(13, 13, 1),
(14, 14, 1),
(15, 15, 2);

-- Contains Table
INSERT INTO Contains (orderID, inventoryItemID, addressID, quantity) VALUES
(1, 1, 1, 2),
(2, 2, 2, 1),
(3, 3, 3, 3),
(4, 4, 4, 2),
(5, 5, 5, 4),
(6, 6, 6, 1),
(7, 7, 7, 3),
(8, 8, 8, 2),
(9, 9, 9, 4),
(10, 10, 10, 1),
(11, 11, 11, 2),
(12, 12, 12, 3),
(13, 13, 13, 1),
(14, 14, 14, 2),
(15, 15, 15, 1);

-- FavoriteBooks Table
INSERT INTO FavoriteBooks (username, ISBN) VALUES
('user1', '9780007117116'),
('user2', '9780451524935'),
('user3', '9780618640157'),
('user4', '9780062073488'),
('user5', '9781501142970'),
('user6', '9780141439518'),
('user7', '9780142437179'),
('user8', '9780140439441'),
('user9', '9780684801223'),
('user10', '9780743273565'),
('user11', '9780679407584'),
('user12', '9780060883287'),
('user13', '9780142437223'),
('user14', '9780307476463'),
('user15', '9780553382563');

-- Written Table
INSERT INTO Written (authorID, ISBN) VALUES
(1, '9780007117116'),
(2, '9780451524935'),
(3, '9780618640157'),
(4, '9780062073488'),
(5, '9781501142970'),
(6, '9780141439518'),
(7, '9780142437179'),
(8, '9780140439441'),
(9, '9780684801223'),
(10, '9780743273565'),
(11, '9780679407584'),
(12, '9780060883287'),
(13, '9780142437223'),
(14, '9780307476463'),
(15, '9780553382563');

-- Recommend Table
INSERT INTO Recommend (username, ISBN) VALUES
('user1', '9780007117116'),
('user2', '9780451524935'),
('user3', '9780618640157'),
('user4', '9780062073488'),
('user5', '9781501142970'),
('user6', '9780141439518'),
('user7', '9780142437179'),
('user8', '9780140439441'),
('user9', '9780684801223'),
('user10', '9780743273565'),
('user11', '9780679407584'),
('user12', '9780060883287'),
('user13', '9780142437223'),
('user14', '9780307476463'),
('user15', '9780553382563');

-- OrderModifications Table
INSERT INTO OrderModifications (username, orderID, addressID, modifiedDateTime) VALUES
('user1', 1, 1, '2024-12-02 14:30:00'),
('user2', 2, 2, '2024-12-03 15:00:00'),
('user3', 3, 3, '2024-12-04 16:00:00'),
('user4', 4, 4, '2024-12-05 17:00:00'),
('user5', 5, 5, '2024-12-06 18:00:00'),
('user6', 6, 6, '2024-12-07 19:00:00'),
('user7', 7, 7, '2024-12-08 20:00:00'),
('user8', 8, 8, '2024-12-09 21:00:00'),
('user9', 9, 9, '2024-12-10 22:00:00'),
('user10', 10, 10, '2024-12-11 23:00:00'),
('user11', 11, 11, '2024-12-12 08:00:00'),
('user12', 12, 12, '2024-12-13 09:00:00'),
('user13', 13, 13, '2024-12-14 10:00:00'),
('user14', 14, 14, '2024-12-15 11:00:00'),
('user15', 15, 15, '2024-12-16 12:00:00');

-- InventoryUpdates Table
INSERT INTO InventoryUpdates (username, ISBN, inventoryItemID, updatedDateTime) VALUES
('user1', '9780007117116', 1, '2024-12-01 10:00:00'),
('user2', '9780451524935', 2, '2024-12-02 10:30:00'),
('user3', '9780618640157', 3, '2024-12-03 11:00:00'),
('user4', '9780062073488', 4, '2024-12-04 11:30:00'),
('user5', '9781501142970', 5, '2024-12-05 12:00:00'),
('user6', '9780141439518', 6, '2024-12-06 12:30:00'),
('user7', '9780142437179', 7, '2024-12-07 13:00:00'),
('user8', '9780140439441', 8, '2024-12-08 13:30:00'),
('user9', '9780684801223', 9, '2024-12-09 14:00:00'),
('user10', '9780743273565', 10, '2024-12-10 14:30:00'),
('user11', '9780679407584', 11, '2024-12-11 15:00:00'),
('user12', '9780060883287', 12, '2024-12-12 15:30:00'),
('user13', '9780142437223', 13, '2024-12-13 16:00:00'),
('user14', '9780307476463', 14, '2024-12-14 16:30:00'),
('user15', '9780553382563', 15, '2024-12-15 17:00:00');

-- Belong Table
INSERT INTO Belong (ISBN, genreID) VALUES
('9780007117116', 1),
('9780451524935', 15),
('9780618640157', 1),
('9780062073488', 3),
('9781501142970', 8),
('9780141439518', 5),
('9780142437179', 7),
('9780140439441', 10),
('9780684801223', 10),
('9780743273565', 10),
('9780679407584', 11),
('9780060883287', 11),
('9780142437223', 12),
('9780307476463', 4),
('9780553382563', 2);