# 2 Oyunculu Online Tetris

Oyun artık 2 oyunculu online moda göre tasarlanmıştır.

## Akış
1. Oyuncu A oda oluşturur.
2. Sistem 6 karakterli oda kodu üretir.
3. Oyuncu B kodu girerek odaya katılır.
4. Oda `playing` durumuna geçer.
5. İki oyuncunun skor, level, satır ve canlılık durumu gerçek zamanlı senkronize edilir.
6. Oyunculardan biri elendiğinde diğer oyuncu kazanır.

## Firebase bağlantısı
Gerçek cihazlar arasında senkronizasyon için Firebase/Firestore bağlantısı gerekir. `google-services.json` dosyası kullanıcıya ait Firebase projesinden alınmalıdır ve GitHub'a gizli kimlik bilgileri içeren dosyalar yüklenmemelidir.

Kod tabanında online oda modeli ve `RoomManager` sınırı hazırdır; Firebase adapter'ı bu katmana bağlanabilir.
