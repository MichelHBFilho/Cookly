ALTER TABLE post
ADD description TEXT;

ALTER TABLE users
ADD CONSTRAINT uk_users_username UNIQUE (username);