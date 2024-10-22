-- Must insert Authors, Genres, and Books first to be able to insert Users otherwise there will be no favoriteAuthorIDs or favoriteGenreIDs to tie to

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

-- Inserting Genres
INSERT INTO Genres (name) VALUES
('Fiction'),
('Fantasy'),
('Mystery'),
('Thriller');

-- Inserting Books
INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES
('978-0451524935', '1984', 1949, 'Secker & Warburg', TRUE),
('978-0747532743', 'Harry Potter and the Philosopher\'s Stone', 1997, 'Bloomsbury', TRUE),
('978-0261102385', 'The Hobbit', 1937, 'George Allen & Unwin', TRUE),
('978-0007119314', 'Murder on the Orient Express', 1934, 'Collins Crime Club', FALSE),
('978-0743273565', 'The Great Gatsby', 1925, 'Charles Scribner\'s Sons', TRUE),
('978-0684830421', 'The Old Man and the Sea', 1952, 'Charles Scribner\'s Sons', FALSE),
('978-0451527999', 'The Adventures of Tom Sawyer', 1876, 'Chatto & Windus', FALSE),
('978-1501160827', 'To Kill a Mockingbird', 1960, 'J.B. Lippincott & Co.', TRUE),
('978-0452284234', 'It', 1986, 'Viking Press', TRUE),
('978-0446310789', 'The Client', 1993, 'Putnam', FALSE);

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


