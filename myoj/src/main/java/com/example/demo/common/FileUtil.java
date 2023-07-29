package com.example.demo.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 读文件写文件两个方法
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/28 12:16
 */
public class FileUtil {

    public static String readFile(String filePath) {

        StringBuilder s = new StringBuilder();

        try (FileReader fileReader = new FileReader(filePath)) {
            while (true) {
                int ch = fileReader.read();
                if (ch == -1) {
                    break;
                }
                s.append(ch);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public static void writeFile(String filePath, String content) {
        try(FileWriter fileWriter = new FileWriter(filePath)) {

            fileWriter.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
