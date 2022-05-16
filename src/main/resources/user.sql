CREATE TABLE IF NOT EXISTS user (
    id int  AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    nick_name VARCHAR(255) UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_authentication BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(255),
    PRIMARY KEY(id),
    INDEX idx1(email)
);
