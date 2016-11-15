# AmbroAFB
Accounting for Busines

##Clients განყოფილება

1. Client კლასის მოვალეობაა  კლიენტის ობიექტის  არსებობა, ისე რომ ადვილი იყოს მისი ქსელში გაგზავნა, ბაზაში ჩაწერა და სიაში გამოჩენა
  (TableView, ComboBox და სხვა). ქსელში გასაგზავნად იყენებს Json-ის სპეციფიკაციას,ბაზასთან ურთიერთქმედებისას პასუხისმგებელია შენახვა/განახლება/წაშლა/ამოღებაზე, ხოლო
  სცენაზე გამოჩენისას საშუალებას იძლევა სცენაზე რაიმე კომპონენტის მნიშვნელობის ცვლილება ავტომატურად ასახოს ობიექტის შესაბამისი ველის ცვლილებაში.
  
2. Clients წარმოადგენს 3 კომპონენტის ერთობლიობას: stage, სცენის fxml და სცენის controller-ი. Clients (stage) თვითონ ხატავს სცენას, რომელსაც დამატებით აქვს 
	კონტროლერი(ClientsController). Clients კლასის ფუნქციაა გამტარის როლი შეასრულოს გარე კლასებსა და 
	მისი სცენის კონტროლერს შორის. ClientsController-ი ურთიერთობს სცენის კომპონენტებთან, editorPanel-თან და პასუხისმგებელია სცენზე გამოაჩინოს კონკრეტული კლიენტები ClientsFilter
	კლასის საშუალებით.

3. EditorPanel-ის წარმოადგენს სცენა-კონტროლერ ერთობლიობას და ჩასმულია ყველა სცენაზე, სადაც TableView-ს ობიექტია. კლასი პასუხისმგებელია Delete, Edit, View და Add ოპერაციებზე
	და ამასთან ის ურთიერთობს აბსტრაქციებთან, ამიტომ არის ზოგადი ლოგიკის მატარებელი და არაა დამოკიდებული კონკრეტული კლასის იმპლემენტაციაზე.
	
4. ClientsFilter-ი სცენის ობიექტია, ამიტომ გააჩნია საკუთარი stage კლასი და სცენის fxml. მისი მოვალეობაა თარიღის მიხედვით გაფილტროს კლიენტების სია. 
	იყენებს FilterOkayCnacel-ს რათა დახუროს stage.

5. FilterOkayCancel ურთიერთობს კონტროლერის აბსტრაქციასთან და შესაბამისის მოქმედების შემდეგ, რასაც აბსტრაგირებული კონტროლერი გააკეთებს, ხურავს Filter-ის დიალოგს.

6. ClientDialog კლასი მომხმარებელთან ურთიერთობს და იყენებს Client ობიექტს პირდაპირი შეცვლისათვის. აბრუნებს შედეგს რაც გამოიხატება Client ობიექტით (null ან შევსებული ველებით).
	მის გასათიშად გამოიყენება DialogOkayCancel კლასი, რომელიც შედარების გზით არკვევს მოხდა თუ არა იმ ობიექტის შეცვლა, რომელიც Dialog-ი გვთავაზობდა (ამ შემტხვევაში Client). 
	ამას გებულობს იმ სცენის კონტროლერის მიხედვით, რომელზედაც თვითონ იმყოფება. ClientDialog-ს აქვს სცენის კონტროლერი რომელიც უზრუნველყოფს სცენის კომპონენტების ურთიერთობას და დიალოგ კლასის ძირითად ლოგიკას.

იხილეთ "სტუქტურის სურათი.pdf"



 +  -> მოგვარებულია
 -  -> მოსაგვარებელია
 |  -> კარგი იქნებოდა ყოფილიყო


