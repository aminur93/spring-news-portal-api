-- Add column news_image_id to news_images_galleries
ALTER TABLE news_images_galleries
ADD COLUMN news_image_id INT NULL AFTER id;