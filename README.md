Splash Ekranı:

![image](https://github.com/user-attachments/assets/9a133d8c-19a5-4b77-b7f6-7d8d4f6a8199)

İntro ekranları kullanıcıya 1 kereye mahsus gösterilir. Sonrasında Uygulama içerisinde geri dönülebilir.

![image](https://github.com/user-attachments/assets/4d9649fb-2c88-4a7d-b421-aacc6d881fb5)

Notların Önceliklendirilmesi:
Notların öncelik derecesine göre sıralanmasını sağladım. Renklerle ifade edilen önem dereceleri şu şekilde tanımlandı:
Kırmızı: Yüksek öncelikli notlar.
Turuncu: Orta öncelikli notlar.
Sarı: Düşük öncelikli notlar.

Tarihe Göre Filtreleme:
o	Günlük görevleri daha verimli takip edebilmek için günü geçen veya az kalan notları filtreleme sistemine ekledim. Bu notlar uygulamanın en üst kısmında bir LazyRow içerisinde gösterildi.
Arama ve Kategorize Etme:
o	Uygulamada, kullanıcıların notlar arasında hızlıca arama yapabilmesi için bir arama çubuğu ekledim.
o	Filtreleme butonları ile notların belirli kategorilere ayrılmasını sağladım. Bu özellik sayesinde kullanıcı, yalnızca belirli bir kategoriye ait notları görüntüleyebiliyor.

•	Dropdown Menü Tasarımı: Jetpack Compose'un DropdownMenu bileşeni kullanılarak filtre seçeneklerini içeren bir menü tasarladım.
•	Filtreleme Lojik Kurgusu: ViewModel'de filtreleme kriterlerini belirleyerek kullanıcı seçimlerine göre listeyi dinamik olarak güncelledim.
•	Filtreleme sonucunun LazyRow bileşeni üzerinde doğru şekilde görüntülenmesini sağladım. Bu özellikler, kullanıcı deneyimini artırarak not yönetimini daha etkili ve kolay bir hale getirdi.  


![image](https://github.com/user-attachments/assets/16326f40-9657-4f00-ac53-add73fa2f9e1)
![image](https://github.com/user-attachments/assets/53152a68-a917-46e0-901c-449c36b4e7be)



![image](https://github.com/user-attachments/assets/488a6efc-d901-410d-bf41-98fc82770035)
![image](https://github.com/user-attachments/assets/aeb56702-7b19-40cf-905c-325b89007951)
![image](https://github.com/user-attachments/assets/e7c24bb1-3c68-4a77-ad27-0188ca0a6f4b)

Veri Silme ve Geri Alma İşlevi: Bu hafta, uygulamaya kullanıcı dostu bir veri silme ve geri alma mekanizması ekledim. Özellik şu şekilde çalışıyor:
1.	Kaydırarak silme işlemi: Kullanıcı bir veriyi silmek için kaydırma hareketi yaptığında, önce bir onay kutucuğu beliriyor.
o	Onay kutucuğu, kullanıcının "Bu veriyi silmek istiyor musunuz?" sorusuna yanıt vermesini sağlıyor.
o	Kullanıcı silme işlemine onay verirse işlem devam ediyor.
o	Kullanıcı iptal ederse işlem sonlandırılıyor.


2.	Silme işlemi onaylandıktan sonra: Bir Snackbar beliriyor.
o	Snackbar, "Geri Al" butonunu içeriyor ve bu mesaj 3 saniye boyunca ekranda kalıyor.
o	Geri Al butonuna tıklanırsa: Veri eski yerine geri yükleniyor.
o	Kullanıcı herhangi bir işlem yapmazsa: Veri kalıcı olarak siliniyor.

Bu özellik sayesinde, kullanıcılar veri silme işlemini tamamen kontrol edebiliyor ve yanlışlıkla yapılan silme işlemlerini geri alabiliyor. Aynı zamanda kullanıcı deneyimi daha güvenli ve interaktif bir hale getirilmiş oldu.

![image](https://github.com/user-attachments/assets/ee4bba26-338e-4a06-ae0e-3e7e6ca113c0)

![image](https://github.com/user-attachments/assets/8cb870d2-453f-4741-becb-e4306a60309b)


