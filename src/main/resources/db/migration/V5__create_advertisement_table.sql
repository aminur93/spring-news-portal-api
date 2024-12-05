-- Create Advertisement table
CREATE TABLE advertisements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title_en VARCHAR(255) NOT NULL,
    title_bn VARCHAR(255) NOT NULL,
    slogan_en VARCHAR(255) NULL,
    slogan_bn VARCHAR(255) NULL,
    description_en LONGTEXT NULL,
    description_bn LONGTEXT NULL,
    company_name_en VARCHAR(255) NOT NULL,
    company_name_bn VARCHAR(255) NOT NULL,
    start_date DATETIME  NULL,
    end_date DATETIME  NULL,
    image VARCHAR(255) NULL,
    status TINYINT(1) DEFAULT 0,
    created_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);