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
	ზოგ შემთხვევაში ავსება ხდება objectProperty ობიქტის შიგთავსი ობიექტის ველები თავიდანვე არაა ცნობილი და setter-ში იცვლება, ამიტომ კონსტრუქტორში bind უნდა გვეწეროს და არა set რომ მნიშვნელობა შეიცვალოს შესაბამისმა property-მ.

* (-) Date-ების გამოჩენა სიაში და DatePicker-ში



დასახვეწია:
* (+) აჯობებს path-ის აგება გავაკეთოთ იმ კლასში სადაც გავაკეთებთ ჩაკეცვის(iconified) ლოგიკის რეალიზებას. ყველა stage-ი არ უნდა აგებდეს path-ს თავისთან ხელით. მომავალში რომ მოგვიხდეს pathDelimmiter-ის შეცვლა (/) შესაცვლელი იქნება ყველა stage-ში. (registerStageByOwner)
* (+) callGallerySendMethod არ უნდა იყოს Utils-ში (აჯობებს Client-ს ქონდეს imageGalleryController-ის Instance-ი და saveOneToDB-ში იძახებდეს ამ instance-ის send მეთოდს)
* closeStageWithChildren-ის ლოგიკა
* (+) StagesContainer კლასში შემოსატანია delimiter = "/"
* (-) Utils კლასში არასაჭირო მეთოდების ამოშლა ან გატანა სხვა კლასებში (მაგ. SceneUtils-ში იყოს Scene-თან დაკავშირებული მეთოდები)
* (+) MVS პატერნის გამოყენება Filter-ში
* (|) ATableView-ს contextMenu-დან იყოს შესაძლებლობა რომელ სვეტზეც ვდგავართ იმის content-ის alignment-ი ვცვალოთ
* (-) recId-ის ვერ ცნობს callProcedureAndGetAsJson, წამოღებისას ცნობს (getDBClient().select("discounts_on_licenses_count") და ჩაწერისას ვერა.
* (-) DBUtils რომელიც პასუხისმგებელი იქნება DBService-ისთან ურთიერთობაზე
* (-) currency-ის განმეორებადი კოდი ყველგან სადაც iso string მოდის ბაზიდან (მაგ. CurrencyRate). მოგვარება -> Product კლასში
* (+) StagesContainer.getPathForStage შესაცვლელია getPathForStage(owner, stageName)
* (-) ბაზის სტატიკურ მეთოდებში კოპირების გამო error ex-ში ან სადმე შესაძლოა იყოს ჩარჩენილი სხვადასხვა კლასის class ცვლადი...
* (+) product-ებში ზოგს მოაქვს ველი descrip და ასევე specificesrcip და ზოგს მხოლოდ specificDesrcip
* (+) product-ების width დაპატარავებისას editorPanel-ის region-ის ადგილი უჩვეულოდ დიდა რჩება
* (-) fxml, css, less ფაილები   (formPane_ზე Min და Max width_ები  // sheidzleba tavdapirvel zomas vusetavdit, tumca arc esaa sachiro)
* (|) UML დიაგრამები
* (-) Editorpanelable კლასების db მეთოდები ერთ სტილზე
* (+) product-ების დიალოგში ორჯერ ჩნდება ფასდაკლებები mapEditor-ში. (discounts.addAll() შეიცვალა discounts.setAll()-ით)
* (-) შესამოწმებელია ყველა Editorpanelable კლასში compare/copyFrom მეთოდების სისწორე
* (-) client-ში რეზიდენტობა
* (-) ALL currencies-ში და country-ში
* (-) Client-ის compare მეთოდში Phone.copmareList-ის მაგივრად Utils.compareList
* (|) ტესტები
* (+) Client-ის comboBox (+ALL, search.  წერია და კარგად მუშაობს TestExcelGeneral პროექტში)
* (+) სადაც equals და compares-ს ვიყენებთ მარტო equals-ზე ხომ არ გადავიდეთ. compares-ს საჭიროება? equals-ს საჭიროება?
compare / equals ორივე საჭიროა, მაგალითად ქვეყნებისთვის country.equal(other) ადარებს country code-ის მიხედვით, ხოლო compare ადარებს ყველა კომპონენტის მიხედვით (რადგან თუ სცენაზე რამე შეიცვალა compare მეთოდი გაარკვევს და მოამზადებს პროგრამას ალერტის ამოსაგდებად)
* (-) clientComboBox-ის ნაცვლა ერთი generic filterableComboBox-ი.
* (-) whereBuilder-ის andGropu() ან orGroup() მეთოდების გამოყენების დროს closeGroup-ის გამოძახება.
* (+) გასუფთავდეს statis hashMap-ები ??  საერთოდ აღარ ვიყენებთ static HashMap-ს...
* (-) whereBuilder-ი საჭიროებს:  whereBuilder = whereBuilder.andGroup(); ...     whereBuilder = whereBuilder.closeGroup()
* (+) client-ებს მოაქვს rezident გერმანია, როცა არ უნდ აწამოიღოს. ფილტრში ეთითება:  ორივე date -> null,  juridical -> false, country -> ALL, სტტუსები: მხოლოდ პირველი, rezident -> true
* (+) Client-ის დიალოგის დროს თუ რეზიდენტობა გაინიშნა ალბათ ქვეყანა საქართველო შესაძლოა დარჩეს არჩეული comboBox-ში.
	+ აღარაა საჭირო გადაბმა
* (-) ქვეყნების comboBox-ი ძებნადი ??


ბაზა: 
* (+) products_whole ?
* (+) general_select-ს როგორ მიეთითოს appLanguage
* (+) DBClient-ში select-ზე return new JSONArray(res.getDataAsString()); - ში ალბათ response უნდა გადაეცემოდეს ნაცვლად res.getDataAsString()-სა
* (-) BallanceAccount-ებში მოდის ორი descrip-ი. 
* (+) BallanceAccount-ებში actPas არის boolean. ადრე შეიძლებოდა რომ actPas ერთდროულად ყოფილიყო ანგარიში
* (-) product_specific_descrips და product_specifics ცხრილები ?
* (-) წაშლის პროცედურა
* (+) approved Client_Status-descrips
* (-) Client-ის შენახვის დროს (saveOneToDB) ვარდება error: 
You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '[]' at line 1
client-ის phones არის ცარიელი მასივი და შეიძლება ამის ბრალი იყოს?


დასატესტია:
* (+) ჩაკეცვა / ამოკეცვის  ფუნქციონალი. (თუ გადიდებულია მშობელი ფანჯარა, ჩაკეცვა/ამოკეცვა ცუდად მუშაობს)
* (+) stage-ის ზედა მარცხენა კუთხის კოორდინატების უარყოფითში გადასვლისას stage-ი აღარ მიყვება მშობელს
* (-) closeStageWithChildren  StagesContainer კლასში
* (-) textField-ების regular expression-ები
* (-) პროგრამა რა მეხსიერებას მოიხმარს და cpu-ს რა დონეზე ტვირთავს



