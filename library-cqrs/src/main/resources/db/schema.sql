CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    national_id VARCHAR(20),
    email VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    student_number VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS staff (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    staff_number VARCHAR(50) UNIQUE NOT NULL,
    role VARCHAR(100) CHECK (role IN ('LIBRARIAN', 'MANAGER', 'ASSISTANT', 'CIRCULATION_DESK', 'SHELF_ORGANIZER')),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS publishers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) UNIQUE,
    publisher_id UUID REFERENCES publishers(id),
    published_year INT,
    category_id UUID REFERENCES categories(id),
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book_authors (
    book_id UUID REFERENCES books(id) ON DELETE CASCADE,
    author_id UUID REFERENCES authors(id),
    PRIMARY KEY (book_id, author_id)
);

CREATE TABLE IF NOT EXISTS floors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50),
    level INT UNIQUE
);

CREATE TABLE IF NOT EXISTS sections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    floor_id UUID REFERENCES floors(id),
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS shelves (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    section_id UUID REFERENCES sections(id),
    code VARCHAR(50),
    capacity INT NOT NULL,
    category_id UUID REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS book_copies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID REFERENCES books(id),
    barcode VARCHAR(100) UNIQUE NOT NULL,
    shelf_id UUID REFERENCES shelves(id),
    status VARCHAR(50) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'BORROWED', 'RESERVED', 'DAMAGED', 'LOST'))
);

CREATE TABLE IF NOT EXISTS loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    staff_id UUID REFERENCES staff(id),
    loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'OVERDUE'))
);

CREATE TABLE IF NOT EXISTS loan_book_copies (
    loan_id UUID REFERENCES loans(id) ON DELETE CASCADE,
    book_copy_id UUID REFERENCES book_copies(id),
    PRIMARY KEY (loan_id, book_copy_id)
);

CREATE TABLE IF NOT EXISTS returns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID REFERENCES loans(id),
    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_by UUID REFERENCES staff(id)
);

CREATE TABLE IF NOT EXISTS fines (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    loan_id UUID REFERENCES loans(id),
    amount NUMERIC(10,2),
    is_paid BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id),
    book_id UUID REFERENCES books(id),
    status VARCHAR(50) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'READY', 'EXPIRED', 'CANCELLED')),
    position INT,
    reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- Exception Logging Table
CREATE TABLE IF NOT EXISTS exception_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    exception_type VARCHAR(255) NOT NULL,
    message TEXT,
    stack_trace TEXT NOT NULL,
    method VARCHAR(10),
    uri TEXT,
    query_params TEXT,
    request_body TEXT,
    user_info VARCHAR(255),
    client_ip VARCHAR(45),
    status_code INTEGER,
    context TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for exception_logs for better query performance
CREATE INDEX IF NOT EXISTS idx_exception_logs_created_at ON exception_logs(created_at);
CREATE INDEX IF NOT EXISTS idx_exception_logs_exception_type ON exception_logs(exception_type);
CREATE INDEX IF NOT EXISTS idx_exception_logs_status_code ON exception_logs(status_code);
CREATE INDEX IF NOT EXISTS idx_exception_logs_user_info ON exception_logs(user_info);
CREATE INDEX IF NOT EXISTS idx_exception_logs_client_ip ON exception_logs(client_ip);
