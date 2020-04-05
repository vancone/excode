package com.mekcone.excrud.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static String read(String path){
        try {
            InputStream inputStream = new FileInputStream(path);
            int inputStreamAvailable = inputStream.available();
            byte[] bytes = new byte[inputStreamAvailable];
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean write(String url, String data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url));
            bufferedWriter.write(data);
            bufferedWriter.close();
            return true;
        } catch(IOException ex) {
            LogUtil.warn(ex.getMessage());
            return false;
        }
    }

    public static void checkDirectory(String path) {
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
}
