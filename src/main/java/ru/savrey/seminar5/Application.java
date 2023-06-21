package ru.savrey.seminar5;

/**
 * 1. Написать функцию, создающую резервную копию всех файлов в директории(без поддиректорий)
 * во вновь созданную папку ./backup
 * 2. Доработайте класс Tree и метод print который мы разработали на семинаре.
 * Ваш метод должен распечатать полноценное дерево директорий и файлов относительно
 * корневой директории.
 * 3***. Предположить, что числа в исходном массиве из 9 элементов имеют диапазон[0, 3],
 * и представляют собой, например, состояния ячеек поля для игры в крестикинолики, где 0 –
 * это пустое поле, 1 – это поле с крестиком, 2 – это поле с ноликом,
 * 3 – резервное значение. Такое предположение позволит хранить в одном числе типа int всё поле 3х3.
 * Записать в файл 9 значений так, чтобы они заняли три байта
 */

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Application {

    /**
     * 1. Создать два текстовых файла (по 50-100 символов).
     * 2. Написать метод, "склеивающий" эти файлы.
     * 3. Написать метод, который проверяет присутствует ли указанное пользователем слово в файле.
     * 4. Написать метод, проверяющий, есть ли указанное слово в папке.
     */

    private static final Random random = new Random();

    private static final int CHAR_BOUND_L = 65; // Номер начального символа
    private static final int CHAR_BOUND_H = 90; // Номер конечного символа

    private static final String WORD = "SAVREY"; // Слово для поиска

    public static void main(String[] args) throws IOException {
        writeFileContent2("file1.txt", 10, 10);

        Tree.printTree(new File("."), "", true);

        String[] fileNames = new String[10];
        for (int i = 0; i < fileNames.length; i++){
            fileNames[i] = "file_" + i + ".txt";
            writeFileContent2(fileNames[i], 100, 4);
            System.out.printf("Файл %s создан.\n", fileNames[i]);
        }

        List<String> result = searchMatch(fileNames, WORD);
        for (String s : result) {
            System.out.printf("Файл %s содержит слово '%s'\n", s, WORD);
        }

        backup(".");
    }

    /**
     * Метод генерации последовательности символов.
     * @param amount Количество символов в последовательности.
     * @return Последовательность символов.
     */
    private static String generateSymbols(int amount){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++){
            sb.append((char)random.nextInt(CHAR_BOUND_L, CHAR_BOUND_H + 1));
        }
        return sb.toString();
    }

    /**
     * Метод записи последовательности символов в файл.
     * @param filename имя файла.
     * @param length длина последовательности символов.
     * @throws IOException Исключение ввода-вывода.
     */
    private static void writeFileContent(String filename, int length) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(filename)){
            fileOutputStream.write(generateSymbols(length).getBytes());
        }
        /* FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(generateSymbols(length).getBytes());
        fileOutputStream.flush();
        fileOutputStream.close(); */
    }

    /**
     * Метод записи последовательности символов в файл и добавления осозанного слова случайным образом.
     * @param filename имя файла.
     * @param length длина последовательности символов.
     * @param words количество слов для поиска .
     * @throws IOException Исключение ввода-вывода.
     */
    private static void writeFileContent2(String filename, int length, int words) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            for (int i = 0; i < words; i++){
                if(random.nextInt(5) == 5 / 2){
                    fileOutputStream.write(WORD.getBytes());
                } else {
                    fileOutputStream.write(generateSymbols(random.nextInt(3, length)).getBytes());
                }
                fileOutputStream.write(' ');
            }
        }
    }

    /**
     * Метод склеивания содержимого двух файлов и записи результата в третий файл.
     * @param fileIn1 Имя первого исходного файла.
     * @param fileIn2 Имя второго исходного файла.
     * @param fileOut Имя файла для записи результата склеивания.
     * @throws IOException Исключение ввода-вывода.
     */
    private static void concatenate(String fileIn1, String fileIn2, String fileOut) throws IOException{
        // На запись
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileOut)) {
            int c;
            // На чтение
            try(FileInputStream fileInputStream = new FileInputStream(fileIn1)) {
                while ((c = fileInputStream.read()) != -1)
                    fileOutputStream.write(c);
            }
            // На чтение
            try(FileInputStream fileInputStream = new FileInputStream(fileIn2)) {
                while ((c = fileInputStream.read()) != -1)
                    fileOutputStream.write(c);
            }
        }
    }

    /**
     * Метод поиска последовательности символов в файле.
     * @param fileName Имя файла.
     * @param search Последовательность символов для поиска.
     * @return Результат поиска.
     */
    private static boolean searchInFile(String fileName, String search) throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(fileName)) {
            byte[] searchData = search.getBytes();
            int c;
            int counter = 0;
            while ((c = fileInputStream.read()) != -1){
                if (c == searchData[counter]){
                    counter++;
                } else {
                    counter = 0;
                    if (c == searchData[counter]) // SAVRESAVREY
                        counter++;
                }
                if (counter == searchData.length){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Метод поиска определенного слова в файлах текущей директории
     * @param files Массив имен файлов.
     * @param search Определенное слово для поиска.
     * @return Список имен файлов, в которых содержится искомое слово.
     * @throws IOException Исключение ввода-вывода.
     */
    private static List<String> searchMatch(String[] files, String search) throws IOException {
        List<String> list = new ArrayList<>();
        File path = new File(new File(".").getCanonicalPath());
        File[] dir = path.listFiles();
        assert dir != null;
        for (File file : dir) {
            if (file.isDirectory())
                continue;
            for (String s : files) {
                if (file.getName().equals(s)) {
                    if (searchInFile(file.getName(), search)) {
                        list.add(file.getName());
                        break;
                    }
                }
            }
        }
        return list;
    }

    /**
     * Метод копирования файлов из указанной директории в ./backup
     * @param directory Имя директории для копирования файлов.
     * @throws IOException Исключение ввода-вывода.
     */
    private static void backup(String directory) throws IOException {
        File backupDir = new File(directory + "/backup");
        boolean createBackup = backupDir.mkdir();
        File path = new File(new File(directory).getCanonicalPath());
        File[] dir = path.listFiles();
        assert dir != null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        for (File file : dir){
            if (file.isDirectory())
                continue;
            try {
                inputStream = new FileInputStream(file);
                File backupFile = new File(backupDir.getPath() + "\\" + file.getName());
                outputStream = new FileOutputStream(backupFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0){
                    outputStream.write(buffer, 0, length);
                }
            }
            finally {
                assert inputStream != null;
                inputStream.close();
                assert outputStream != null;
                outputStream.close();
            }
        }
    }
}
