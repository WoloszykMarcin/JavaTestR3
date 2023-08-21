-- Circle Views
CREATE VIEW circle_area_view AS
SELECT id, (3.141592653589793 * radius * radius) AS area
FROM Shape
WHERE type = 'CIRCLE';

CREATE VIEW circle_perimeter_view AS
SELECT id, (2 * 3.141592653589793 * radius) AS perimeter
FROM Shape
WHERE type = 'CIRCLE';

-- Rectangle Views
CREATE VIEW rectangle_area_view AS
SELECT id, (length * width) AS area
FROM Shape
WHERE type = 'RECTANGLE';

CREATE VIEW rectangle_perimeter_view AS
SELECT id, (2 * (length + width)) AS perimeter
FROM Shape
WHERE type = 'RECTANGLE';

-- Square Views
CREATE VIEW square_area_view AS
SELECT id, (side * side) AS area
FROM Shape
WHERE type = 'SQUARE';

CREATE VIEW square_perimeter_view AS
SELECT id, (4 * side) AS perimeter
FROM Shape
WHERE type = 'SQUARE';
