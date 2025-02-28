CREATE TABLE IF NOT EXISTS profiles (
  profileId INTEGER PRIMARY KEY AUTO_INCREMENT,
  profileName VARCHAR(50) NOT NULL UNIQUE,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
  userId INTEGER PRIMARY KEY AUTO_INCREMENT,
  login VARCHAR(50) NOT NULL UNIQUE,
  hash VARCHAR(255) NOT NULL,
  salt VARCHAR(255) NOT NULL,
  profileId INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (profileId) REFERENCES profiles(profileId) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS categories (
  categoryId INTEGER PRIMARY KEY AUTO_INCREMENT,
  categoryName VARCHAR(100) NOT NULL UNIQUE,
  lastUpdatedBy INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (lastUpdatedBy) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS countries (
  countryId INTEGER PRIMARY KEY AUTO_INCREMENT,
  countryName VARCHAR(100) NOT NULL UNIQUE,
  lastUpdatedBy INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (lastUpdatedBy) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS publishers (
  publisherId INTEGER PRIMARY KEY AUTO_INCREMENT,
  publisherName VARCHAR(255) NOT NULL UNIQUE,
  countryId INTEGER,
  lastUpdatedBy INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (countryId) REFERENCES countries(countryId) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (lastUpdatedBy) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS authors (
  authorId INTEGER PRIMARY KEY AUTO_INCREMENT,
  authorName VARCHAR(100) NOT NULL UNIQUE,
  dateOfBirth DATE,
  countryId INTEGER,
  lastUpdatedBy INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (countryId) REFERENCES countries(countryId) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (lastUpdatedBy) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS books (
  bookId INTEGER PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL UNIQUE,
  releaseDate DATE,
  description TEXT,
  lastUpdatedBy INTEGER,
  isActive BOOLEAN DEFAULT true,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (lastUpdatedBy) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS book_author (
  bookId INTEGER,
  authorId INTEGER,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (bookId, authorId),
  FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (authorId) REFERENCES authors(authorId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS book_category (
  bookId INTEGER,
  categoryId INTEGER,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (bookId, categoryId),
  FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (categoryId) REFERENCES categories(categoryId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS book_publisher (
  bookId INTEGER,
  publisherId INTEGER,
  lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (bookId, publisherId),
  FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (publisherId) REFERENCES publishers(publisherId) ON DELETE CASCADE ON UPDATE CASCADE
);