# İş Yeri Uzaktan Destek

Bu klasör, yalnızca yetkili ve açık kullanıcı onayıyla çalışan bir uzaktan destek uygulaması için başlangıç projesidir.

## Tasarım

- Host: İşyerindeki Windows bilgisayarında çalışır.
- Client: Yetkili yöneticinin bilgisayarında çalışır.
- Bağlantı başlamadan önce host ekranında açık izin/uyarı gösterilir.
- Kullanıcı bağlantıyı kabul veya reddedebilir.
- Bağlantı sırasında görünür bir "Uzaktan destek aktif" göstergesi bulunur.
- Kimlik doğrulama ve şifreli iletişim kullanılmalıdır.
- Gizli başlatma, parola çalma, keylogger, kamera/mikrofonun gizli açılması veya güvenlik önlemlerinin aşılması bu projeye dahil değildir.

## Sonraki geliştirme

1. Windows Host arayüzü
2. Yönetici Client arayüzü
3. Tek kullanımlık bağlantı kodu
4. TLS tabanlı güvenli bağlantı
5. Kullanıcı onayı
6. Ekran paylaşımı
7. İsteğe bağlı fare/klavye kontrolü
8. Bağlantı günlüğü

## Çalıştırma

Bu ilk commit yalnızca güvenli proje iskeletidir. Gerçek uzaktan ekran/kontrol katmanı, yetkilendirme ve güvenlik testleri tamamlandıktan sonra eklenmelidir.
