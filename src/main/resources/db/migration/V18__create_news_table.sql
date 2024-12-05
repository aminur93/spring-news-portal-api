CREATE TABLE news (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    country_id INT,
    city_id INT,
    division_id INT,
    district_id INT,
    upzilla_id INT,
    heading_en VARCHAR(100) NULL,
    heading_bn VARCHAR(100) NULL,
    title_en VARCHAR(100) NULL,
    title_bn VARCHAR(100) NULL,
    description_en LONGTEXT NULL,
    description_bn LONGTEXT NULL,
    additional_description_en LONGTEXT NULL,
    additional_description_bn LONGTEXT NULL,
    cover_image VARCHAR(100) NULL,
    image VARCHAR(100) NULL,
    date DATE,
    source_en VARCHAR(100) NULL,
    source_bn VARCHAR(100) NULL,
    news_column INT,
    is_popular TINYINT(1) DEFAULT 0,
    is_breaking TINYINT(1) DEFAULT 0,
    is_opinion TINYINT(1) DEFAULT 0,
    is_for_you TINYINT(1) DEFAULT 0,
    is_discussed TINYINT(1) DEFAULT 0,
    is_good_news TINYINT(1) DEFAULT 0,
    is_bd TINYINT(1) DEFAULT 0,
    is_world TINYINT(1) DEFAULT 0,
    is_top TINYINT(1) DEFAULT 0,
    is_middle TINYINT(1) DEFAULT 0,
    is_bottom TINYINT(1) DEFAULT 0,
    is_featured TINYINT(1) DEFAULT 0,
    is_trending TINYINT(1) DEFAULT 0,
    is_fashion TINYINT(1) DEFAULT 0,
    is_cartoon TINYINT(1) DEFAULT 0,
    count INT NULL DEFAULT 0,
    status TINYINT DEFAULT 0,
    created_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create join table for News Images and SubCategories
CREATE TABLE news_sub_categories (
    news_id INT NOT NULL,
    sub_category_id INT NOT NULL,
    PRIMARY KEY (news_id, sub_category_id),
    FOREIGN KEY (news_id) REFERENCES news(id),
    FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
);

-- Create join table for News Images and Tags
CREATE TABLE news_tags (
    news_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (news_id, tag_id),
    FOREIGN KEY (news_id) REFERENCES news_images(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);