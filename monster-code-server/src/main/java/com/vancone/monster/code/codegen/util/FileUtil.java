package com.vancone.monster.code.codegen.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class FileUtil {
    public static String read(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            int inputStreamAvailable = inputStream.available();
            byte[] byteArray = new byte[inputStreamAvailable];
            inputStream.read(byteArray);
            inputStream.close();
            return new String(byteArray);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean write(String url, String data) {
        try {
            if (url.lastIndexOf(File.separator) > 0) {
                createDirectoryIfNotExist(url.substring(0, url.lastIndexOf(File.separator)));
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url));
            bufferedWriter.write(data);
            bufferedWriter.close();
            return true;
        } catch (IOException ex) {
            log.warn(ex.getMessage());
            return false;
        }
    }

    public static void createDirectoryIfNotExist(String path) {
        File directory = new File(path);
        if ((!directory.exists()) || (!directory.isDirectory())) {
            directory.mkdirs();
        }
    }

    public static List<String> readLine(String path) {
        List<String> arrayList = new ArrayList<>();
        try {
            File file = new File(path);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);

            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            return null;
        }

        return arrayList;
    }

    public static void copyDirectory(String srcDir, String destDir) {
        if (srcDir.charAt(srcDir.length() - 1) != File.separator.charAt(0)) {
            srcDir += File.separator;
        }

        if (destDir.charAt(destDir.length() - 1) != File.separator.charAt(0)) {
            destDir += File.separator;
        }

        createDirectoryIfNotExist(destDir);

        File srcDirFile = new File(srcDir);
        if (!srcDirFile.exists()) {
            log.error("srcDirFile is null: {}", srcDir);
            return;
        }
        for (File file : Objects.requireNonNull(srcDirFile.listFiles())) {
            if (file.isFile()) {
                try {
                    File destFile = new File(destDir + file.getName());
                    if (destFile.exists()) {
                        Files.delete(destFile.toPath());
                    }
                    Files.copy(file.toPath(), destFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                copyDirectory(srcDir + file.getName(), destDir + file.getName());
            }
        }
    }

    public static void copyFile(String srcFile, String destFile) {
        File srcDirFile = new File(srcFile);
        if (!srcDirFile.isFile()) {
            log.error("srcFile is not a file");
        }
        if (!srcDirFile.exists()) {
            log.error("srcFile is null: {}", srcFile);
            return;
        }

        File destDirFile = new File(destFile);
        if (destDirFile.exists()) {
            try {
                Files.delete(destDirFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.copy(srcDirFile.toPath(), destDirFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> listAllFile(String path) {
        List<File> files = new ArrayList<>();
        File parentDir = new File(path);
        if (parentDir.exists()) {
            if (parentDir.isDirectory()) {
                for (File file: Objects.requireNonNull(parentDir.listFiles())) {
                    if (file.isDirectory()) {
                        files.addAll(listAllFile(file.getPath()));
                    } else {
                        files.add(file);
                    }
                }
            } else {
                log.error("Path not a directory: {}", path);
            }
        } else {
            log.error("Path not exist: {}", path);
        }
        return files;
    }
}
