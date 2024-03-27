drop schema stack_overflow;
create schema stack_overflow;

use stack_overflow;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    type ENUM('member', 'moderator') DEFAULT 'member' NOT NULL,
    score DOUBLE DEFAULT 0,
    INDEX (username)
);

CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    creation_datetime DATETIME DEFAULT NOW(),
    picture_url VARCHAR(255) DEFAULT '',
    tags VARCHAR(255),
    upvotes INT DEFAULT 0,
    downvotes INT DEFAULT 0,
    FOREIGN KEY (author) REFERENCES users(username)
);

CREATE TABLE answers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    author VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    image_url VARCHAR(255) DEFAULT '',
    creation_datetime DATETIME DEFAULT NOW(),
    upvotes INT DEFAULT 0,
    downvotes INT DEFAULT 0,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (author) REFERENCES users(username)
);

INSERT INTO users (username, email, password, type) VALUES
('JohnDoe', 'johndoe@textmail.com', 'P@ssw0rd123', 'member'),
('AliceSmith', 'alicesmith@textmail.com', 'Str0ngP@ssword!', 'member'),
('EmilyJones', 'emilyjones@textmail.com', 'SecureP@ssw0rd', 'member'),
('MichaelJohnson', 'michaeljohnson@textmail.com', 'P@ssw0rd!123', 'member'),
('SarahBrown', 'sarahbrown@textmail.com', 'SecretP@ssw0rd', 'member'),
('DavidTaylor', 'davidtaylor@textmail.com', 'P@ssw0rd1234!', 'moderator'),
('LauraMiller', 'lauramiller@textmail.com', 'P@ssw0rd!!!', 'member'),
('DanielWilson', 'danielwilson@textmail.com', 'StrongP@ssw0rd1', 'member'),
('EmmaWhite', 'emmawhite@textmail.com', 'P@ssw0rd!@#', 'member'),
('MatthewClark', 'matthewclark@textmail.com', 'P@ssw0rd$123', 'member');

INSERT INTO questions (author, title, text, tags, creation_datetime, upvotes, downvotes) VALUES
('JohnDoe', 'C++ question', 'What is the difference between ++i and i++ in C++?', 'C++', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),
('AliceSmith', 'Java question', 'What is the difference between equals() and == in Java?', 'Java', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),
('EmilyJones', 'Python question', 'How do you iterate over a list in Python?', 'Python', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5));

INSERT INTO answers (question_id, author, title, text, creation_datetime, upvotes, downvotes) VALUES
(1, 'SarahBrown', 'Useful Answer', 'In C++, ++i is pre-increment, which means the value of i is incremented before it is used in the expression. Whereas, i++ is post-increment, which means the current value of i is used in the expression before it is incremented.', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),
(1, 'MichaelJohnson', 'Bad Answer', 'The difference between ++i and i++ is that they both increment the value of i by 1, but ++i increments it before using the value and i++ increments it after using the value. It really doesn\'t matter which one you use in C++.', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),

(2, 'DanielWilson', 'Useful Answer', 'In Java, equals() is a method used to compare the contents of two objects for equality, whereas == is an operator used to compare the memory addresses of two objects to determine if they refer to the same instance.', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),
(2, 'LauraMiller', 'Bad Answer', 'equals() and == are basically the same thing in Java. You can use either one to compare two objects, and it won\'t make much of a difference.', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),

(3, 'MatthewClark', 'Useful Answer', 'To iterate over a list in Python, you can use a for loop. For example: \nfor item in my_list:\n    print(item)', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5)),
(3, 'EmmaWhite', 'Bad Answer', 'Just use a while loop to iterate over a list in Python. It works better and is more efficient than using a for loop.', NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 5));