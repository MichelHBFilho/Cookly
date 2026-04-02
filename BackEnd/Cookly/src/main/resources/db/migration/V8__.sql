CREATE TABLE followers
(
    person_followed_id    CHAR(36) NOT NULL,
    person_following_id CHAR(36) NOT NULL
);

ALTER TABLE person
    ADD CONSTRAINT uc_person_user UNIQUE (user_id);

ALTER TABLE followers
    ADD CONSTRAINT fk_followers_on_followers FOREIGN KEY (followers_id) REFERENCES person (id);

ALTER TABLE followers
    ADD CONSTRAINT fk_followers_on_person FOREIGN KEY (person_id) REFERENCES person (id);

ALTER TABLE person
    MODIFY birth_day date NULL;