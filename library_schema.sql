--------------------------------------------------
-- USERS (KVKK: hard delete yapılabilir)
--------------------------------------------------
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Normalde kod üzerinden uuid oluşturacağız
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    national_id VARCHAR(20),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------
-- STUDENT & STAFF
--------------------------------------------------
CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    student_number VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE staff (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    staff_number VARCHAR(50) UNIQUE NOT NULL,
    role VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);

--------------------------------------------------
-- PUBLISHER & AUTHOR
--------------------------------------------------
CREATE TABLE publishers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
);

--------------------------------------------------
-- CATEGORY
--------------------------------------------------
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL
);

--------------------------------------------------
-- BOOK
--------------------------------------------------
CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) UNIQUE,
    publisher_id UUID REFERENCES publishers(id),
    published_year INT,
    category_id UUID REFERENCES categories(id),
    deleted_at TIMESTAMP
);

--------------------------------------------------
-- BOOK AUTHORS (M:N)
--------------------------------------------------
CREATE TABLE book_authors (
    book_id UUID REFERENCES books(id) ON DELETE CASCADE,
    author_id UUID REFERENCES authors(id),
    PRIMARY KEY (book_id, author_id)
);

--------------------------------------------------
-- LOCATION STRUCTURE
--------------------------------------------------
CREATE TABLE floors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50),
    level INT UNIQUE
);

CREATE TABLE sections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    floor_id UUID REFERENCES floors(id),
    name VARCHAR(100)
);

CREATE TABLE shelves (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    section_id UUID REFERENCES sections(id),
    code VARCHAR(50),
    capacity INT NOT NULL,
    category_id UUID REFERENCES categories(id)
);

--------------------------------------------------
-- BOOK COPIES
--------------------------------------------------
CREATE TABLE book_copies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID REFERENCES books(id),
    barcode VARCHAR(100) UNIQUE NOT NULL,
    shelf_id UUID REFERENCES shelves(id),
    status VARCHAR(50) DEFAULT 'AVAILABLE'
);

--------------------------------------------------
-- LOAN SYSTEM
--------------------------------------------------
CREATE TABLE loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    staff_id UUID REFERENCES staff(id),
    loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE'
);

CREATE TABLE loan_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID REFERENCES loans(id) ON DELETE CASCADE,
    book_copy_id UUID REFERENCES book_copies(id)
);

--------------------------------------------------
-- RETURN SYSTEM
--------------------------------------------------
CREATE TABLE returns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID REFERENCES loans(id),
    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_by UUID REFERENCES staff(id)
);

--------------------------------------------------
-- FINE SYSTEM
--------------------------------------------------
CREATE TABLE fines (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    loan_id UUID REFERENCES loans(id),
    amount NUMERIC(10,2),
    is_paid BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------
-- RESERVATION
--------------------------------------------------
CREATE TABLE reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    book_id UUID REFERENCES books(id),
    status VARCHAR(50) DEFAULT 'PENDING',
    position INT,
    reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);
