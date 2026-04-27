INSERT INTO categories (name)
SELECT x.name
FROM (VALUES
    ('Science Fiction'),
    ('Software Engineering'),
    ('Databases'),
    ('History'),
    ('Psychology'),
    ('Literature')
) AS x(name)
WHERE NOT EXISTS (SELECT 1 FROM categories);

INSERT INTO publishers (name)
SELECT x.name
FROM (VALUES
    ('Penguin Random House'),
    ('O''Reilly Media'),
    ('Addison-Wesley'),
    ('Manning'),
    ('No Starch Press'),
    ('HarperCollins'),
    ('Springer'),
    ('MIT Press'),
    ('McGraw-Hill'),
    ('Markus Winand'),
    ('Beacon Press'),
    ('Prentice Hall')
) AS x(name)
WHERE NOT EXISTS (SELECT 1 FROM publishers);

INSERT INTO authors (first_name, last_name)
SELECT x.first_name, x.last_name
FROM (VALUES
    ('Robert', 'Martin'),
    ('Martin', 'Fowler'),
    ('Eric', 'Evans'),
    ('Kent', 'Beck'),
    ('Andrew', 'Hunt'),
    ('David', 'Thomas'),
    ('J. R. R.', 'Tolkien'),
    ('Isaac', 'Asimov'),
    ('Frank', 'Herbert'),
    ('George', 'Orwell'),
    ('Fyodor', 'Dostoevsky'),
    ('Carl', 'Jung'),
    ('Viktor', 'Frankl'),
    ('Thomas', 'H. Cormen'),
    ('Robert', 'Sedgewick')
) AS x(first_name, last_name)
WHERE NOT EXISTS (SELECT 1 FROM authors);

INSERT INTO floors (name, level)
SELECT 'Floor ' || gs, gs
FROM generate_series(1, 3) AS gs
WHERE NOT EXISTS (SELECT 1 FROM floors);

INSERT INTO sections (floor_id, name)
SELECT f.id, s.section_name
FROM floors f
CROSS JOIN (VALUES ('A'), ('B'), ('C'), ('D')) AS s(section_name)
WHERE NOT EXISTS (SELECT 1 FROM sections);

WITH ordered_sections AS (
    SELECT s.id,
           f.level,
           s.name,
           ROW_NUMBER() OVER (ORDER BY f.level, s.name) AS section_rn
    FROM sections s
    JOIN floors f ON f.id = s.floor_id
),
ordered_categories AS (
    SELECT c.id,
           ROW_NUMBER() OVER (ORDER BY c.name) AS category_rn,
           COUNT(*) OVER () AS category_count
    FROM categories c
)
INSERT INTO shelves (section_id, code, capacity, category_id)
SELECT os.id,
       FORMAT('F%s-%s-S%02s', os.level, os.name, gs.n),
       20,
       (
           SELECT oc.id
           FROM ordered_categories oc
           WHERE oc.category_rn = ((os.section_rn + gs.n - 2) % (SELECT MAX(category_count) FROM ordered_categories)) + 1
       )
FROM ordered_sections os
CROSS JOIN generate_series(1, 10) AS gs(n)
WHERE NOT EXISTS (SELECT 1 FROM shelves);

