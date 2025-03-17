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
![image](https://github.com/user-attachments/assets/e7c24bb1-3c68-4a77-ad27-0188ca0a6f4b)
![image](https://github.com/user-attachments/assets/8cb870d2-453f-4741-becb-e4306a60309b)
![image](https://github.com/user-attachments/assets/488a6efc-d901-410d-bf41-98fc82770035)
![image](https://github.com/user-attachments/assets/aeb56702-7b19-40cf-905c-325b89007951)


