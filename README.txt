==============
|   README   |
==============

Developers:
- 13513009 Muhamad Fikri Alhawarizmi
- 13513021 Erick Chandra

DESAIN APLIKASI
===============

- Aplikasi dibuat dalam bentuk CLI, menggunakan bahasa Java, dengan bantuan IDE NetBeans.
- Semua komunikasi antara server dan client ditengahi oleh RabbitMQ sebagai middleware.
- Komunikasi dapat dibagi menjadi 3 bagian:
    1. Komunikasi Request-Response (semua Command semisal REGISTER, LOGIN, ADDFRIEND, dll)
    2. Komunikasi pengiriman pesan (private chat maupun group chat)
    3. Komunikasi notifikasi (dari server ke client)
- Tipe exchange yang digunakan sesuai dengan poin di atas:
    1. RPC - [default exchange]
    2. topic - message_exchange
    3. direct - notification_exchange
- Untuk topic, penamaan menggunakan format berikut:
    => message.[group/private].[username]
- Asumsi yang digunakan adalah
    - Tidak digunakan persistent storage untuk menyimpan informasi tentang user, message, ataupun yang lain.
    - Pemenuhan tugas sesuai spesifikasi, dengan beberapa tambahan opsional (tidak tercantum di spesifikasi) disesuaikan saat pengembangan


Langkah-langkah menjalankan program
===================================

- Untuk server, buka project netbeans dan navigasi ke berkas HiChatServer.java. Kemudian Run File dari Netbeans.
- Untuk client, buka project netbeans dan navigasi ke berkas HiChatClient.java. Kemudian Run File dari Netbeans.

- Komando yang diperbolehkan dapat dilihat saat kode program dijalankan.
- Komando untuk Private Chat, gunakan "CHATPRIVATE", untuk Group Chat, gunakan "CHATGROUP".
- Jalankan 1 server dan beberapa klien sekaligus


Langkah-langkah melakukan tes
=============================

Untuk klien A
- Register
- Login
- Add friend, misal B
- Create group, misal ABCGROUP
- Chat private, misal dengan A
- Masukkan content untuk dikirimkan

Untuk klien B
- Register
- Login
- Chat private, misal dengan A
- Pesan akan masuk ke B yang dikirimkan oleh A ke B
- Kirim ke A, dan A akan menerima pesan dari B

Untuk server
- Jalankan dan diamkan
- Jika server menerima perintah dari klien, akan ditulis log pada layar