import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        /* KAMUS */
        char puzzle[][];
        int puzzleColor[][];
        int rowNum, colNum, wordNum, totalComp;
        long totalTime;
        Word words[];
        String tempRow[];
        String filename;
        boolean fileReadSuccess, isPuzzleSizeFound, isReadingPuzzle, wordFound;

        String resetColorCode = "\u001B[0m";

        /* ALGORITMA */
        System.out.println("WORD SEARCH PUZZLE SOLVER");

        /* Pembacaan File */
        fileReadSuccess = false;
        Scanner input = new Scanner(System.in);
        filename = "";
        rowNum = 0;
        colNum = 0;
        wordNum = 0;

        while (!fileReadSuccess) {
            System.out.print("Masukkan nama file (tanpa .txt): ");
            filename = input.nextLine();
            System.out.println("");

            /* Perhitungan ukuran puzzle dan jumlah kata yang perlu dicari */
            try {
                File file = new File("../test/" + filename + ".txt");
                Scanner reader1 = new Scanner(file);
                isPuzzleSizeFound = false;

                String data;
                while (reader1.hasNextLine()) {
                    data = reader1.nextLine().trim();
                    if (!isPuzzleSizeFound) {
                        if (data.length() == 0) {
                            isPuzzleSizeFound = true;
                        } else {
                            if (colNum == 0) {
                                colNum = data.split(" ").length;
                            }
                            rowNum++;
                        }
                    } else {
                        wordNum++;
                    }
                }

                fileReadSuccess = true;
                input.close();
                reader1.close();
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan!");
                e.printStackTrace();
            }
        }

        puzzle = new char[rowNum][colNum];
        words = new Word[wordNum];

        /* Pembacaan data dari file konfigurasi */
        try {
            File file = new File("../test/" + filename + ".txt");
            Scanner reader2 = new Scanner(file);

            isReadingPuzzle = true;
            int i = 0;
            int k = 0;

            String data;
            while (reader2.hasNextLine()) {
                data = reader2.nextLine().trim();

                if (isReadingPuzzle) {
                    if (data.length() == 0) {
                        isReadingPuzzle = false;
                    } else {
                        tempRow = data.split(" ");
                        for (int j = 0; j < colNum; j++) {
                            puzzle[i][j] = tempRow[j].charAt(0);
                        }
                        i++;
                    }
                } else {
                    words[k] = new Word(data.toCharArray());
                    k++;
                }
            }

            reader2.close();

        } catch (FileNotFoundException e) {
        }

        /* Pencarian kata */
        for (int n = 0; n < wordNum; n++) {
            /* Iterasi kata */
            /* Assignment color untuk kata */
            Random r = new Random();
            words[n].colorCode = "\u001B[38;5;" + (r.nextInt(212) + 17) + "m";

            long start = System.nanoTime();
            wordFound = false;
            int i = 0;

            while (!wordFound && i < rowNum) {
                /* Iterasi baris */
                int j = 0;
                while (!wordFound && j < colNum) {
                    /* Iterasi kolom */
                    if (puzzle[i][j] == words[n].letters[0]) {
                        byte h = -1;
                        while (!wordFound && h <= 1) {
                            /* Iterasi arah pencarian */
                            byte v = -1;
                            while (!wordFound && v <= 1) {
                                if (!(h == 0 && v == 0)) {
                                    int c = 1;
                                    boolean isSimilar = true;
                                    while (c < words[n].length && isSimilar) {
                                        /* Perbandingan huruf pada kata yang dicari dengan huruf pada puzzle */
                                        if (i + c * v < 0 || i + c * v >= rowNum || j + c * h < 0
                                                || j + c * h >= colNum) {
                                            isSimilar = false;
                                        } else {
                                            if (words[n].letters[c] == puzzle[i + c * v][j + c * h]) {
                                                c++;
                                            } else {
                                                isSimilar = false;
                                            }
                                            words[n].comparisonNum++;
                                        }
                                    }

                                    if (isSimilar) {
                                        wordFound = true;
                                        words[n].wordFound(i, j, h, v, System.nanoTime() - start);
                                    }
                                }
                                v++;
                            }
                            h++;
                        }
                    }
                    words[n].comparisonNum++;
                    j++;
                }
                i++;
            }

            if (!wordFound) {
                words[n].wordNotFound(System.nanoTime() - start);
            }
        }

        /* Display jawaban akhir */
        /* Display puzzle */
        puzzleColor = new int[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                puzzleColor[i][j] = -1;
            }
        }

        for (int n = 0; n < wordNum; n++) {
            for (int x = 0; x < words[n].length; x++) {
                if (words[n].rowFound != -1) {
                    puzzleColor[words[n].rowFound + x * words[n].verticalDirection][words[n].colFound
                            + x * words[n].horizontalDirection] = n;
                }
            }
        }

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (puzzleColor[i][j] == -1) {
                    System.out.print(puzzle[i][j] + " ");
                } else {
                    System.out.print(words[puzzleColor[i][j]].colorCode + puzzle[i][j] + " " + resetColorCode);
                }

            }
            System.out.println("");
        }

        /* Display jumlah perbandingan huruf dan total waktu pencarian kata */
        totalComp = 0;
        totalTime = 0;
        for (int n = 0; n < wordNum; n++) {
            totalComp += words[n].comparisonNum;
            totalTime += words[n].comparisonTime;
        }
        System.out.println("\nTotal jumlah perbandingan huruf yang dilakukan = " + totalComp);
        System.out.println("Total waktu pencarian seluruh kata = " + totalTime + "ns");
    }
}

