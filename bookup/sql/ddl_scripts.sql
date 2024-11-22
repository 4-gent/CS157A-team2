-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS Bookie;

-- Set the default schema to 'Bookie'
USE Bookie;

-- Entities

-- Authors Table
CREATE TABLE IF NOT EXISTS Authors (
    authorID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Genres Table
CREATE TABLE IF NOT EXISTS Genres (
    genreID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Updated Users Table
CREATE TABLE IF NOT EXISTS Users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(15),
    isAdmin BOOLEAN NOT NULL,
    favoriteAuthorID INT DEFAULT NULL,
    favoriteGenreID INT DEFAULT NULL,
    FOREIGN KEY (favoriteAuthorID) REFERENCES Authors(authorID) ON DELETE SET NULL,
    FOREIGN KEY (favoriteGenreID) REFERENCES Genres(genreID) ON DELETE SET NULL
);

-- Books Table
CREATE TABLE IF NOT EXISTS Books (
    ISBN VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT,
    publisher VARCHAR(255),
    isFeatured BOOLEAN
);

-- InventoryItems Table
CREATE TABLE IF NOT EXISTS InventoryItems (
    inventoryItemID INT AUTO_INCREMENT PRIMARY KEY,
    ISBN VARCHAR(13),
    price DECIMAL(10, 2),
    qty INT,
    description TEXT,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE
);

-- Addresses Table
CREATE TABLE IF NOT EXISTS Addresses (
    addressID INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip VARCHAR(10),
    country VARCHAR(100)
);

-- PaymentDetails Table
CREATE TABLE IF NOT EXISTS PaymentDetails (
    paymentID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    cardNumber VARCHAR(16),
    exp VARCHAR(7),
    cardHolderName VARCHAR(255),
    cvv VARCHAR(4),
    addressID INT,
    isDeleted BOOLEAN,
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID) ON DELETE CASCADE
);

-- Orders Table
CREATE TABLE IF NOT EXISTS Orders (
    orderID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    addressID INT,
    paymentID INT, -- Foreign key to PaymentDetails
    orderDate DATE,
    orderStatus VARCHAR(50),
    total DECIMAL(10, 2),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID) ON DELETE CASCADE,
    FOREIGN KEY (paymentID) REFERENCES PaymentDetails(paymentID) ON DELETE CASCADE
);

-- Updated Cart Table with totalPrice
CREATE TABLE IF NOT EXISTS Cart (
    cartID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    cartTotal DECIMAL(10, 2) DEFAULT 0.0,
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE
);

-- Relationships

-- Owns Relationship (Between Users and Cart)
CREATE TABLE IF NOT EXISTS Owns (
    username VARCHAR(255),
    cartID INT,
    PRIMARY KEY (username, cartID),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (cartID) REFERENCES Cart(cartID) ON DELETE CASCADE
);

-- Updated Includes Relationship (Between Cart and InventoryItems)
CREATE TABLE IF NOT EXISTS Includes (
    cartID INT,
    inventoryItemID INT,
    quantity INT NOT NULL,
    PRIMARY KEY (cartID, inventoryItemID),
    FOREIGN KEY (cartID) REFERENCES Cart(cartID) ON DELETE CASCADE,
    FOREIGN KEY (inventoryItemID) REFERENCES InventoryItems(inventoryItemID) ON DELETE CASCADE
);

-- Contains Relationship (Between Orders and InventoryItems)
CREATE TABLE IF NOT EXISTS Contains (
    orderID INT,
    inventoryItemID INT,
    addressID INT,
    quantity INT NOT NULL,
    PRIMARY KEY (orderID, inventoryItemID),
    FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE,
    FOREIGN KEY (inventoryItemID) REFERENCES InventoryItems(inventoryItemID) ON DELETE CASCADE,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID) ON DELETE CASCADE
);

-- FavoriteBooks Relationship (Between Users and Books)
CREATE TABLE IF NOT EXISTS FavoriteBooks (
    username VARCHAR(255),
    ISBN VARCHAR(13),
    PRIMARY KEY (username, ISBN),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE
);

-- Written Relationship (Between Authors and Books)
CREATE TABLE IF NOT EXISTS Written (
    authorID INT,
    ISBN VARCHAR(13),
    PRIMARY KEY (authorID, ISBN),
    FOREIGN KEY (authorID) REFERENCES Authors(authorID) ON DELETE CASCADE,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE
);

-- Recommend Relationship (Between Users and Books)
CREATE TABLE IF NOT EXISTS Recommend (
    username VARCHAR(255),
    ISBN VARCHAR(13),
    PRIMARY KEY (username, ISBN),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE
);

-- Modifies Relationship (Between Users, Orders, and Addresses)
CREATE TABLE IF NOT EXISTS OrderModifications (
    username VARCHAR(255),
    orderID INT,
    addressID INT,
    modifiedDateTime TIMESTAMP,
    PRIMARY KEY (username, orderID, addressID),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE,
    FOREIGN KEY (addressID) REFERENCES Addresses(addressID) ON DELETE CASCADE
);

-- Update Relationship (Between Users, Books, and InventoryItems)
CREATE TABLE IF NOT EXISTS InventoryUpdates (
    username VARCHAR(255),
    ISBN VARCHAR(13),
    inventoryItemID INT,
    updatedDateTime TIMESTAMP,
    PRIMARY KEY (username, ISBN, inventoryItemID),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE,
    FOREIGN KEY (inventoryItemID) REFERENCES InventoryItems(inventoryItemID) ON DELETE CASCADE
);

-- Belong Relationship (Between Books and Genres)
CREATE TABLE IF NOT EXISTS Belong (
    ISBN VARCHAR(13),
    genreID INT,
    PRIMARY KEY (ISBN, genreID),
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN) ON DELETE CASCADE,
    FOREIGN KEY (genreID) REFERENCES Genres(genreID) ON DELETE CASCADE
);