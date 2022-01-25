# Tugas Kecil Strategi Algoritma - Word Search Puzzle Solver
## Deskripsi Singkat
Word Search Puzzle adalah permainanan yang menugaskan kita untuk mencari kata-kata yang tersembunyi di dalam kumpulan huruf acak berupa matriks huruf segiempat. Kata-kata di dalam kumpulan huruf bisa tersusun dalam 8 arah sesuai dengan arah mata angin.

Program ini dibuat untuk mencari kata-kata di dalam Word Search Puzzle. Puzzle dan kata-kata yang perlu dicari diinput melalui sebuah file .txt.

## Requirements
- JDK
- VS Code

## Cara Menjalankan dan Menggunakan Program
1. Buka file program ini di VS Code
2. Copy atau buat file .txt yang berisi konfigurasi puzzle dan kata-kata yang ingin dicari ke folder test. Konfigurasi puzzle berupa matriks huruf yang dipisahkan dengan spasi. Setelah konfigurasi puzzle, berikan jeda 1 buah baris kosong dan lanjutkan dengan kata-kata yang ingin dicari yang dipisahkan dengan enter. Contoh-contoh file konfigurasi dapat dilihat di folder test.
3. Pada terminal VS Code, navigasi ke folder src dengan kode berikut. Ganti PathFolderSrc dengan lokasi folder src di perangkat masing-masing.
```
cd PathFolderSrc
```
4. Compile program Java dengan kode berikut
```
javac -d /bin Main.java
```
5. Jalankan program Java dengan kode berikut
```
java -cp /bin Main
```
6. Masukkan nama file konfigurasi yang telah dimasukkan ke folder test (tanpa extension .txt nya)
7. Program akan menampilkan hasil pencarian kata-kata di puzzle.

## Pembuat Program
Ignasius Ferry Priguna (13520126)