class Word {
    char letters[]; // Huruf-huruf dalam kata
    int length; // Panjang kata
    int rowFound; // Baris ditemukannya kata
    int colFound; // Kolom ditemukannya kata
    int horizontalDirection; // Arah horizontal ditemukannya kata
    int verticalDirection; // Arah vertikal ditemukannya kata
    int comparisonNum; // Jumlah perbandingan huruf
    long comparisonTime; // Waktu pencarian kata
    String colorCode; // Kode warna kata yang akan ditampilkan

    public Word(char[] l) {
        length = l.length;
        letters = new char[length];
        comparisonNum = 0;
        rowFound = -1;

        for (int i = 0; i < length; i++) {
            letters[i] = l[i];
        }
    }

    public void wordFound(int row, int col, int hd, int vd, long ct) {
        /*
         * Mengeluarkan pesan bahwa kata telah ditemukan beserta info lokasi
         * ditemukannya serta jumlah dan waktu perbandingannya.
         */
        /*
         * Mengeset properti lokasi ditemukannya kata, arahnya, dan waktu
         * perbandingannya
         */
        rowFound = row;
        colFound = col;
        horizontalDirection = hd;
        verticalDirection = vd;
        comparisonTime = ct;

        System.out.print("Kata \"" + colorCode + String.valueOf(letters) + "\u001B[0m" + "\" ditemukan di baris "
                + (rowFound + 1) + " kolom " + (colFound + 1) + " dengan arah");
        if (hd == -1) {
            System.out.print(" kiri");
        } else if (hd == 1) {
            System.out.print(" kanan");
        }

        if (vd == -1) {
            System.out.print(" atas");
        } else if (vd == 1) {
            System.out.print(" bawah");
        }
        System.out.println(".");
        System.out.println("Jumlah perbandingan huruf yang dilakukan = " + comparisonNum);
        System.out.println("Waktu pencarian kata = " + comparisonTime + "ns\n");
    }

    public void wordNotFound(long ct) {
        /*
         * Mengeluarkan pesan bahwa kata tidak ditemukan di puzzle serta jumlah dan
         * waktu perbandingannya.
         */
        /* Mengeset properti waktu perbandingannya */
        comparisonTime = ct;

        System.out.println("Kata \"" + String.valueOf(letters) + "\" tidak ditemukan di puzzle.");
        System.out.println("Jumlah perbandingan huruf yang dilakukan = " + comparisonNum);
        System.out.println("Waktu pencarian kata = " + comparisonTime + "ns\n");
    }
}