კითხვები რომლებიც საჭიროებენ დასაბუთებულ ახსნას:
* როცა stage დაიხურება ამოიშალოს თუ არა  bidmap-იდან?
* iso-ს bind და set-ის შემთხვევები შესაბამისად CurrencyRate-სა და Currency (ასევე Product) კლასებში. იგივეა Country Client-ებში
    +  ჯობია rebind() მეთოდები გამოვიძახოთ და არ დავსეტოთ ხელით მნიშვნელობები:
    	1. კოდი ინარჩუნებს მოქნილობას. ამ მნიშვნელობებს dialog კლასებში ვაბაინდებთ ამიტომ შესაძლოა ხელით დაწერილმა set-მა runtime exception-ი მოგვცეს ან სადმე გამოგვრჩეს set-ის დაწერა.
    	2. ეს მეთოდები Listener-ის გარეთაც საჭიროა, თავად კონსტრუქტორში. ყველა property-ის თავდაპირველად აქვს default მნიშვნელობა, იმის გამო რომ რომელიმე dialog-ის ფანჯარამ (მაგ. new ან new for templ.) არ ამოაგდოს runtime NullPointerException-ი. ამიტომ ბაზიდან წამოღებული მნიშვნელობების შესაბამის setter-ებსა და getter-ებში ხდება ამ objectProperty-ების ობიექტების set-ი და get-ი. რადგან ამ ობიექტის შეცვლა ავტომატურად არ ნიშნავს იმას რომ შესაბამისი column-ის მნიშვნელობაც შეიცვლება tableView-ში, ამიტომ საჭიროა ამ column-ის მნიშვნელობა დაბაინდებული იყოს უკვე objectProperty-ი ობიექტის მნიშვნელობის შესაბამის property-ისთან.
	შენიშვნა: გამონაკლისია შემთხვევები მაშინ როცა ცვლადები bidirectional კავშირში არიან სცენის ომპონენტებთან. მაშინ მათზე unbind-ი შეცომას მოგვცემს. ამ შემთხვევაში resetი უნდა მოხდეს მნიშვნელობის და არა unbind / bind-ი.
	ზოგ შემთხვევაში objectProperty ობიქტის შიგთავსი ობიექტის ველები თავიდანვე არაა ცნობილი და setter-ში იცვლება, ამიტომ კონსტრუქტორში bind უნდა გვეწეროს და არა set რომ მნიშვნელობა შეიცვალოს შესაბამისმა property-მ.

* (-) Date-ების გამოჩენა სიაში და DatePicker-ში
* (-) setAll-ის გამოყენება ხომ არ აჯობებს reAssignTable-ებში სტატიკური მეთოდების result-ის forEach-ით გადაყოლას ?
* (+) mapEditor-ის გამოყენება productDiscount-ებში. 
	+ MapEditor-ი იმისათვის რომ მასთან მანიპულაცია მოხერხებული იყოს იყენებს Element ნტერფეისს (afb-ს შემთხვევაში MapEditorElement-ს). თუ MapEditor-ში გვინდა ელემენტი ჩავდოთ ეს ობიექტი უნდა implement-ირებდეს MapEditorelement ინტერფეისს (get.set Key/get.set Value/compare). თუმცა ამან გამოიწვია ის რომ საჭირო გახდა product-ების discount-ების დაბრუნების ახალი მეთოდი, რომელიც დააბრუნებს ObservableList<MapEditorElement>-ს. ეს სია უნდა დაესეტოს mapEditorComboBox-ს და ჩამატება წაშლა უნდა მოხდეს ამ სიაში. თუმცა ბაზიდან productDiscount-ების ასავსებად და გამოსაჩენად product-ებს აქვთ ObservableList<ProductDiscount> სია. რომლეიც თანხვედრაში უნდა მოვიდეს ზემოთ ნახსენებ სიასთან. ამიტომ setDiscount(Collection<ProductDiscount> discounts) მეთოდში, რომელიც ბაზიდან წამოღებულ მნიშვნელობებს უსეტავს product კლასს, საჭირო გახდა პირველი სიის შეცვლა. ხოლო ამ სიის შეცვლა უნდა იწვევდეს ObservableList<ProductDiscount> სიის ცვლილებას, ამიტომ დაუყენდა Listener-ი. copyFrom-შიც ხდება ObservableList<MapEditorElement> სიის გადაკოპირება.
	mapEditor-ის ძველ, generic ვერსიაში, განსხვავება იყო stringConverter-ის რეალიზებაში. ახალი stringConverter-ი გაცილებით დახვეწილია და მხოლოდ ახალი item-ის ჩამატების ფუნქციონალს მოიცავს. delete და edit გამოიყო stringConverter-ისგან და გატანილია MapEditorItem კლასში.
