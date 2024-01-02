/* Creating the recipe table*/
CREATE TABLE IF NOT EXISTS 'recipe' (
    `recipe_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `recipe_name` varchar(100) NOT NULL,
    PRIMARY KEY (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* This delets existing data from the recipe table*/
DELETE FROM `recipe`;
/* Inserting data into the recipe table*/
INSERT INTO `recipe` (`recipe_id`, `recipe_name`) VALUES
        (1, 'Hotdog'),
        (2, 'Pepperoni Pizza'),
        (3, 'Cheeseburger'),
        (4, 'Lasagna'),
        (5, 'French Fries'),
        (6, 'Penne Pasta'),
        (7, 'Lemon Pepper Chicken'),
        (8, 'Spring Roll'),
        (9, 'Spam Fried Rice'),
        (10, 'Thai Garlic Beans')
        (11, 'Balsamic Roasted Brussel Sprouts'),
        (12, 'Creamy Mushroom Steak'),
        (13, 'Bavarian Pretzel Sticks'),
        (14, 'Spiced Sausage Pizza'),
        (15, 'Homemade Chicken Salad');

/* Creating the ingredient table*/
CREATE TABLE IF NOT EXISTS ingredient (
    `ingredient_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `ingredient_name` varchar(100) NOT NULL,
    `recipe_id` int(10) unsigned,
    FOREIGN KEY (`recipe_id`) REFERENCES recipe(`recipe_id`),
    PRIMARY KEY (`ingredient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Deleting existing data formt eh ingredient table*/
DELETE FROM `ingredient`;
/* Inserting data into the ingredient table*/
INSERT INTO `ingredient` (`ingredient_id`, `ingredient_name`, `recipe_id`) VALUES
        (1, 'Bun', 1),
        (2, 'Sausage', 1),
        (3, 'Onion', 1),
        (4, 'Mustard', 1),
        (5, 'Ketchup', 1),
        (6, 'Dough', 2),
        (7, 'Tomato Sauce', 2),
        (8, 'Cheese', 2),
        (9, 'Pepperoni', 2),
        (10, 'Oregano', 2),
        (11, 'Bun', 3),
        (12, 'Beef Patty', 3),
        (13, 'Cheese', 3),
        (14, 'Lettuce', 3),
        (15, 'Tomato', 3),
        (16, 'Ground Beef', 4),
        (17, 'Tomato Sauce', 4),
        (18, 'Ricotta Cheese', 4),
        (19, 'Mozzarella Cheese', 4),
        (20, 'Lasagna Noodles', 4),
        (21, 'Potatoes', 5),
        (22, 'Vegetable Oil', 5),
        (23, 'Salt', 5),
        (24, 'Pepper', 5),
        (25, 'Ketchup', 5),
        (26, 'Penne Pasta', 6),
        (27, 'Olive Oil', 6),
        (28, 'Tomato Sauce', 6),
        (29, 'Garlic', 6),
        (30, 'Parmesan Cheese', 6),
        (31, 'Chicken Breast', 7),
        (32, 'Lemon', 7),
        (33, 'Black Pepper', 7),
        (34, 'Olive Oil', 7),
        (35, 'Garlic', 7),
        (36, 'Rice Paper', 8),
        (37, 'Shrimp', 8),
        (38, 'Rice Vermicelli', 8),
        (39, 'Carrots', 8),
        (40, 'Lettuce', 8),
        (41, 'Rice', 9), 
        (42, 'Span', 9),
        (43, 'Vegetable Oil', 9),
        (44, 'Frozen Peas and Carrots', 9),
        (45, 'Soy Sauce', 9),
        (46, 'Green Beans', 10),
        (47, 'Garlic', 10),
        (48, 'Soy Sauce', 10),
        (49, 'Oyster Sauce', 10),
        (50, 'Chili Paste', 10),
        (51, 'Brussel Sprouts', 11)
        (52, 'Balsamic Vinegar', 11),
        (53, 'Olive Oil', 11),
        (54, 'Garlic', 11),
        (55, 'Salt Kosher', 11),
        (56, 'Black Pepper', 11),
        (57, 'Beef', 12),
        (58, 'Whole Milk', 12),
        (59, 'Mushroom', 12),
        (60, 'Green Pepper', 12),
        (61, 'Yellow Onion', 12),
        (62, 'Wheat Flour', 13),
        (63, 'Yeast', 13),
        (64, 'Palm Oil', 13),
        (65, 'Salt', 13),
        (66, 'Dough', 14),
        (67, 'Tomato Sauce', 14),
        (68, 'Cheese', 14),
        (69, 'Sausage', 14),
        (70, 'Oregano', 14),
        (71, 'Chicken', 15),
        (72, 'Mayonnaise', 15),
        (73, 'Celery', 15),
        (74, 'Onion', 15);

CREATE TABLE IF NOT EXISTS `user` (
  `usr_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `usr_first_name` varchar(100) NOT NULL,
  `usr_last_name` varchar(100) NOT NULL,
  `usr_username` varchar(150) NOT NULL,
  `usr_password` varchar(255) NOT NULL,
  `usr_salt` varchar(100) NOT NULL,
  `usr_avatar` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`usr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELETE FROM `user`;
INSERT INTO `user` (`usr_id`, `usr_first_name`, `usr_last_name`, `usr_username`, `usr_password`, `usr_salt`, `usr_avatar`) VALUES
	(1, 'Stu', 'Dent', 'student', '83d9bdb5e20f3571b087db9aabf190a296741c3e864d7742f35658cfccc1b79c4599aad25084aa9a28c649a50c92244227b3e53e197621301d619d1ea01873c4', '48c8947f69c054a5caa934674ce8881d02bb18fb59d5a63eeaddff735b0e9', 'https://robohash.org/veniamdoloresenim.png?size=64x64&set=set1'),
	(2, 'Gra', 'Duate', 'graduate', 'e289219c34f9a32ebc82393f09719b7f34872de95463242b5ffe8bb4b11a5fe7d454f9f5d082c8207c5d69b220ba06624b4bb15ffa05cc7d7d53c43f9e96da6a', '801e87294783281ae49fc8287a0fd86779b27d7972d3e84f0fa0d826d7cb67dfefc', 'https://robohash.org/nullaautemin.png?size=64x64&set=set1');
    






