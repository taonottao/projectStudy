package com.example.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/11 20:39
 */
public class TestFile {
    public static void main(String[] args) throws IOException {
        // 通过这个简单的程序，把一个文件的内容读取出来，写入到另一个文件中（文件拷贝）
        String srcPath = "e:\\test1.txt";
        String destPath = "e:\\test2.txt";

        // 读写文件之前，一定要先的打开文件！！！
        // 通过这个代码打开了第一个用来读数据的文件
        FileInputStream fileInputStream = new FileInputStream(srcPath);
        // 通过这个代码打开了第二个用来写数据的文件
        FileOutputStream fileOutputStream = new FileOutputStream(destPath);

        // 循环的把第一个文件的内容按字节取出来，写入到第二个文件中
        while (true) {
            // read 方法一次返回的是一个字节（byte），但实际上是以 int 来进行接收的
            // 这样做的理由主要是两个方面
            // 1. Java 中不存在无符号类型。byte 这样的类型也是有符号的。byte 的表示范围：-128-127
            //    实际上在按照字节读取数据的时候，并不需要这样的数据进行运算，此时，这里的正负就没有意义了，
            //    因此期望读到的结果是一个无符号的数字，0-255
            // 2. read如果读取完毕（文件读到末尾了）就会返回EOF(end of file)，用 -1 来表示
            int read = fileInputStream.read();
            if (read == -1) {
                break;
            }
            fileOutputStream.write(read);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