INSERT INTO books (title, isbn, publisher_id, published_year, category_id, deleted_at)
SELECT b.title, b.isbn, p.id, b.published_year, c.id, NULL
FROM (VALUES
    ('Clean Code', '9780132350884', 2008, 'Addison-Wesley', 'Software Engineering'),
    ('Refactoring', '9780201485677', 1999, 'Addison-Wesley', 'Software Engineering'),
    ('Domain-Driven Design', '9780321125217', 2003, 'Addison-Wesley', 'Software Engineering'),
    ('The Pragmatic Programmer', '9780201616224', 1999, 'Addison-Wesley', 'Software Engineering'),
    ('Design Patterns', '9780201633610', 1994, 'Addison-Wesley', 'Software Engineering'),
    ('The Mythical Man-Month', '9780201835953', 1995, 'Addison-Wesley', 'Software Engineering'),
    ('Database System Concepts', '9780078022159', 2010, 'McGraw-Hill', 'Databases'),
    ('SQL Performance Explained', '9783950307825', 2012, 'Markus Winand', 'Databases'),
    ('Learning SQL', '9781492057611', 2020, 'O''Reilly Media', 'Databases'),
    ('Introduction to Algorithms', '9780262046305', 2022, 'MIT Press', 'Software Engineering'),
    ('The Lord of the Rings', '9780618640157', 1954, 'HarperCollins', 'Literature'),
    ('Foundation', '9780553293357', 1951, 'Penguin Random House', 'Science Fiction'),
    ('Dune', '9780441172719', 1965, 'Penguin Random House', 'Science Fiction'),
    ('1984', '9780451524935', 1949, 'Penguin Random House', 'Literature'),
    ('Crime and Punishment', '9780143058144', 1866, 'Penguin Random House', 'Literature'),
    ('Man and His Symbols', '9780440351832', 1964, 'Penguin Random House', 'Psychology'),
    ('Man''s Search for Meaning', '9780807014295', 1946, 'Beacon Press', 'Psychology'),
    ('The Clean Coder', '9780137081073', 2011, 'Prentice Hall', 'Software Engineering'),
    ('Working Effectively with Legacy Code', '9780131177055', 2004, 'Prentice Hall', 'Software Engineering'),
    ('Patterns of Enterprise Application Architecture', '9780321127426', 2002, 'Addison-Wesley', 'Software Engineering')
) AS b(title, isbn, published_year, publisher_name, category_name)
JOIN categories c ON c.name = b.category_name
LEFT JOIN publishers p ON p.name = b.publisher_name
WHERE NOT EXISTS (SELECT 1 FROM books);

INSERT INTO book_authors (book_id, author_id)
SELECT bk.id, a.id
FROM (VALUES
    ('Clean Code', 'Robert', 'Martin'),
    ('Refactoring', 'Martin', 'Fowler'),
    ('Domain-Driven Design', 'Eric', 'Evans'),
    ('The Pragmatic Programmer', 'Andrew', 'Hunt'),
    ('The Pragmatic Programmer', 'David', 'Thomas'),
    ('Design Patterns', 'Robert', 'Martin'),
    ('The Mythical Man-Month', 'Martin', 'Fowler'),
    ('Learning SQL', 'Robert', 'Sedgewick'),
    ('Introduction to Algorithms', 'Thomas', 'H. Cormen'),
    ('Introduction to Algorithms', 'Robert', 'Sedgewick'),
    ('The Lord of the Rings', 'J. R. R.', 'Tolkien'),
    ('Foundation', 'Isaac', 'Asimov'),
    ('Dune', 'Frank', 'Herbert'),
    ('1984', 'George', 'Orwell'),
    ('Crime and Punishment', 'Fyodor', 'Dostoevsky'),
    ('Man and His Symbols', 'Carl', 'Jung'),
    ('Man''s Search for Meaning', 'Viktor', 'Frankl'),
    ('The Clean Coder', 'Robert', 'Martin'),
    ('Working Effectively with Legacy Code', 'Michael', 'Feathers'),
    ('Patterns of Enterprise Application Architecture', 'Martin', 'Fowler')
) AS m(title, first_name, last_name)
JOIN books bk ON bk.title = m.title
JOIN authors a ON a.first_name = m.first_name AND a.last_name = m.last_name
ON CONFLICT DO NOTHING;

WITH missing_author AS (
    SELECT 1 AS marker
    WHERE NOT EXISTS (
        SELECT 1 FROM authors a WHERE a.first_name = 'Michael' AND a.last_name = 'Feathers'
    )
)
INSERT INTO authors (first_name, last_name)
SELECT 'Michael', 'Feathers'
FROM missing_author;

INSERT INTO book_authors (book_id, author_id)
SELECT bk.id, a.id
FROM books bk
JOIN authors a ON a.first_name = 'Michael' AND a.last_name = 'Feathers'
WHERE bk.title = 'Working Effectively with Legacy Code'
ON CONFLICT DO NOTHING;

