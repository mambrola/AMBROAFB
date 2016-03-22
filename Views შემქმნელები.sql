use ambro_soft_afb;

/*CREATE  OR REPLACE 
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
    GROUP BY `i`.`rec_id`
*/
/*CREATE  OR REPLACE 
    ALGORITHM = UNDEFINED 
    DEFINER = `dtm`@`%` 
    SQL SECURITY DEFINER
VIEW `clients_to_java` AS
    SELECT 
        `c`.`rec_id` AS `rec_id`,
        `c`.`email` AS `email`,
        `c`.`password` AS `password`,
        `c`.`is_jur` AS `is_jur`,
        `c`.`first_name` AS `first_name`,
        `c`.`last_name` AS `last_name`,
        `c`.`address` AS `address`,
        `c`.`zip_code` AS `zip_code`,
        `c`.`city` AS `city`,
        CONCAT(`ct`.`rec_code`, ';:;', `ct`.`descrip`) AS `country`,
        `c`.`is_rezident` AS `is_rezident`,
        `c`.`pass_number` AS `pass_number`,
        `c`.`fax` AS `fax`,
        GROUP_CONCAT(CONCAT(`cp`.`rec_id`, ';:;', `cp`.`phone`)
            ORDER BY `cp`.`rec_id` ASC
            SEPARATOR ':;:') AS `phones`
    FROM
        ((`clients` `c`
        LEFT JOIN `countries` `ct` ON ((`ct`.`rec_code` = `c`.`country_code`)))
        LEFT JOIN `client_phones` `cp` ON ((`cp`.`client_id` = `c`.`rec_id`)))
    GROUP BY `c`.`rec_id`
*/
