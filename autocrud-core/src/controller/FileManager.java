package controller;

import java.io.*;

public class FileManager {
    public static String read(String path){
        try {
            InputStream inputStream = new FileInputStream(path);
            int inputStreamAvailable = inputStream.available();
            byte[] bytes = new byte[inputStreamAvailable];
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch(Exception e) {
            Logger.error("Read file \"" + path + "\" failed");
        }
        return null;
    }

    public static void write(String url, String data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url));
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch(IOException ex) {
            Logger.error(ex.getMessage());
        }
    }

    public static void checkDirectory(String path) {
        File directory = new File(path);
        if ((!directory.exists()) || (!directory.isDirectory())) {
            directory.mkdirs();
        }
    }
}
