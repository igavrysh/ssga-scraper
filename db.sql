CREATE DATABASE ssga_scraper CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'web'@'localhost';

GRANT ALL ON ssga_scraper.* TO 'web'@'localhost';

ALTER USER 'web'@'localhost' IDENTIFIED BY 'pass';