WITH books_ordered AS (
    SELECT b.id,
           b.isbn,
           ROW_NUMBER() OVER (ORDER BY b.title) AS book_rn
    FROM books b
),
shelves_ordered AS (
    SELECT s.id,
           ROW_NUMBER() OVER (ORDER BY s.code) AS shelf_rn,
           COUNT(*) OVER () AS shelf_count
    FROM shelves s
),
shelf_meta AS (
    SELECT MAX(shelf_count) AS total_shelf_count
    FROM shelves_ordered
)
INSERT INTO book_copies (book_id, barcode, shelf_id, status)
SELECT bo.id,
       FORMAT('BC-%s-%02s', REPLACE(bo.isbn, '-', ''), gs.n),
       so.id,
       'AVAILABLE'
FROM books_ordered bo
CROSS JOIN generate_series(1, 10) AS gs(n)
JOIN shelf_meta sm ON TRUE
JOIN shelves_ordered so ON so.shelf_rn = ((bo.book_rn * 10 + gs.n - 1) % sm.total_shelf_count) + 1
WHERE NOT EXISTS (SELECT 1 FROM book_copies);

INSERT INTO users (username, password_hash, national_id, email, first_name, last_name)
SELECT x.username, x.password_hash, x.national_id, x.email, x.first_name, x.last_name
FROM (VALUES
    ('student1', '$2a$10$seedhash', '11111111111', 'student1@library.local', 'Ayse', 'Demir'),
    ('student2', '$2a$10$seedhash', '22222222222', 'student2@library.local', 'Mehmet', 'Kaya'),
    ('student3', '$2a$10$seedhash', '33333333333', 'student3@library.local', 'Elif', 'Aydin'),
    ('student4', '$2a$10$seedhash', '44444444444', 'student4@library.local', 'Can', 'Yildiz'),
    ('student5', '$2a$10$seedhash', '55555555555', 'student5@library.local', 'Deniz', 'Arslan'),
    ('student6', '$2a$10$seedhash', '66666666666', 'student6@library.local', 'Merve', 'Sahin'),
    ('staff1', '$2a$10$seedhash', '77777777777', 'staff1@library.local', 'Burak', 'Celik'),
    ('staff2', '$2a$10$seedhash', '88888888888', 'staff2@library.local', 'Irem', 'Kilic'),
    ('staff3', '$2a$10$seedhash', '99999999999', 'staff3@library.local', 'Okan', 'Kurt'),
    ('admin1', '$2a$10$seedhash', '10101010101', 'admin1@library.local', 'Selin', 'Acar'),
    ('admin2', '$2a$10$seedhash', '20202020202', 'admin2@library.local', 'Ege', 'Tas'),
    ('admin3', '$2a$10$seedhash', '30303030303', 'admin3@library.local', 'Seda', 'Er')
) AS x(username, password_hash, national_id, email, first_name, last_name)
WHERE NOT EXISTS (SELECT 1 FROM users);

INSERT INTO students (user_id, student_number, is_active)
SELECT u.id, x.student_number, x.is_active
FROM (VALUES
    ('student1', 'STU-1001', TRUE),
    ('student2', 'STU-1002', TRUE),
    ('student3', 'STU-1003', TRUE),
    ('student4', 'STU-1004', TRUE),
    ('student5', 'STU-1005', TRUE),
    ('student6', 'STU-1006', TRUE)
) AS x(username, student_number, is_active)
JOIN users u ON u.username = x.username
WHERE NOT EXISTS (SELECT 1 FROM students);

INSERT INTO staff (user_id, staff_number, role, is_active)
SELECT u.id, x.staff_number, x.role, x.is_active
FROM (VALUES
    ('student1', 'STF-9001', 'CIRCULATION_DESK', TRUE),
    ('student2', 'STF-9002', 'SHELF_ORGANIZER', TRUE),
    ('student3', 'STF-9003', 'ASSISTANT', TRUE),
    ('staff1', 'STF-1001', 'LIBRARIAN', TRUE),
    ('staff2', 'STF-1002', 'MANAGER', TRUE),
    ('staff3', 'STF-1003', 'CIRCULATION_DESK', TRUE)
) AS x(username, staff_number, role, is_active)
JOIN users u ON u.username = x.username
WHERE NOT EXISTS (SELECT 1 FROM staff);

