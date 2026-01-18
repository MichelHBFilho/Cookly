CREATE TABLE recipe(
    id CHAR(36) PRIMARY KEY,
    name TEXT NOT NULL,
    prepare_time INT NOT NULL
);

CREATE TABLE step_to_prepare(
    id CHAR(36) PRIMARY KEY,
    step_order INTEGER NOT NULL,
    description TEXT NOT NULL,
    recipe_id CHAR(36) NOT NULL,

    CONSTRAINT fk_step_recipe
                          FOREIGN KEY (recipe_id)
                          REFERENCES recipe(id)
                          ON DELETE CASCADE
);

CREATE TABLE person(
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    name TEXT NOT NULL,
    sur_name TEXT NOT NULL,
    birth_day DATE NOT NULL,

    CONSTRAINT fk_person_user
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
);

CREATE TABLE post(
    id CHAR(36) PRIMARY KEY ,
    recipe_id CHAR(36) NOT NULL,
    person_id CHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_post_recipe
                 FOREIGN KEY (recipe_id)
                 REFERENCES recipe(id)
                 ON DELETE CASCADE,

    CONSTRAINT fk_post_person
                 FOREIGN KEY (person_id)
                 REFERENCES person(id)
                 ON DELETE CASCADE
);

CREATE TABLE comment(
    id CHAR(36) PRIMARY KEY,
    post_id CHAR(36) NOT NULL,
    person_id CHAR(36) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comment_post
                    FOREIGN KEY (post_id)
                    REFERENCES post(id)
                    ON DELETE CASCADE,

    CONSTRAINT fk_comment_person
                    FOREIGN KEY (person_id)
                    REFERENCES person(id)
                    ON DELETE CASCADE
);

CREATE TABLE post_like(
    id CHAR(36) PRIMARY KEY,
    post_id CHAR(36) NOT NULL,
    person_id CHAR(36) NOT NULL,

    CONSTRAINT fk_like_post
                 FOREIGN KEY (post_id)
                 REFERENCES post(id)
                 ON DELETE CASCADE,

    CONSTRAINT fk_like_person
                 FOREIGN KEY (person_id)
                 REFERENCES person(id)
                 ON DELETE CASCADE,

    CONSTRAINT uk_like UNIQUE (post_id, person_id)
);