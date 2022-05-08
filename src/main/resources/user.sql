CREATE TABLE IF NOT EXISTS user (
    id int NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nick_name VARCHAR(255) UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    INDEX idx1(email)
);

INSERT INTO user(email, password) VALUES ("asdf@asdf.com", "password");
INSERT INTO user(email, password) VALUES ("zxcv@asdf.com", "password");