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



კითხვები რომლებიც საჭიროებენ დასაბუთებულ ახსნას:
* ამოიშალოს თუ არა stage bidmap-იდან როცა დაიხურება ?


დასახვეწია:
* აჯობებს path-ის აგება გავაკეთოთ იმ კლასში სადაც გავაკეთებთ ჩაკეცვის(iconified) ლოგიკის რეალიზებას. ყველა stage-ი არ უნდა აგებდეს path-ს თავისთან ხელით. მომავალში რომ მოგვიხდეს pathDelimmiter-ის შეცვლა (/) შესაცვლელი იქნება ყველა stage-ში. (registerStageByOwner)
* callGallerySendMethod არ უნდა იყოს Utils-ში

დასატესტია:
* (+) ჩაკეცვა / ამოკეცვის  ფუნქციონალი. (თუ გადიდებულია მშობელი ფანჯარა, ჩაკეცვა/ამოკეცვა ცუდად მუშაობს)
* (+) stage-ის ზედა მარცხენა კუთხის კოორდინატების უარყოფითში გადასვლისას stage-ი აღარ მიყვება მშობელს
