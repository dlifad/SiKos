# SiKos - Sistem Manajemen Kos

## Project Praktikum Pemrograman Berorientasi Objek IF-C

### Anggota:
- **Nasarudin** - 12330020  
- **Ahmad Zainur Fadli** - 123230049

---

## Deskripsi Proyek

**SiKos** adalah sebuah aplikasi desktop berbasis **Java Swing** yang dirancang untuk mempermudah pengelolaan administrasi usaha kos-kosan. Sistem ini bertujuan untuk menyediakan antarmuka yang intuitif bagi pemilik atau pengelola kos dalam menangani berbagai aspek operasional, mulai dari **data kamar**, **data penghuni**, hingga **data pembayaran sewa**.

---

## Fitur Utama:

### 1. Manajemen Akun Pengguna
- **Registrasi Akun**  
  Memungkinkan pembuatan akun baru untuk pengguna dengan input: username, email, dan password (dengan konfirmasi).
  
- **Login Pengguna**  
  Menyediakan autentikasi bagi pengguna terdaftar untuk mengakses sistem.

---

### 2. Dashboard Utama
- Halaman navigasi utama setelah login berhasil.
- Akses cepat ke manajemen: **Kamar**, **Penghuni**, dan **Pembayaran**.
- Tersedia opsi keluar atau kembali ke halaman login.

---

### 3. Manajemen Data Kamar
- Menampilkan daftar kamar dengan detail: Nomor, Tipe, Harga, dan Status (Kosong/Terisi).
- Fitur tambah, edit, dan hapus data kamar.
- Tabel interaktif dan form input/edit dinamis.
- Tabel kamar mendukung sorting untuk setiap kolom

---

### 4. Manajemen Data Penghuni
- Menampilkan data penghuni: ID, NIK, Nama, Kontak, Nomor Kamar, Tanggal Mulai Sewa.
- Fitur tambah, edit, dan hapus data penghuni.
- Pemilihan kamar tersedia saat penambahan penghuni.
- Tabel dan form input/edit yang dinamis.
- Tabel penghuni mendukung sorting untuk setiap kolom

---

### 5. Manajemen Pembayaran Kos
- Menampilkan ringkasan tagihan **multi-periode** untuk setiap penghuni.
- Informasi: jumlah bayar akumulatif, sisa tagihan, dan status lunas/belum lunas per bulan.
- Tampilan diurutkan dari periode terbaru ke lama, lalu berdasarkan nama penghuni.
- Mencatat transaksi pembayaran untuk periode tertentu (termasuk periode lampau).
- Perhitungan otomatis sisa tagihan dan update status setelah transaksi baru.
- Form input mencakup: pemilihan penghuni, periode tagihan, tanggal transaksi, dan jumlah bayar.
- Tabel pembayaran mendukung sorting untuk setiap kolom (misalnya nama penghuni atau tanggal).

---

