INSERT INTO price (id, product_code, price_number, depart, price_begin, price_end, price_value )
VALUES (1, '122856', 1, 1, PARSEDATETIME ('01-01-2013 00:00:00','dd-MM-yy hh:mm:ss'),
        PARSEDATETIME ('31-01-2013 23:59:59','dd-MM-yy hh:mm:ss'), 11000),
       (2, '6654', 1, 2, PARSEDATETIME ('01-01-2013 00:00:00','dd-MM-yy hh:mm:ss'),
        PARSEDATETIME ('31-01-2013 00:00:00','dd-MM-yy hh:mm:ss'), 5000);