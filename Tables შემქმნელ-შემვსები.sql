use ambro_soft_afb;

/*DROP TABLE IF EXISTS `ambro_soft_afb`.bal_accounts;
CREATE TABLE `ambro_soft_afb`.bal_accounts (
	`rec_id` 		INT NOT NULL AUTO_INCREMENT,
	`bal_acc` 		int,
	`descrip` 		VARCHAR(100) NULL,
	`descrip_en` 	VARCHAR(100),
	`act_pas` 		tinyint(1),
	`level` 		tinyint(1),
    `is_base` 		boolean,
	PRIMARY KEY (`rec_id`),
	UNIQUE INDEX `bal_acc_UNIQUE` (`bal_acc` ASC)
)	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.bal_accounts
(`rec_id`, `bal_acc`, `descrip`, `descrip_en`, `act_pas`, `level`, `is_base`) 
VALUES
(null, 1000, 'მიმდინარე აქტივები', 'CURRENT ASSETS', 1, 2, FALSE),
(null, 1100, 'ნაღდი ფული სალაროში', 'Cash on hand', 1, 1, FALSE),
(null, 1110, 'ნაღდი ფული ეროვნულ ვალუტაში', 'Cash on hand national currency', 1, 0, TRUE),
(null, 1120, 'ნაღდი ფული უცხოურ ვალუტაში', 'Cash on hand foreign currency', 1, 0, TRUE),
(null, 1200, 'ფული საბანკო ანგარიშებზე', 'Cash in bank', 1, 1, FALSE),
(null, 1210, 'ეროვნული ვალუტა რეზიდენტ ბანკში', 'Bank - national currency', 1, 0, TRUE),
(null, 1220, 'უცხოური ვალუტა რეზიდენტ ბანკში', 'Bank - foreign currency', 1, 0, TRUE),
(null, 1230, 'უცხოური ვალუტა არარეზიდენტ ბანკში', 'International Bank - foreign currency', 1, 0, TRUE),
(null, 1290, 'ფული სხვა საბანკო ანგარიშზე', 'Other bank accounts', 1, 0, TRUE),
(null, 1291, 'აკრედიტივი', 'Letter of Credit', 1, 0, FALSE),
(null, 1292, 'ჩეკები დეპონირებული სახსრებით', 'Checks deposited funds', 1, 0, FALSE),
(null, 1293, 'ჩეკები საბანკო გარანტიით', 'Checks with a bank guarantee', 1, 0, FALSE),
(null, 1294, 'ფული კაპიტალური დაბანდებისათვის', 'Money for capital investments', 1, 0, FALSE),
(null, 1300, 'მოკლევადიანი ინვესტიციები', 'Short-term investments', 1, 1, FALSE),
(null, 1310, 'მოკლევადიანი ინვესტიციები საწარმოთა ფასიან ქაღალდებში', 'Marketable securities', 1, 0, TRUE),
(null, 1311, 'მოკლევადიანი ინვესტიციები აქციებში', 'Short-term investments in shares', 1, 0, FALSE),
(null, 1314, 'მოკლევადიანი ინვესტიციები ობლიგაციებში', 'Short-term investments in bonds', 1, 0, FALSE),
(null, 1320, 'მოკლევადიანი ინვესტიციები სახელმწიფო ფასიან ქაღალდებში', 'Governments securities', 1, 0, TRUE),
(null, 1330, 'გრძელვადიანი ინვესტიციების მიმდინარე ნაწილი', 'Current portion of long-term investments', 1, 0, TRUE),
(null, 1390, 'სხვა მოკლევადიანი ინვესტიციები', 'Other short-term investments', 1, 0, TRUE),
(null, 1400, 'მოკლევადიანი მოთხოვნები', 'Receivables', 1, 1, FALSE),
(null, 1410, 'მოთხოვნები მიწოდებიდან და მომსახურებიდან', 'Accounts receivable - trade', 1, 0, TRUE),
(null, 1415, 'გამოქვითული საეჭვო მოთხოვნები', 'Allowance for doubtful debts', 2, 0, TRUE),
(null, 1420, 'მოთხოვნები მეკავშირე საწარმოს მიმართ', 'Accounts receivable from subsidiaries', 1, 0, TRUE),
(null, 1430, 'მოთხოვნები საწარმოს პერსონლის მიმართ', 'Accounts receivable - employees', 1, 0, TRUE),
(null, 1440, 'მოთხოვნები ხელმძღვანელებისა და სამეთვალყურეო ორგანოების წევრების მიმართ', 'Accounts receivable - officers and directors', 1, 0, TRUE),
(null, 1450, 'მოთხოვნები პარტნიორებზე გაცემული სესხიდან', 'Accounts receivable - partners on the loans issued', 1, 0, TRUE),
(null, 1460, 'კაპიტალის შევსებაზე პარტნიორების გრძელვადიანი დავალიანების მიმდინარე ნაწილი', 'Payments receivable from founders for subscribed share', 1, 0, TRUE),
(null, 1470, 'გრძელვადიანი მოთხოვნების მიმდინარე ნაწილი', 'Current portion of long-term receivables', 1, 0, TRUE),
(null, 1477, 'მოკლევადიანი მოთხოვნები ფინანსური იჯარიდან', 'Short accets from financial rent', 1, 0, FALSE),
(null, 1480, 'მომწოდებელზე გადახდილი ავანსები', 'Prepayments to suppliers', 1, 0, TRUE),
(null, 1490, 'სხვა მოთხოვნები', 'Other receivables', 1, 0, TRUE),
(null, 1500, 'მოკლევადიანი სათამასუქო მოთხოვნები', 'Notes Receivable', 1, 1, FALSE),
(null, 1510, 'მიღებული მოკლევადიანი თამასუქები', 'Notes receivable due within one year', 1, 0, TRUE),
(null, 1520, 'მიღებული გრძელვადიანი თამასუქების მიმდინარე ნაწილი', 'Current portion of long-term notes receivable', 1, 0, TRUE),
(null, 1600, 'სასაქონლო-მატერიალური მარაგი', 'Inventory', 1, 1, FALSE),
(null, 1610, 'საქონელი', 'Merchandise inventory', 1, 0, TRUE),
(null, 1620, 'ნედლეული და მასალები', 'Raw materials inventory', 1, 0, TRUE),
(null, 1629, 'სათადარიგო ნაწილები', '', 1, 0, FALSE),
(null, 1630, 'დაუმთავრებელი წარმოება', 'Work-in process inventory', 1, 0, TRUE),
(null, 1640, 'მზა პროდუქცია', 'Finished goods inventory', 1, 0, TRUE),
(null, 1642, 'კონსიგნაციით გაგზავნილი საქონელი', '', 1, 0, FALSE),
(null, 1690, 'სხვა სასაქონლო-მატერიალური მარაგი', 'Other inventory (fuel, tare)', 1, 0, TRUE),
(null, 1700, 'წინასწარ გაწეული ხარჯები', 'Prepaid expenses', 1, 1, FALSE),
(null, 1710, 'წინასწარ ანაზღაურებული მომსახურეობა', 'Prepaid services', 1, 0, TRUE),
(null, 1720, 'წინასწარ გადახდილი საიჯარო ქირა', 'Prepaid rent', 1, 0, TRUE),
(null, 1790, 'სხვა წინასწარ გაწეული ხარჯი', 'Prepaid other', 1, 0, TRUE),
(null, 1800, 'დარიცხული მოთხოვნები', 'Accrued receivables', 1, 1, FALSE),
(null, 1810, 'მისაღები დივედენდები (კორესპონდენცია 8120 ანგარიშთან)', 'Accrued dividends receivable', 1, 0, TRUE),
(null, 1820, 'მისაღები პროცენტები (კორესპონდენცია 8110 ანგარიშთან)', 'Accrued interest receivable', 1, 0, TRUE),
(null, 1890, 'სხვა დარიცხული მოთხოვნები', 'Other accrued receivables', 1, 0, TRUE),
(null, 1891, 'მოთხოვნები იჯარაზე', '', 1, 0, FALSE),
(null, 1900, 'სხვა მიმდინარე აქტივები', 'Other current assets', 1, 1, FALSE),
(null, 1910, 'სხვა მიმდინარე აქტივები', 'Current assets - other', 1, 0, TRUE),
(null, 2000, 'გრძელვადიანი აქტივები', 'LONG-TERM ASSETS', 1, 2, FALSE),
(null, 2100, 'ძირითადი საშუალებები', 'Fixed assets', 1, 1, FALSE),
(null, 2110, 'მიწა', 'Land', 1, 0, TRUE),
(null, 2120, 'დაუმთავრებელი მშენებლობა', 'Construction in progress', 1, 0, TRUE),
(null, 2130, 'შენობა', 'Buildings and improvements', 1, 0, TRUE),
(null, 2140, 'ნაგებობა', 'Constructions', 1, 0, TRUE),
(null, 2150, 'მანქანა-დანადგარები', 'Machinery and equipment', 1, 0, TRUE),
(null, 2160, 'ოფისის აღჭურვილობა', 'Office equipment', 1, 0, TRUE),
(null, 2170, 'ავეჯი და სხვა ინვენტარი', 'Furniture and fixtures', 1, 0, TRUE),
(null, 2180, 'ტრანსპორტი', 'Vehicles', 1, 0, TRUE),
(null, 2190, 'იჯარით აღებული ქონების კეთილმოწყობა', 'Leasehold improvements', 1, 0, TRUE),
(null, 2200, 'ძირითადი საშუალების ცვეთა', 'Accumulated depreciation - Fixed assets', 2, 1, FALSE),
(null, 2230, 'შენობის ცვეთა', 'Accumulated depreciation - Buildings and improvements', 2, 0, TRUE),
(null, 2240, 'ნაგებობის ცვეთა', 'Accumulated depreciation - Constructions', 2, 0, TRUE),
(null, 2250, 'მანქანა-დანადგარების ცვეთა', 'Accumulated depreciation - Machinery and equipment', 2, 0, TRUE),
(null, 2260, 'ოფისის აღჭურვილობის ცვეთა', 'Accumulated depreciation - Office equipment', 2, 0, TRUE),
(null, 2270, 'ავეჯი და სხვა ინვენტარის ცვეთა', 'Accumulated depreciation - Furniture and fixtures', 2, 0, TRUE),
(null, 2280, 'ტრანსპორტის ცვეთა', 'Accumulated depreciation - Vehicles', 2, 0, TRUE),
(null, 2290, 'იჯარით აღებული ქონების კეთილმოწყობის ცვეთა', 'Accumulated depreciation - Leasehold improvements', 2, 0, TRUE),
(null, 2300, 'გრძელვადიანი მოთხოვნები', 'Long-term receivables', 1, 1, FALSE),
(null, 2310, 'მიღებული გრძელვადიანი თამასუქები (კორესპონდენცია 1520 ანგარიშთან)', 'Notes receivable', 1, 0, TRUE),
(null, 2320, 'ფინანსურ იჯარასთან დაკავშირებული მოთხოვნები (კორესპონდენცია 1477 ანგარიშთან)', 'Finance lease receivable', 1, 0, TRUE),
(null, 2330, 'მოთხოვნები საწესდებო კაპიტალის შევსებაზე (კორესპონდენცია 1460 ანგარიშთან)', 'Payments receivable for subscribed shares', 1, 0, TRUE),
(null, 2340, 'გადავადებული საგადასახადო აქტივები', 'Deferred tax assets', 1, 0, TRUE),
(null, 2390, 'სხვა გრძელვადიანი მოთხოვნები', 'Other long-term receivables', 1, 0, TRUE),
(null, 2400, 'გრძელვადიანი ინვესტიციები', 'Long-term investments', 1, 1, FALSE),
(null, 2410, 'საწარმოთა გრძელვადიანი ფასიანი ქაღალდები', 'Long-term marketable securities', 1, 0, TRUE),
(null, 2411, 'დისკონტი გრძელვადიან ობლიგაციებზე', '', 2, 0, FALSE),
(null, 2412, 'პრემია გრძელვადიან ობლიგაციებზე', '', 2, 0, FALSE),
(null, 2414, 'გრძელვადიანი ინვესტიციები ობლიგაციებში', '', 1, 0, FALSE),
(null, 2415, 'გრძელვადიანი ინვესტიციები შვილობილი საწარმოს აქციებში', '', 1, 0, FALSE),
(null, 2416, 'გრძელვადიანი ინვესტიციები მეკავშირე საწარმოს აქციებში', '', 1, 0, FALSE),
(null, 2420, 'სახელმწიფო გრძელვადიანი ფასიანი ქაღალდები', 'Long-term governmental securities', 1, 0, TRUE),
(null, 2430, 'მონაწილეობა სხვა საზოგადოებაში', 'Participant in order companies', 1, 0, TRUE),
(null, 2490, 'სხვა გრძელვადიანი ინვესტიციები', 'Other long-term investments', 1, 0, TRUE),
(null, 2500, 'არამატერიალური აქტივები', 'Intangible assets', 1, 1, FALSE),
(null, 2510, 'ლიცენზიები', 'License', 1, 0, TRUE),
(null, 2520, 'კონცენსიები', 'Concession', 1, 0, TRUE),
(null, 2530, 'პატენტები', 'Patents', 1, 0, TRUE),
(null, 2540, 'გუდვილი', 'Goodwill', 1, 0, TRUE),
(null, 2590, 'სხვა არამატერიალური აქტივები', 'Other intangible assets', 1, 0, TRUE),
(null, 2591, 'კომპიუტერული პროგრამა', '', 1, 0, FALSE),
(null, 2592, 'სავაჭრო ნიშნები და სახელები', '', 1, 0, FALSE),
(null, 2593, 'რეცეპტი', '', 1, 0, FALSE),
(null, 2594, 'ლაბორატორიული ჩანაწერები', '', 1, 0, FALSE),
(null, 2595, 'სავაჭრო არეალი', '', 1, 0, FALSE),
(null, 2596, 'პროექტები', '', 1, 0, FALSE),
(null, 2597, 'ტექნოლოგიები', '', 1, 0, FALSE),
(null, 2598, 'ნოუ-ჰაუ', '', 1, 0, FALSE),
(null, 2599, 'ფრანშიზი', '', 1, 0, FALSE),
(null, 2600, 'არამატერიალური აქტივების ამორტიზაცია', 'Accumulated amortization of intangible assets', 2, 1, FALSE),
(null, 2610, 'ლიცენზიების ამორტიზაცია', 'Accumulated amortization - License', 2, 0, TRUE),
(null, 2620, 'კონცენსიების ამორტიზაცია', 'Accumulated amortization - Concession', 2, 0, TRUE),
(null, 2630, 'პატენტების ამორტიზაცია', 'Accumulated amortization - Patents', 2, 0, TRUE),
(null, 2640, 'გუდვილის ამორტიზაცია', 'Accumulated amortization - Goodwill', 2, 0, TRUE),
(null, 2690, 'სხვა არამატერიალური აქტივების ამორტიზაცია', 'Accumulated amortization - Other intangible assets', 2, 0, TRUE),
(null, 2691, 'კომპიუტერული პროგრამის ამორტიზაცია', '', 2, 0, FALSE),
(null, 2692, 'სავაჭრო ნიშნები და სახელების ამორტიზაცია', '', 2, 0, FALSE),
(null, 2693, 'რეცეპტის ამორტიზაცია', '', 2, 0, FALSE),
(null, 2694, 'ლაბორატორიული ჩანაწერების ამორტიზაცია', '', 2, 0, FALSE),
(null, 2695, 'სავაჭრო არეალის ამორტიზაცია', '', 2, 0, FALSE),
(null, 2696, 'პროექტების ამორტიზაცია', '', 2, 0, FALSE),
(null, 2697, 'ტექნოლოგიების ამორტიზაციას ამორტიზაცია', '', 2, 0, FALSE),
(null, 2698, 'ნოუ-ჰაუს ამორტიზაცია', '', 2, 0, FALSE),
(null, 2699, 'ფრანშიზის ამორტიზაცია', '', 2, 0, FALSE),
(null, 3000, 'მიმდინარე ვალდებულებები', 'CURRENT LIABILITIES', 2, 2, FALSE),
(null, 3100, 'მოკლევადიანი ვალდებულებები', 'Payables', 2, 1, FALSE),
(null, 3110, 'მოწოდებიდან და მომსახურეობიდან წარმოქმნილი ვალდებულებები', 'Accounts payable - trade', 2, 0, TRUE),
(null, 3115, 'გასანაღდებელი მოკლევადიანი თამასუქები', '', 2, 0, FALSE),
(null, 3120, 'მიღებული ავანსები', 'Advances received', 2, 0, TRUE),
(null, 3130, 'გადასახდელი ხელფასები', 'Wages and salaries payable', 2, 0, TRUE),
(null, 3140, 'როიალტი', 'Royalty', 2, 0, TRUE),
(null, 3150, 'საკომისიო გადასახდელები', 'Liability for fees, commissions', 2, 0, TRUE),
(null, 3160, 'ვალდებულებები საწარმოს პერსონალის წინაშე', 'Non-wage payables to company personal', 2, 0, TRUE),
(null, 3165, 'შვებულების რეზერვი', '', 2, 0, FALSE),
(null, 3170, 'ვალდებულებები მეკავშირე საწარმოების წინაშე', 'Payables to subsidiaries', 2, 0, TRUE),
(null, 3190, 'სხვა მოკლევადიანი ვალდებულებები', 'Other short-term payables', 2, 0, TRUE),
(null, 3195, 'ფინანსური იჯარის ვალდებულების მიმდინარე ნაწილი', '', 2, 0, FALSE),
(null, 3200, 'მოკლევადიანი სესხები', 'Short-Term Debt', 2, 1, FALSE),
(null, 3210, 'მოკლევადიანი სესხები', 'Short-term loans', 2, 0, TRUE),
(null, 3220, 'სესხები პარტნიორებისგან', 'Shareholders (partners) loans', 2, 0, TRUE),
(null, 3230, 'გრღელვადიანი სესხების მიმდინარე ნაწილი', 'Current portion of long-term debt', 2, 0, TRUE),
(null, 3300, 'საგადასახადო ვალდებულებები', 'Taxes Payable', 2, 1, FALSE),
(null, 3310, 'გადასახდელი მოგების გადასახადი', 'Profit tax payable', 2, 0, TRUE),
(null, 3315, 'გადახდილი მოგების გადასახადი', '', 1, 0, FALSE),
(null, 3320, 'გადასახდელი საშემოსავლო გადასახადი', 'Payroll taxes payable', 2, 0, TRUE),
(null, 3330, 'გადასახდელი დღგ', 'Value Added Tax (VAT) payable', 2, 0, TRUE),
(null, 3340, 'გადახდილი დღგ', 'VAT recoverable (paid)', 2, 0, TRUE),
(null, 3350, 'გადასახდელი აქციზი', 'Excise payable', 2, 0, TRUE),
(null, 3360, 'გადახდილი აქციზი', 'Excise paid', 2, 0, TRUE),
(null, 3370, 'სოციალური გადასახადი', 'Social insurance payable', 2, 0, TRUE),
(null, 3380, 'ქონების გადასახადი', 'Property tax', 2, 0, TRUE),
(null, 3385, 'საგზაო ფონდის გადასახადი', 'Tax for use highway', 2, 0, FALSE),
(null, 3390, 'სხვა საგადასახადო ვალდებულებები', 'Other tax payables', 2, 0, TRUE),
(null, 3400, 'დარიცხული ვალდებულებები', 'Accrued Liabilities', 2, 1, FALSE),
(null, 3410, 'გადასახდელი პროცენტები', 'Accrued interest payable', 2, 0, TRUE),
(null, 3420, 'გადასახდელი დივიდენდები', 'Dividends payable', 2, 0, TRUE),
(null, 3430, 'ვალდებულება საგარანტიო მომსახურებაზე', 'Warranties payable', 2, 0, TRUE),
(null, 3490, 'სხვა დარიცხული ვალდებულებები', 'Other accrued expenses', 2, 0, TRUE),
(null, 4000, 'გრძელვადიანი ვალდებულებები', 'LONG-TERM LIABILITIES', 2, 2, FALSE),
(null, 4100, 'გრძელვადიანი სასესხო ვალდებულებები', 'Long-Term Debt', 2, 1, FALSE),
(null, 4110, 'გასანაღდებელი ობლიგაციები', 'Bonds payable', 2, 0, TRUE),
(null, 4120, 'გასანაღდებელი გრძელვადიანი თამასუქები (კორესპონდენცია 3115 ანგარიშთან)', 'Notes payable', 2, 0, TRUE),
(null, 4130, 'ვალდებულებები ფინანსურ იჯარაზე (კორესპონდენცია 3195 ანგარიშთან)', 'Obligations under finance leases', 2, 0, TRUE),
(null, 4140, 'გრძელვადიანი სესხები', 'Long-term loans', 2, 0, TRUE),
(null, 4190, 'სხვა გრძელვადიანი სასესხო ვალდებულებები', 'Other long-term debt', 2, 0, TRUE),
(null, 4200, 'გადავადებული გადასახ და სხვა გრძელვ ვალდ', 'Deferred taxes and other Long-term liabilities', 2, 1, FALSE),
(null, 4210, 'გადადებული მოგების გადასახადი', 'Deferred profit tax', 2, 0, TRUE),
(null, 4220, 'სხვა გრძელვადიანი ვალდებულებები', 'Other long-term liabilities', 2, 0, TRUE),
(null, 4300, 'ანარიცხები', 'Provisions', 2, 1, FALSE),
(null, 4310, 'საპენსიო უზრუნველყოფის ანარიცხები', 'Provisions for the retired benefits', 2, 0, TRUE),
(null, 4320, 'სხვა ანარიცხები', 'Other provisions', 2, 0, TRUE),
(null, 4400, 'გადავადებული შემოსავალი', 'Deferred income', 2, 1, FALSE),
(null, 4410, 'გადავადებული შემოსავალი', 'Deferred income', 2, 0, TRUE),
(null, 5000, 'საკუთარი კაპიტალი', 'EQUITY', 2, 2, FALSE),
(null, 5100, 'საწესდებო კაპიტალი', 'Capital Stock', 2, 1, FALSE),
(null, 5110, 'ჩვეულებრივი აქციები', 'Common stock', 2, 0, TRUE),
(null, 5120, 'პრივილიგიური აქციები', 'Preferred stock', 2, 0, TRUE),
(null, 5130, 'გამოსყიდული საკუთარი აქციები', 'Treasury stock', 1, 0, TRUE),
(null, 5140, 'საემისიო კაპიტალი', 'Additional Paid in Capital', 2, 0, TRUE),
(null, 5150, 'საწესდებო კაპიტალი შპს-ში', 'Subscription to stock (shares)', 2, 0, TRUE),
(null, 5200, 'პარტნნიორთა კაპიტალი (შეზღ.პასუხ.არმქონე საზ.)', 'Owners Equity for non', 2, 1, FALSE),
(null, 5210, 'პარტნიორთა კაპიტალი', 'Equity Capital', 2, 0, TRUE),
(null, 5300, 'მოგება/ ზარალი', 'Profit/Loss', 2, 1, FALSE),
(null, 5310, 'გაუნაწილებელი მოგება', 'Undistributed retained earnings', 2, 0, TRUE),
(null, 5320, 'დაუფარავი ზარალი', 'Previous periods accumulated losses', 1, 0, TRUE),
(null, 5330, 'საანგარიშგებო პერიოდის მოგება/ზარალი', 'Current period income and Losses', 3, 0, TRUE),
(null, 5400, 'რეზერვები და დაფინანსება', 'Reserves and Financing', 2, 1, FALSE),
(null, 5410, 'სარეზერვო კაპიტალი', 'Restricted appropriations', 2, 0, TRUE),
(null, 5420, 'ძირ. საშუალებების გადაფასების რეზერვი', 'Asset revaluation adjustments', 2, 0, TRUE),
(null, 5430, 'ინვესტიციების გადაფასების რეზერვი', 'Long-term investments revaluation adjust', 2, 0, TRUE),
(null, 5440, 'არამატერიალური აქტივების გადაფასების რეზერვი', '', 2, 0, FALSE),
(null, 5490, 'სხვა რეზერვები და დაფინანსება', 'Other reserves and financing', 2, 0, TRUE),
(null, 6000, 'საოპერაციო შემოსავლები', 'INCOME', 2, 2, FALSE),
(null, 6100, 'საოპერაციო შემოსავლები', 'Operating income', 2, 1, FALSE),
(null, 6110, 'შემოსავალი რეალიზიციიდან', 'Income from sales', 2, 0, TRUE),
(null, 6120, 'გაყიდული საქ. დაბრუნება და ფასდათმობა', 'Returns and discounts', 1, 0, TRUE),
(null, 6130, 'შემოსავალი საეჭვო მოთხოვნებიდან', 'Bad debt income', 2, 0, FALSE),
(null, 6190, 'სხვა საოპერაციო შემოსავლები', 'Other operating income', 2, 0, TRUE),
(null, 7000, 'საოპერაციო ხარჯები', 'EXPENSES', 1, 2, FALSE),
(null, 7100, 'რეალიზ.პროდ.თვითღ.(პრ.მწ.და მომს.სფ.საწ.)', 'Cost of Goods Sold (manufacturing company)', 1, 1, FALSE),
(null, 7110, 'ძირითადი მასალების დანახარჯები/შეძენა', 'Materials purchased', 1, 0, TRUE),
(null, 7120, 'პირდაპირი ხელფასი', 'Labor - Direct', 1, 0, TRUE),
(null, 7130, 'სოციალ. დანარიცხები პირდაპირ ხელფასზე', 'Direct labor taxes and social insurance', 1, 0, TRUE),
(null, 7140, 'დამხმარე მასალების დანახარჯები/შეძენა', 'Indirect materials (purchased)', 1, 0, TRUE),
(null, 7150, 'არაპირდაპირი ხელფასი', 'Indirect labor', 1, 0, TRUE),
(null, 7160, 'სოციალ. დანარიცხები არაპირდაპირ ხელფასზე', 'Indirect labor taxes and benefits expense', 1, 0, TRUE),
(null, 7170, 'ცვეთა და ამორტიზაცია', 'Depreciation/amortization', 1, 0, TRUE),
(null, 7180, 'რემონტის დანახარჯები', 'Repair and maintenance', 1, 0, TRUE),
(null, 7185, 'სასაქ.-მატერიალური მარაგის კორექტირება (გამოიყენება მხოლოდ პერიოდულ აღრიცხვაში)', 'Inventory adjustment', 2, 0, TRUE),
(null, 7190, 'სხვა საოპერაციო ხარჯები', 'Other operating expenses', 1, 0, TRUE),
(null, 7200, 'რეალიზ.საქონ.თვითღ.(სავაჭრო საწარმოსათვის)', 'Cost of Goods Sold (merchandise firms)', 1, 1, FALSE),
(null, 7210, 'გაყიდული/შეძენილი საქონელი', 'Acquisition/purchase', 1, 0, TRUE),
(null, 7220, 'შეძენილი საქონ. უკან დაბრ. და ფასდათმობა (გამოიყენება მხოლოდ პერიოდულ აღრიცხვაში)', 'Returns/discounts', 2, 0, TRUE),
(null, 7290, 'სასაქონლო-მატერიალ. მარაგის კორექტირება (გამოიყენება მხოლოდ პერიოდულ აღრიცხვაში)', 'Inventory adjustments', 1, 0, TRUE),
(null, 7300, 'მიწოდების ხარჯები', 'Selling expenses', 1, 1, FALSE),
(null, 7310, 'რეკლამის ხარჯები', 'Advertisement and sales promotion expense', 1, 0, TRUE),
(null, 7320, 'შრომის ანაზღ.და საკომისიო გასამრჯელო', 'Salaries,wages and commissions expenses', 1, 0, TRUE),
(null, 7330, 'შრომის ანაზღაურებაზე დანარიცხები', 'Sales salary taxes and benefits expenses', 1, 0, TRUE),
(null, 7340, 'ტრანსპორტირებისა და შენახვის ხარჯები', 'Transportation and storage expenses', 1, 0, TRUE),
(null, 7390, 'მიწოდების სხვა ხარჯები', 'Other selling expenses - taxable', 1, 0, TRUE),
(null, 7400, 'საერთო და ადმინისტრაციული ხარჯები', 'General and administrative expenses', 1, 1, FALSE),
(null, 7410, 'შრომის ანაზღაურება', 'Salary and wages expense', 1, 0, TRUE),
(null, 7411, 'დამოუკიდებელი ანგარიში', 'Independent account', 1, 0, FALSE),
(null, 7415, 'სოციალური დანარიცხები', 'Social insurance - expense', 1, 0, FALSE),
(null, 7420, 'საიჯარო ქირა', 'Operating lease', 1, 0, TRUE),
(null, 7425, 'საოფისე ინვენტარი', 'Office supplies', 1, 0, TRUE),
(null, 7430, 'კომუნიკაციის ხარჯები', 'Communications - expense', 1, 0, TRUE),
(null, 7435, 'დაზღვევა', 'Insurance', 1, 0, TRUE),
(null, 7440, 'რემონტი', 'Repairs', 1, 0, TRUE),
(null, 7445, 'კომპიუტერის ხარჯები', 'Computers expenses', 1, 0, TRUE),
(null, 7450, 'საკონსულტაციო ხარჯები', 'Consulting fees', 1, 0, TRUE),
(null, 7455, 'ცვეთა და ამორტიზაცია', 'Depreciation and amortization', 1, 0, TRUE),
(null, 7460, 'საეჭვო მოთხოვნ. დაკავშირებული ხარჯები', 'Bad debt expense', 1, 0, TRUE),
(null, 7461, 'ფასდათმობასთან დაკავშირებული ხარჯები', '', 1, 0, FALSE),
(null, 7465, 'სხვა საგადასახადო ხარჯი', 'Other tax expenses', 1, 0, TRUE),
(null, 7490, 'სხვა საერთო ხარჯი', 'Other general expenses', 1, 0, TRUE),
(null, 7491, 'რეკლამის ხარჯი', '', 1, 0, FALSE),
(null, 7492, 'ბანკის მომსახურეობის ხარჯი', '', 1, 0, FALSE),
(null, 7493, 'მივლინების ხარჯი', '', 1, 0, FALSE),
(null, 8000, 'არასაოპერაციო ხარჯები და შემოსავლები', 'NON-OPERATING INCOME AND EXPENSE', 2, 2, FALSE),
(null, 8100, 'არასაოპერაციო შემოსავლები', 'Non-operation income', 2, 1, FALSE),
(null, 8110, 'საპროცენტო შემოსავალი', 'Interest income', 2, 0, TRUE),
(null, 8120, 'დივიდენდები', 'Income from dividends', 2, 0, TRUE),
(null, 8130, 'არასაოპერაციო მოგება', 'Gain from non operating activities', 2, 0, TRUE),
(null, 8140, 'მოგება სავალუტო კურსთაშორისი სხვაობიდან', 'Gain from changes in exchange rates', 2, 0, FALSE),
(null, 8180, 'მოგება არამატერიალური აქტივების აფასებიდან', '', 2, 0, FALSE),
(null, 8190, 'სხვა არასაოპერაციო შემოსავალი', 'Other non-operating income', 2, 0, TRUE),
(null, 8191, 'შემოსავალი იჯარიდან', '', 2, 0, FALSE),
(null, 8200, 'არასაოპერაციო ხარჯები', 'Non-Operating Expenses/Losses ', 1, 1, FALSE),
(null, 8210, 'საპროცენტო ხარჯი', 'Interest expense', 1, 0, TRUE),
(null, 8220, 'არასაოპერაციო ზარალი', 'Non-operating loss', 1, 0, TRUE),
(null, 8230, 'გადახდილი ჯარიმა', '', 1, 0, FALSE),
(null, 8240, 'ზარალი სავალუტო კურსთაშორისი სხვაობიდან', 'Losses from changes in exchange rates', 1, 0, FALSE),
(null, 8270, 'ზარალი ფინანსური იჯარიდან ', '', 1, 0, FALSE),
(null, 8280, 'ზარალი არამატერიალური აქტივების ჩამოფასებიდან ', '', 1, 0, FALSE),
(null, 8290, 'სხვა არასაოპერაციო ხარჯები', 'Other non operating Losses', 1, 0, TRUE),
(null, 9000, 'განსაკუთრებული და სხვა შემოსავლები და ხარჯები', 'EXTRAORDINARY GAINS (LOSSES)', 2, 2, FALSE),
(null, 9100, 'განსაკუთრებული შემოსავლები და ხარჯები', 'Extraordinary gains or losses', 2, 1, FALSE),
(null, 9110, 'განსაკუთრებული შემოსავლები', 'Extraordinary gains', 2, 0, TRUE),
(null, 9120, 'განსაკუთრებული ხარჯები', 'Extraordinary losses', 1, 0, TRUE),
(null, 9200, 'სხვა ხარჯები', 'Other expenses', 1, 1, FALSE),
(null, 9210, 'მოგების გადასახადი', 'Profit tax', 1, 0, TRUE),
(null, 9220, 'გაცემული შესაწირი', '', 1, 0, FALSE);*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.clients;
CREATE TABLE `ambro_soft_afb`.clients (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
	`email` 		varchar(255) DEFAULT NULL,
	`password` 		varchar(255) DEFAULT NULL,
	`is_jur` 	  	        BIT DEFAULT false,
	`first_name` 	varchar(255) DEFAULT NULL,
	`last_name` 	varchar(255) DEFAULT NULL,
	`address` 		varchar(255) DEFAULT NULL,
	`zip_code` 		varchar(255) DEFAULT NULL,
	`city` 			varchar(255) DEFAULT NULL,
	`country_code` 	  varchar(2) DEFAULT NULL,
	`is_rezident`	        BIT DEFAULT false,
	`pass_number` 	varchar(255) DEFAULT NULL,
	PRIMARY KEY (`rec_id`),
	UNIQUE KEY `email` (`email`),
	UNIQUE KEY `email_first_name_last_name` (`email`, `first_name`, `last_name`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.clients
(`rec_id`, `email`, `password`, `is_jur`, `first_name`, `last_name`, `address`, `zip_code`, `city`, `country_code`, `is_rezident`, `pass_number`) 
VALUES
(null, 'm.ambrola@gmail.com', 'password', false, 'Murman', 'ამბროლაძე', 'დელისი 30ა', '0177', 'Tiflis', 'GE', true, '01024008007'), 
(null, 'Glimer@gmail.com', 'password', true, 'Glimer', 'GmbH', 'Strasener 56', '60004', 'Hanover', 'DE', false, '171717017'), 
(null, 'GiaLomidze@web.de', 'password', false, 'Gia', 'Lomidze', 'shtrase N', '63255', 'Celle', 'DE', false, '01024448887'); 
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.accounts;
CREATE TABLE `ambro_soft_afb`.accounts (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
	`account` 				 int NOT NULL,
	`iso` 			  varchar(3) NOT NULL,
	`bal_acc_id` 	         int NOT NULL,
	`descrip` 		varchar(255) DEFAULT NULL,
	`client_no` 	  bigint(20) DEFAULT NULL,
	`date_open` 		datetime DEFAULT NULL,
	`remark` 		varchar(255) DEFAULT NULL,
	`flag` 				 tinyint DEFAULT NULL,
	PRIMARY KEY (`rec_id`),
	UNIQUE KEY `account_iso` (`account`, `iso`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.accounts
(`rec_id`, `account`, `iso`, `bal_acc_id`, `descrip`, `client_no`, `date_open`, `remark`, `flag`) 
VALUES
(null, 11006, 'GEL', 1110, 'სალარო', null, '2016-02-20', 'დროებითია', null), 
(null, 11013, 'GEL', 1110, 'სალარო', null, '2016-02-20', 'დროებითია', null), 
(null, 11020, 'GEL', 1110, 'სალარო', null, '2016-02-20', 'დროებითია', null), 
(null, 11037, 'GEL', 1110, 'სალარო', null, '2016-02-20', 'დროებითია', null), 
(null, 231017, 'GEL', 3120, 'კლიენტი Murman', 2, '2016-02-25', 'test', 2);
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.products;
CREATE TABLE `ambro_soft_afb`.products (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
	`descrip` 		varchar(255) DEFAULT NULL,
	`remark` 		varchar(255) DEFAULT NULL,
	PRIMARY KEY (`rec_id`),
	UNIQUE KEY `descrip` (`descrip`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.products
(`rec_id`, `descrip`, `remark`) 
VALUES
(null, 'auto_radar', 'auto remark'),
(null, 'ebay_radar', 'ebay remark'),
(null, 'expert', 'expert remark');
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.product_prices;
CREATE TABLE `ambro_soft_afb`.product_prices (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
    `product_id`	  bigint(20) NOT NULL,
	`day` 			 	     int NOT NULL,
	`price_per_month` 		decimal(8,2) NOT NULL,
	PRIMARY KEY (`rec_id`),
	UNIQUE KEY `product_id_day` (`product_id`, `day`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.product_prices
(`rec_id`, `product_id`, `day`, `price_per_month`) 
VALUES
(null, 1,  28, 25.00),
(null, 1, 365, 20.00),
(null, 2,  28, 15.00),
(null, 3,  28, 40.00),
(null, 3,  89, 35.00),
(null, 3, 181, 25.00),
(null, 3, 365, 20.00)
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.invoices;
CREATE TABLE `ambro_soft_afb`.invoices (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
    `invoice_number`  varchar(255) DEFAULT NULL,
    `client_id`	  	  bigint(20) NOT NULL,
	`begin_date` 		datetime NOT NULL,
	`end_date` 			datetime NOT NULL,
	PRIMARY KEY (`rec_id`),
    UNIQUE KEY `invoice_number` (`invoice_number`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.invoices
(`rec_id`, `invoice_number`, `client_id`, `begin_date`, `end_date`) 
VALUES
(null, '2016/25', 2, '2016-02-26', '2016-04-26'),
(null, '2016/28', 3, '2016-02-26', '2016-08-26'),
(null, '2016/22', 3, '2016-02-26', '2017-02-26')
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.invoice_products;
CREATE TABLE `ambro_soft_afb`.invoice_products (
	`rec_id` 		  bigint(20) NOT NULL AUTO_INCREMENT,
    `invoice_id`	  bigint(20) NOT NULL,
    `product_id`	  bigint(20) NOT NULL,
	PRIMARY KEY (`rec_id`),
	UNIQUE KEY `invoice_id_product_id` (`invoice_id`, `product_id`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.invoice_products
(`rec_id`, `invoice_id`, `product_id`) 
VALUES
(null, 4, 4),
(null, 4, 2),
(null, 2, 4),
(null, 3, 4),
(null, 3, 2),
(null, 3, 3)
*/

/*DROP TABLE IF EXISTS `ambro_soft_afb`.countries;
CREATE TABLE `ambro_soft_afb`.countries (
	`rec_code` 		  varchar(2) NOT NULL,
    `descrip` 		varchar(255) DEFAULT NULL,
	PRIMARY KEY (`rec_code`),
	UNIQUE KEY `rec_code` (`rec_code`)
) 	ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ambro_soft_afb`.countries
(`rec_code`, `descrip`) 
VALUES
('GE', 'Georgia'),
('DE', 'Germany'),
('RU', 'Russia'),
('PL', 'Polska')
*/