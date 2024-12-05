-- Create Author table
CREATE TABLE authors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name_en VARCHAR(100) NOT NULL,
    name_bn VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_en VARCHAR(15) NOT NULL,
    phone_bn VARCHAR(15) NOT NULL,
    address_en LONGTEXT NULL,
    address_bn LONGTEXT NULL,
    dob DATE NOT NULL,
    gender_en VARCHAR(55) NOT NULL,
    gender_bn VARCHAR(55) NOT NULL,
    biography_en LONGTEXT NULL,
    biography_bn LONGTEXT NULL,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
