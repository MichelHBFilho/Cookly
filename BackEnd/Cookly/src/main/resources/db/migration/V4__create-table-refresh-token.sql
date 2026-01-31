CREATE TABLE refresh_token(
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    token VARCHAR(255) UNIQUE,
    expiry_date TIMESTAMP,

    CONSTRAINT fk_token_users
                          FOREIGN KEY (user_id)
                          REFERENCES users(id)
                          ON DELETE CASCADE
);