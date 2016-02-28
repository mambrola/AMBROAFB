use ambro_soft_afb;

/*CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `invoices_to_java` AS
    SELECT 
        `i`.`rec_id` AS `rec_id`,
        `i`.`invoice_number` AS `invoice_number`,
        `i`.`client_id` AS `client_id`,
        `i`.`begin_date` AS `begin_date`,
        `i`.`end_date` AS `end_date`,
        `c`.`first_name` AS `first_name`,
        `c`.`last_name` AS `last_name`,
        `c`.`email` AS `email`,
        GROUP_CONCAT(`ip`.`product_id`
            ORDER BY `ip`.`product_id` ASC
            SEPARATOR ':;:') AS `product_ids`,
        GROUP_CONCAT(`p`.`descrip`
            ORDER BY `ip`.`product_id` ASC
            SEPARATOR ':;:') AS `product_descrips`
    FROM
        (((`invoices` `i`
        LEFT JOIN `clients` `c` ON ((`c`.`rec_id` = `i`.`client_id`)))
        JOIN `invoice_products` `ip` ON ((`ip`.`invoice_id` = `i`.`rec_id`)))
        LEFT JOIN `products` `p` ON ((`p`.`rec_id` = `ip`.`product_id`)))
    GROUP BY `i`.`rec_id` , `i`.`invoice_number` , `i`.`client_id` , `i`.`begin_date` , `i`.`end_date` , `c`.`first_name` , `c`.`last_name` , `c`.`email` 
*/

