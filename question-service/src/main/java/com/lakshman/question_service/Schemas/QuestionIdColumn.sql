--- Adding Category ID

ALTER TABLE questions
ADD COLUMN category_id INTEGER;

--- Creating Categories Table
CREATE TABLE categories (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

--- Set Category Id as Foreign Key
ALTER TABLE questions
ADD CONSTRAINT fk_category
FOREIGN KEY (category_id)
REFERENCES categories(id);

--- Insert Data to the Category Table

INSERT INTO categories (category, description) VALUES
('PostgreSQL', 'Open source relational database used for scalable applications'),
('SQL', 'Language used to query and manage relational databases'),
('Microservices', 'Architecture that splits applications into small independent services'),
('Spring Boot', 'Java framework for building production ready backend applications quickly'),
('Java', 'Object oriented programming language widely used for enterprise development');

--- Fetch the data from categories

select * from categories;

--- Updating Category Id using Categories table

UPDATE questions q
SET category_id = c.id
FROM categories c
WHERE q.category = c.category;

--- Check Null values in category_id field

SELECT *
FROM questions
WHERE category_id IS NULL;

--- Drop Category Column

ALTER TABLE questions
DROP COLUMN category;