INSERT INTO loans (student_id, staff_id, loan_date, due_date, status)
SELECT s.id, st.id, NOW() - INTERVAL '2 days', NOW() + INTERVAL '12 days', 'ACTIVE'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student4'
JOIN staff st ON st.staff_number = 'STF-1001'
WHERE NOT EXISTS (SELECT 1 FROM loans)
UNION ALL
SELECT s.id, st.id, NOW() - INTERVAL '20 days', NOW() - INTERVAL '5 days', 'COMPLETED'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student5'
JOIN staff st ON st.staff_number = 'STF-1003'
WHERE NOT EXISTS (SELECT 1 FROM loans)
UNION ALL
SELECT s.id, st.id, NOW() - INTERVAL '15 days', NOW() - INTERVAL '2 days', 'OVERDUE'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student6'
JOIN staff st ON st.staff_number = 'STF-1001'
WHERE NOT EXISTS (SELECT 1 FROM loans);

WITH l AS (
    SELECT id, status, ROW_NUMBER() OVER (ORDER BY loan_date ASC, id) AS rn
    FROM loans
),
bc AS (
    SELECT id, ROW_NUMBER() OVER (ORDER BY barcode ASC) AS rn
    FROM book_copies
)
INSERT INTO loan_book_copies (loan_id, book_copy_id)
SELECT l.id, bc.id
FROM l
JOIN bc ON (
       (l.rn = 1 AND bc.rn IN (1, 2))
    OR (l.rn = 2 AND bc.rn IN (3))
    OR (l.rn = 3 AND bc.rn IN (4, 5))
)
WHERE NOT EXISTS (SELECT 1 FROM loan_book_copies);

UPDATE book_copies bc
SET status = 'BORROWED'
WHERE bc.id IN (
    SELECT lbc.book_copy_id
    FROM loan_book_copies lbc
    JOIN loans l ON l.id = lbc.loan_id
    WHERE l.status IN ('ACTIVE', 'OVERDUE')
);

UPDATE book_copies bc
SET status = 'AVAILABLE'
WHERE bc.id IN (
    SELECT lbc.book_copy_id
    FROM loan_book_copies lbc
    JOIN loans l ON l.id = lbc.loan_id
    WHERE l.status = 'COMPLETED'
);

INSERT INTO returns (loan_id, return_date, processed_by)
SELECT l.id, NOW() - INTERVAL '1 day', st.id
FROM loans l
JOIN staff st ON st.staff_number = 'STF-1001'
WHERE l.status = 'COMPLETED'
  AND NOT EXISTS (SELECT 1 FROM returns);

INSERT INTO fines (student_id, loan_id, amount, is_paid, created_at)
SELECT l.student_id, l.id, 75.00, FALSE, NOW() - INTERVAL '1 day'
FROM loans l
WHERE l.status = 'OVERDUE'
  AND NOT EXISTS (SELECT 1 FROM fines);

INSERT INTO reservations (student_id, book_id, status, position, reserved_at, expires_at)
SELECT s.id, b.id, 'PENDING', 1, NOW() - INTERVAL '2 hours', NOW() + INTERVAL '2 days'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student1'
JOIN books b ON b.title = 'Dune'
WHERE NOT EXISTS (SELECT 1 FROM reservations)
UNION ALL
SELECT s.id, b.id, 'PENDING', 2, NOW() - INTERVAL '1 hours', NOW() + INTERVAL '2 days'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student2'
JOIN books b ON b.title = 'Dune'
WHERE NOT EXISTS (SELECT 1 FROM reservations)
UNION ALL
SELECT s.id, b.id, 'READY', 1, NOW() - INTERVAL '8 hours', NOW() + INTERVAL '1 day'
FROM students s
JOIN users su ON su.id = s.user_id AND su.username = 'student3'
JOIN books b ON b.title = 'Foundation'
WHERE NOT EXISTS (SELECT 1 FROM reservations);
