CREATE TABLE followers
(
    person_followed_id CHAR(36) NOT NULL,
    person_following_id CHAR(36) NOT NULL
);

ALTER TABLE followers
    ADD CONSTRAINT fk_followers_on_followers FOREIGN KEY (person_followed_id) REFERENCES person (id);

ALTER TABLE followers
    ADD CONSTRAINT fk_followers_on_person FOREIGN KEY (person_following_id) REFERENCES person (id);