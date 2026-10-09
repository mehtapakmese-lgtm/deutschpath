# DeutschPath APK oluşturma — kolay yol

Bu proje Android Studio projesidir. Bu paket, GitHub Actions üzerinden APK oluşturmak için bir otomatik derleme iş akışı da içerir. Bu ortamda doğrudan APK derlenemediği için APK dosyasını burada hazır olarak sunamıyorum.

## GitHub üzerinden APK oluşturma

1. GitHub.com üzerinde oturum açın ve **New repository** ile yeni bir depo oluşturun. Örneğin adı `DeutschPath` olsun.
2. Bu ZIP'i bilgisayarınızda çıkarın.
3. İçindeki tüm dosya ve klasörleri yeni GitHub deposuna yükleyip **Commit changes** ile kaydedin. `.github/workflows/build-apk.yml` dosyasının da yüklendiğini kontrol edin.
4. Depoda **Actions** sekmesini açın.
5. **Build DeutschPath APK** iş akışını seçin ve **Run workflow** düğmesine basın. İlk yükleme sonrası `main` dalına yapılan kayıt da otomatik derlemeyi başlatır.
6. İş akışı tamamlandığında başarılı çalıştırmayı açın. Sayfanın altındaki **Artifacts** bölümünden `DeutschPath-debug-apk` dosyasını indirin.
7. ZIP'i açın; içindeki `app-debug.apk` dosyasını Android telefonunuza aktarın ve dokunarak yükleyin. Android, tarayıcı/dosya yöneticisi için “bilinmeyen uygulama yükleme” izni isterse yalnızca kullandığınız uygulamaya izin verin.

## Bilmeniz gerekenler

- Oluşan dosya test amaçlı **debug APK** olacaktır; Play Store'a yayımlanacak imzalı üretim paketi değildir.
- Bu Android uygulamasındaki ilerleme ve profil bu ilk sürümde cihazda saklanır. Çevrim içi kullanıcı hesabı ve cihazlar arası senkronizasyon henüz bu Android paketine entegre değildir.
- Derleme başarılı olsa bile, APK'nın gerçek bir Android telefonda ayrıca test edilmesi gerekir.
