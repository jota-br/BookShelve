INSERT INTO profiles (profileName) VALUES ('Admin');
INSERT INTO profiles (profileName, isActive) VALUES ('Manager', 0);
INSERT INTO profiles (profileName) VALUES ('User');

INSERT INTO users (login, hash, salt, profileId, isActive) VALUES ('user1', 'hash', 'salt', 1, 1);
INSERT INTO users (login, hash, salt, profileId, isActive) VALUES ('user2', 'hash', 'salt', 2, 1);
INSERT INTO users (login, hash, salt, profileId) VALUES ('user3', 'hash', 'salt', 3);

INSERT INTO countries (countryName, lastUpdatedBy, isActive) VALUES ('United Kingdom', 2, true);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('Russia', 1);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('United States', 1);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('Argentina', 1);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('India', 1);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('Colombia', 1);
INSERT INTO countries (countryName, lastUpdatedBy) VALUES ('South Africa', 1);

INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Jane Austen', '1775-12-16', 1, 2);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Leo Tolstoy', '1828-09-09', 2, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('F. Scott Fitzgerald', '1896-09-24', 3, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Harper Lee', '1926-04-28', 3, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('George Orwell', '1903-06-25', 5, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Herman Melville', '1819-08-01', 3, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('J.D. Salinger', '1919-01-01', 3, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Gabriel García Márquez', '1927-03-06', 6, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('J.R.R. Tolkien', '1892-01-03', 7, 1);
INSERT INTO authors (authorName, dateOfBirth, countryId, lastUpdatedBy) VALUES ('Fyodor Dostoevsky', '1821-11-11', 2, 1);

INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Romance', 2);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Historical Fiction', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Fiction', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Dystopian', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Adventure', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Magical Realism', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Fantasy', 1);
INSERT INTO categories (categoryName, lastUpdatedBy) VALUES ('Psychological Fiction', 1);

INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Thomas Egerton, London', 1, 2);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('The Russian Messenger' , 2, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Charles Scribners Sons', 3, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('J.B. Lippincott & Co.', 3, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Secker & Warburg', 1, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Richard Bentley', 1, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Little, Brown and Company', 3, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('Editorial Sudamericana', 4, 1);
INSERT INTO publishers (publisherName, countryId, lastUpdatedBy) VALUES ('George Allen & Unwin', 1, 1);


INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('Pride and Prejudice', '1813-01-28', 'description', 2);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('War and Peace', '1869-01-01', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('The Great Gatsby', '1925-04-10', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('To Kill a Mockingbird', '1960-07-11', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('1984', '1949-06-08', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('Moby Dick', '1851-10-18', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('The Catcher in the Rye', '1951-07-16', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('One Hundred Years of Solitude', '1967-05-30', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('The Hobbit', '1937-09-21', 'description', 1);
INSERT INTO books (title, releaseDate, description, lastUpdatedBy) VALUES ('Crime and Punishment', '1866-01-01', 'description', 1);

INSERT INTO book_author (bookId, authorId) VALUES (1, 1);
INSERT INTO book_author (bookId, authorId) VALUES (2, 2);
INSERT INTO book_author (bookId, authorId) VALUES (3, 3);
INSERT INTO book_author (bookId, authorId) VALUES (4, 4);
INSERT INTO book_author (bookId, authorId) VALUES (5, 5);
INSERT INTO book_author (bookId, authorId) VALUES (6, 6);
INSERT INTO book_author (bookId, authorId) VALUES (7, 7);
INSERT INTO book_author (bookId, authorId) VALUES (8, 8);
INSERT INTO book_author (bookId, authorId) VALUES (9, 9);
INSERT INTO book_author (bookId, authorId) VALUES (10, 10);

INSERT INTO book_category (bookId, categoryId) VALUES (1, 1);
INSERT INTO book_category (bookId, categoryId) VALUES (2, 2);
INSERT INTO book_category (bookId, categoryId) VALUES (3, 3);
INSERT INTO book_category (bookId, categoryId) VALUES (4, 3);
INSERT INTO book_category (bookId, categoryId) VALUES (5, 4);
INSERT INTO book_category (bookId, categoryId) VALUES (6, 5);
INSERT INTO book_category (bookId, categoryId) VALUES (7, 3);
INSERT INTO book_category (bookId, categoryId) VALUES (8, 6);
INSERT INTO book_category (bookId, categoryId) VALUES (9, 7);
INSERT INTO book_category (bookId, categoryId) VALUES (10, 8);

INSERT INTO book_publisher (bookId, publisherId) VALUES (1, 1);
INSERT INTO book_publisher (bookId, publisherId) VALUES (2, 2);
INSERT INTO book_publisher (bookId, publisherId) VALUES (3, 3);
INSERT INTO book_publisher (bookId, publisherId) VALUES (4, 4);
INSERT INTO book_publisher (bookId, publisherId) VALUES (5, 5);
INSERT INTO book_publisher (bookId, publisherId) VALUES (6, 6);
INSERT INTO book_publisher (bookId, publisherId) VALUES (7, 7);
INSERT INTO book_publisher (bookId, publisherId) VALUES (8, 8);
INSERT INTO book_publisher (bookId, publisherId) VALUES (9, 9);
INSERT INTO book_publisher (bookId, publisherId) VALUES (10, 2);