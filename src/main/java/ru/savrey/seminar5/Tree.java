package ru.savrey.seminar5;

import java.io.File;

public class Tree {

    /**
     * Метод вывода дерева директорий и файлов в консоль
     * @param file Имя директории или файла.
     * @param indent Отступ от левого края (у первой директории = "").
     * @param isLast Флаг - последняя директория или файл или нет.
     */
    public static void printTree(File file, String indent, boolean isLast){
        System.out.print(indent); // рисуем отступ
        if (isLast){
            System.out.print("└─");
            indent += "  ";
        } else {
            System.out.print("├─");
            indent += "│ ";
        }
        System.out.println(file.getName());

        File[] files = file.listFiles();
        if (files == null)
            return;

        int subDirTotal = 0;
        int filesTotal = 0;
        for (File value : files) {
            if (value.isDirectory())
                subDirTotal++;
            else if (value.isFile()) {
                filesTotal++;
            }
        }

        int subDirCounter = 0;
        int filesCounter = 0;
        for (File value : files) {
            if (value.isDirectory()) {
                printTree(value, indent, subDirCounter == subDirTotal - 1 && filesCounter == filesTotal);
                subDirCounter++;
            }
        }
        for (File value : files){
            if (value.isFile()) {
                printTree(value, indent, filesCounter == filesTotal - 1);
                filesCounter++;
            }
        }
    }
}
