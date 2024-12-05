-- Create News Videos table
CREATE TABLE news_videos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title_en VARCHAR(255) NOT NULL,
    title_bn VARCHAR(255) NULL,
    slogan_en VARCHAR(255) NULL,
    slogan_bn VARCHAR(255) NULL,
    description_en LONGTEXT NULL,
    description_bn LONGTEXT NULL,
    video VARCHAR(255) NULL,
    status TINYINT(1) DEFAULT 0,
    created_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create join table for News Images and Categories
CREATE TABLE news_videos_categories (
    news_video_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (news_video_id, category_id),
    FOREIGN KEY (news_video_id) REFERENCES news_videos(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create join table for News Images and SubCategories
CREATE TABLE news_videos_sub_categories (
    news_video_id INT NOT NULL,
    sub_category_id INT NOT NULL,
    PRIMARY KEY (news_video_id, sub_category_id),
    FOREIGN KEY (news_video_id) REFERENCES news_videos(id),
    FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
);

-- Create join table for News Images and Tags
CREATE TABLE news_videos_tag (
    news_video_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (news_video_id, tag_id),
    FOREIGN KEY (news_video_id) REFERENCES news_videos(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);