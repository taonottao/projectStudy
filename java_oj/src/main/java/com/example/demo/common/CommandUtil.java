package com.example.demo.common;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类用来实现对代码的编译运行
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 12:32
 */
public class CommandUtil {

    /**
     * 1. 通过 Runtime 类 得到实例，执行 exec、
     * 2. 利用 process获取到标准输出与标准错误，并分别写入到指定文件中
     * 3. 等待子进程结束，获取子进程的退出码（状态码），并返回
     * @param cmd
     * @param stdoutFile
     * @param stderrFile
     * @return
     */
    public static int run(String cmd, String stdoutFile, String stderrFile){
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            if (stdoutFile != null) {
                InputStream stdoutFrom = process.getInputStream();
                FileOutputStream stdoutTo = new FileOutputStream(stdoutFile);
                while (true){
                    int ch = stdoutFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stdoutTo.write(ch);
                }

                stdoutFrom.close();
                stdoutTo.close();
            }

            if (stderrFile != null) {
                InputStream stderrFrom = process.getErrorStream();
                FileOutputStream stderrTo = new FileOutputStream(stderrFile);
                while (true) {
                    int ch = stderrFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stderrTo.write(ch);
                }
                stderrFrom.close();
                stderrTo.close();
            }
            // 4. 等待子进程结束，拿到子进程的状态码并返回
            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void main(String[] args) {
        run("javac", "stdout.txt", "stderr.txt");
    }


}
