-- Create News Images table
CREATE TABLE news_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title_en VARCHAR(255) NOT NULL,
    title_bn VARCHAR(255) NULL,
    slogan_en VARCHAR(255) NULL,
    slogan_bn VARCHAR(255) NULL,
    description_en LONGTEXT NULL,
    description_bn LONGTEXT NULL,
    image VARCHAR(255) NULL,
    status TINYINT(1) DEFAULT 0,
    created_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create join table for News Images and Categories
CREATE TABLE news_images_categories (
    news_image_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (news_image_id, category_id),
    FOREIGN KEY (news_image_id) REFERENCES news_images(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create join table for News Images and SubCategories
CREATE TABLE news_images_sub_categories (
    news_image_id INT NOT NULL,
    sub_category_id INT NOT NULL,
    PRIMARY KEY (news_image_id, sub_category_id),
    FOREIGN KEY (news_image_id) REFERENCES news_images(id),
    FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
);

-- Create join table for News Images and Tags
CREATE TABLE news_images_tag (
    news_image_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (news_image_id, tag_id),
    FOREIGN KEY (news_image_id) REFERENCES news_images(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);