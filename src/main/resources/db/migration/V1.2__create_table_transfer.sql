CREATE TABLE IF NOT EXISTS transfers (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    uuid VARCHAR(300) NOT NULL,
    account_id INT NOT NULL,
    toAccount VARCHAR(50) NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updateAt DATETIME,
    CONSTRAINT account_fk FOREIGN KEY (account_id)
    REFERENCES accounts(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;