ძველი რეალიზება გულისხმობდა რომ mapeditor-ის Items უნდა ქონოდა konstruqtori key-სა და value-სთვის.


დასახვეწია:
* (+) აჯობებს path-ის აგება გავაკეთოთ იმ კლასში სადაც გავაკეთებთ ჩაკეცვის(iconified) ლოგიკის რეალიზებას. ყველა stage-ი არ უნდა აგებდეს path-ს თავისთან ხელით. მომავალში რომ მოგვიხდეს pathDelimmiter-ის შეცვლა (/) შესაცვლელი იქნება ყველა stage-ში. (registerStageByOwner)
* (+) callGallerySendMethod არ უნდა იყოს Utils-ში (აჯობებს Client-ს ქონდეს imageGalleryController-ის Instance-ი და saveOneToDB-ში იძახებდეს ამ instance-ის send მეთოდს)
* closeStageWithChildren-ის ლოგიკა
* (+) StagesContainer კლასში შემოსატანია delimiter = "/"
* (+) Utils კლასში არასაჭირო მეთოდების ამოშლა ან გატანა სხვა კლასებში (მაგ. SceneUtils-ში იყოს Scene-თან დაკავშირებული მეთოდები)
* (+) MVS პატერნის გამოყენება Filter-ში
* (|) ATableView-ს contextMenu-დან იყოს შესაძლებლობა რომელ სვეტზეც ვდგავართ იმის content-ის alignment-ი ვცვალოთ
* (+) recId-ის ვერ ცნობს callProcedureAndGetAsJson, წამოღებისას ცნობს (getDBClient().select("discounts_on_licenses_count") და ჩაწერისას ვერა. (პასუხი: general-insert-update-simple უნდა გამოვიძახოთ მაშინ როცა გვინდა ჩავდოთ ობიექტები რომლებიც მხოლოდ ერთ ცხრილით შემოიფარგლებიან და სხვა ცხრილშიც არ აქვთ რაიმე ველი, რთულ შემთხვევაში კონკრეტულ ობიექტს აქვს თავის ჩამატების პროცედურა, თუმცა DBClient.insertUpdate უზუნველყოფს შესაბამისი procedur-ის გამოძახებას)
* (+) DBUtils რომელიც პასუხისმგებელი იქნება DBService-ისთან ურთიერთობაზე
* (+) currency-ის განმეორებადი კოდი ყველგან სადაც iso string მოდის ბაზიდან (მაგ. CurrencyRate). მოგვარება -> Product კლასში
* (+) StagesContainer.getPathForStage შესაცვლელია getPathForStage(owner, stageName)
* (+) ბაზის სტატიკურ მეთოდებში კოპირების გამო error ex-ში ან სადმე შესაძლოა იყოს ჩარჩენილი სხვადასხვა კლასის class ცვლადი... (DBUtils მოაგვარებს ამ პრობლემას და ეს მეთოდები უფრო მოკლეები გახდებიან)
* (+) product-ებში ზოგს მოაქვს ველი descrip და ასევე specificesrcip და ზოგს მხოლოდ specificDesrcip
* (+) product-ების width დაპატარავებისას editorPanel-ის region-ის ადგილი უჩვეულოდ დიდა რჩება
	+ less ფაილში ეწერა minWidth და maxWidth
* (+) fxml, css, less ფაილები   (formPane_ზე Min და Max width_ები  // sheidzleba tavdapirvel zomas vusetavdit, tumca arc esaa sachiro)
* (|) UML დიაგრამები
* (+) Editorpanelable კლასების db მეთოდები ერთ სტილზე
* (+) product-ების დიალოგში ორჯერ ჩნდება ფასდაკლებები mapEditor-ში. (discounts.addAll() შეიცვალა discounts.setAll()-ით)
* (-) შესამოწმებელია ყველა Editorpanelable კლასში compare/copyFrom მეთოდების სისწორე
* (-) ALL currencies-ში და country-ში
* (+) Client-ის compare მეთოდში Phone.copmareList-ის მაგივრად Utils.compareList
* (|) ტესტები
* (+) Client-ის comboBox (+ALL, search.  წერია და კარგად მუშაობს TestExcelGeneral პროექტში)
* (+) სადაც equals და compares-ს ვიყენებთ მარტო equals-ზე ხომ არ გადავიდეთ. compares-ს საჭიროება? equals-ს საჭიროება?
compare / equals ორივე საჭიროა, მაგალითად ქვეყნებისთვის country.equal(other) ადარებს country code-ის მიხედვით, ხოლო compare ადარებს ყველა კომპონენტის მიხედვით (რადგან თუ სცენაზე რამე შეიცვალა compare მეთოდი გაარკვევს და მოამზადებს პროგრამას ალერტის ამოსაგდებად)
* (+) clientComboBox-ის ნაცვლად ერთი generic filterableComboBox-ი.
* (-) whereBuilder-ის andGroup() ან orGroup() მეთოდების გამოყენების დროს closeGroup-ის გამოძახება.
* (+) გასუფთავდეს statis hashMap-ები ??  + საერთოდ აღარ ვიყენებთ static HashMap-ს...
* (-) whereBuilder-ი საჭიროებს:  whereBuilder = whereBuilder.andGroup(); ...     whereBuilder = whereBuilder.closeGroup()
* (+) client-ებს მოაქვს rezident გერმანია, როცა არ უნდ აწამოიღოს. ფილტრში ეთითება:  ორივე date -> null,  juridical -> false, country -> ALL, სტტუსები: მხოლოდ პირველი, rezident -> true
* (+) Client-ის დიალოგის დროს თუ რეზიდენტობა გაინიშნა ალბათ ქვეყანა საქართველო შესაძლოა დარჩეს არჩეული comboBox-ში.
	+ აღარაა საჭირო გადაბმა
* (-) ქვეყნების comboBox-ი ძებნადი ??
* (-) @JsonIgnore
    public String language;  არ მოაქვს productSpecific_ის language, მაშინ როცა იგივე ჩანაწერი არ მუშაობს LicenseStatus კლასისთვის
* (+) TestExcelGeneral კომპონენტების საფუძვლიანი ტესტი
* (+) კონფიგურაციის განყოფილების გადაყვანა ახალ სტილზე
* (-) BallanceAccount- refresh აგდებს UnSupportedOperationException-ს როცა მოინიშნება TreeTableView column (საკუთრივ) და მერე დაეჭირება refresh
	+ treeTableView-ს შემთხვევაში არ გვაქვს განსაზღვრული რას ნიშნავდეს column-ის მიხედვით სორტირება. column-ზე კლიკი კი ავტომატურად ამ ფუქნციონალს ამოქმედებს. ეს იყო პრობლემა და არა refresh-ი. გადაწყვეტა: BallanceAccountController-ში TreeTableView-ს ყველა column-ს ვუთხარით რომ არ იყოს sortable.
* (-) invoice-სა და client განყოფილებების შედარება
* (+) სპეც. ComboBox-ების მოწესრიგება (პატერნებით)
	+ სცენაზე უნდა დაიხატოს ამიტომ ერთი კონსტრუქტორი ჭირდება უპარამეტრო, შეიძლება გართულდეს და ჯობდეს extends ComboBox<T>
* (+) localDB-ის შექმნა და derby-ის გამოყენება კიდევ საჭიროა ?? class UtilsDB მაინც დარჩეს, იქნებ საჭირო გახდეს, თუმცა კოდში არ ვიყენებთ.
* (+) რატომღაც AFB-ს არ აქვს ის პრობლემა რომ თუ lib დირექტორიის გარეშ გაეშვა აღარ გაითიშოს და გაშვებული დარჩეს განსხვავებით testMain-სგან (შესაძლოა ამის მიზეზი იყოს ის რომ AFB ორჯერ არ ეშვება - ეს არ იყო მიზეზი, არამედ ის რომ main კლასში არ ეწერა ისეთი import-ი რომელიც lib დირექტორიას საჭიროებდა).
* (-) ტელეფონების გადაყვანა ახალ mapEditor-ზე თუ არ გართულდება პროგრამა
* (+) bal_account-ების comboBox. ალბათ დაჭირდება valueProperty, და promptText-ის დასეტვა აღარ გახდება საჭირო თუ იგივე მიდგომას გამოვიყენებთ რასაც clientComboBox-ის შემთხვევაში. + აქვს valueProperty-ის მსგავსი (selectedItemProperty) და გამოყენებულია AFilterableTableViewComboBox-ი.
* (|) რამე ველი ხომ არ იყოს სადაც გამოჩნდება ამჟამად სიაში რამდენი ელემტია და ზოგადად რამდენია (შეიძლება გაფილტრულია სიის მონაცემები).
toolTip დაყენდა თითოეულ სტრიქონზე თუმცა დავამატოთ Filter-ი გამოყენებულია თუ არა.
* (+) supperWarnnigs  equals და hashCode მეთოდებზე
* (-) compare მეთოდებში ჯობია objectProperty-ების შედარებისას object-ები შევადაროთ და დავაზღვიოთ Null-ობაზე. შეიძლება რთული ჩანაწერი გამოვიდეს მაგრამ არ გამოგვრჩება შემთხვევა. მაგ: როცა comboBox-ში უნდა ჩაჯდეს კლასი იმის შესაბამისი ცვლადები ავსებულია default მნიშვნელობებით. binBidirectional კავშირის დროს კი პრიორიტეტია მარცხნივ ანუ ფრჩხილებში ჩასმული objectProperty და რადგან ეს მნიშვნელობაც default პარამეტრიანია, იმის მნიშვნელობა უყენდება comboBox-სს.  როცა comboBox-ს გავააქტიურებთ comboBox value ხდება null. ანუ ვიწყებთ აკრებას, გაქრა editor-ში მონიშნული ტექსტი (default მნიშვნეობა ისედაც "" იქნებოდა იმ კლასის რომელიც დაუყენდა, ამიტომ ვიზუალურად არ გამოჩნდება ეს პროცესი).  და არც ლურჯად მოინიშნა რომელიმე ელემენტი იმიტომ რომ არცერთთან არ მოხდა ძველად მონიშნული ელემენტის ტოლობა. ამიტომ setValue გახდა Null. ახლა უკვე null დაესეტა bindbidirection-ით comboBox-ის valueProperty-ის შესაბამის Property-ის.
* (-) Decimal-ების გამოჩენა ერთნაირი სიგრძით
* (-) column-ების სახელები
* (-) განახლებული excel_ის ფაილი (email-ზეცაა კონკრეტული ცვლილებების შესახებ)


ბაზა: 
* (+) products_whole ?
* (+) general_select-ს როგორ მიეთითოს appLanguage
* (+) DBClient-ში select-ზე return new JSONArray(res.getDataAsString()); - ში ალბათ response უნდა გადაეცემოდეს ნაცვლად res.getDataAsString()-სა
* (+) BallanceAccount-ებში მოდის ორი descrip-ი. 
* (+) BallanceAccount-ებში actPas არის boolean. ადრე შეიძლებოდა რომ actPas ერთდროულად ყოფილიყო ანგარიში
* (+) product_specific_descrips და product_specifics ცხრილები ?
* (-) წაშლის პროცედურა
* (+) approved Client_Status-descrips
* (+) Client-ის შენახვის დროს (saveOneToDB) ვარდება error: 
You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '[]' at line 1
client-ის phones არის ცარიელი მასივი და შეიძლება ამის ბრალი იყოს?  +  whereBuilder-ის ბრალი იყო, ყველაფრის წამორება გვინდა გადაეცემა ცარიელი json ანუ conditionBuilder.build(). თუ გვინდა ფილტრი მხოლოდ მაშინ ვიყენებთ whereBuilder-ს.
* (+) general_select-ი  status_descrip-ების ცხრილებზე ენას ითვალისწინებს call general_select('client_status_descrips', 'en', '{}'); ???
* (+) Client.saveOneToDB მეთოდი ჯავაში ვერ ამატებს კლიენტს და პასუხად უბრუნდება სულ recId = 1 კლიენტი.
       + rec_id საერთოდ არ უნდა ფიგურირებდეს ბაზის json-ში თუ ახალს ვამატებთ, java-დან კი მიდიოდა rec_id = 0. მოაგავარა @JsonInculde(JsonInculde.non_default)-მა
* (+) login_by_license_whole-ში LicenseNumber-ის ველი. login_time-ის მიხედვით დალაგებული ხომ არ წამოვიდეს ??
* (-) ClientDialog-ის ფანჯარა imageGallery-ის გამო დიდია სიმაღლეში, შეიძლება არ დაეტიოს ლეპტოპების ეკრანზე. ხომ არ ჯობია runtime-ში გავიგოთ ეკრანის ზომები და იმის მიხედვით დავსეტოთ imageGallery-ის ზომები.


დასატესტია:
* (+) ჩაკეცვა / ამოკეცვის  ფუნქციონალი. (თუ გადიდებულია მშობელი ფანჯარა, ჩაკეცვა/ამოკეცვა ცუდად მუშაობს)
* (+) stage-ის ზედა მარცხენა კუთხის კოორდინატების უარყოფითში გადასვლისას stage-ი აღარ მიყვება მშობელს
* (-) closeStageWithChildren  StagesContainer კლასში
* (-) textField-ების regular expression-ები
* (-) პროგრამა რა მეხსიერებას მოიხმარს და cpu-ს რა დონეზე ტვირთავს
* (-) invoice-ები begin, end და revoked  date-ებზე.



კომპონენტები და მოსაგვარებელი საკითხები:
* bal_accounts: 
	1) (+) comboBox-ის რომელიც საჭიროებს clientComboBox-ის მსგავსს ფუნქციონალს ფილტრის თვალსაზრისით.


* Clients: 
	1) (+) compare-ში Phone.compareList-ის მაგივრად Utils.compareLis-ის შემოტანა. Utils.compareList ადარებს object-ების სიებს, ამიტომ მათ გადატვირთული უნდა ქონდეთ equals მეთოდი. აუცილებელია რომ equals მეთოდის header-ი იყოს: public boolean equals(Object someName); თუ პარამეტრი არ იქნა Object ტიპის მაშინ არ ითვლება გადატვირთულად equals მეთოდი და შესაბამისად იძახება Object კლასის equals-ი, რომელიც მიმთითებლებს ადარებს.
	2) (+) clientComboBox-არჩევისას error-ი ხდება. გადავედით FilterableTableView_ის გამოჩენიაზე comboBox-ში.
	
	
* productsDiscount: 
	1) (+) productsDiscount-ებში equals-ის დროს ზოგ შემმთხვევაში other არის null. ალბათ comboBox-ის გამოყენების დროს რადგან იგივე პრობლემაა. (comboBox_ის თავისებურებიდან გამომდინარე, setValue(null)-ის დროს შესაძლოა items-ს გადაყვება)
Country-ის equals მეთპდზე, comboBox_ის დროს.

* (-) licenses:
	1) (+) licenses filter model-ში  ჯობია იყოს  setSelectedClient, setSelectedProduct, setSelectedClientIndex, setSelectedProductIndex

* (-) Currencies:
	1) (-) currencyDialog-ში  ვალუტის  iso  კოდის  comboBox-ი  ალბათ  editable უნდა იყოს
	
* (-) CurrencyRates:
	1) (-) date-ის ცვლილების შესასლებლობა უნდა იყოს დიალოგში ?
	2) (-) dialog-იდან new-ს და new by simple-ს შესაძლებლობა უნდა იყოს ?
* (-) invoice
	1) (+) დიალოგის ველები
