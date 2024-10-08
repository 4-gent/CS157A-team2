CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(100),
    PhoneNumber VARCHAR(15),
    Name VARCHAR(100)
);

CREATE TABLE Authentication (
    Username VARCHAR(50),
    Password VARCHAR(50),
    UserID INT,
    PRIMARY KEY (Username, UserID),
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE Employee (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100)
);

CREATE TABLE Catalog (
    CatalogID INT PRIMARY KEY,
    CatalogName VARCHAR(100),
    Description TEXT
);

CREATE TABLE Listing (
    ListingID INT AUTO_INCREMENT PRIMARY KEY,
    ListingName VARCHAR(100)
);

CREATE TABLE Category (
    ID INT PRIMARY KEY,
    Name VARCHAR(100),
    Version VARCHAR(50)
);

CREATE TABLE Books (
    ISBN VARCHAR(13) PRIMARY KEY,
    Name VARCHAR(255),
    Genre VARCHAR(50),
    Publisher VARCHAR(100),
    Author VARCHAR(100)
);

CREATE TABLE Orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    Status VARCHAR(50),
    DateOrdered DATE
);

CREATE TABLE Cart (
    CartID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    NumberBooks INT,
    PendingAmount DECIMAL(10, 2),
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE PaymentDetails (
    PaymentDetailsID INT AUTO_INCREMENT PRIMARY KEY,
    CardNumber VARCHAR(16),
    CVV VARCHAR(4),
    ExpireDate DATE,
    CardType VARCHAR(20),
    CardholderName VARCHAR(100)
);

CREATE TABLE CardValidity (
    PaymentDetailsID INT,
    MM INT,
    YYYY INT,
    PRIMARY KEY (PaymentDetailsID),
    FOREIGN KEY (PaymentDetailsID) REFERENCES PaymentDetails(PaymentDetailsID)
);

CREATE TABLE Manages (
    CatalogID INT,
    EmployeeID INT,
    PRIMARY KEY (CatalogID, EmployeeID),
    FOREIGN KEY (CatalogID) REFERENCES Catalog(CatalogID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

CREATE TABLE Has (
    CatalogID INT,
    ListingID INT,
    PRIMARY KEY (CatalogID, ListingID),
    FOREIGN KEY (CatalogID) REFERENCES Catalog(CatalogID),
    FOREIGN KEY (ListingID) REFERENCES Listing(ListingID)
);

CREATE TABLE Purchases (
    PurchaseID INT,
    UserID INT,
    ListingID INT,
    DateOrdered DATE,
    Status VARCHAR(50),
    PRIMARY KEY (PurchaseID, UserID, ListingID),
    FOREIGN KEY (UserID) REFERENCES User(UserID),
    FOREIGN KEY (ListingID) REFERENCES Listing(ListingID, CatalogID)
);

CREATE TABLE Includes (
    CatalogID INT,
    ISBN VARCHAR(13),
    PRIMARY KEY (CatalogID, ISBN),
    FOREIGN KEY (CatalogID) REFERENCES Catalog(CatalogID),
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN)
);

CREATE TABLE Oversees (
    EmployeeID INT,
    OrderID INT,
    PRIMARY KEY (EmployeeID, OrderID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);


