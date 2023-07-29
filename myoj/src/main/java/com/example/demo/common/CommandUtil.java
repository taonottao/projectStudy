package com.example.demo.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建进程
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/28 11:33
 */
public class CommandUtil {

    public static int run(String cmd, String stdout, String stderr){

        // 创建进程
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);

            if (stdout != null) {
                InputStream stdoutFrom = process.getInputStream();
                FileOutputStream stroutTo = new FileOutputStream(stdout);
                while (true) {
                    int ch = stdoutFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stroutTo.write(ch);
                }
                stroutTo.close();
                stdoutFrom.close();
            }

            if (stderr != null) {
                InputStream strerrFrom = process.getErrorStream();
                FileOutputStream stderrTo = new FileOutputStream(stderr);
                while (true) {
                    int ch = strerrFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stderrTo.write(ch);
                }
                stderrTo.close();
                strerrFrom.close();
            }

            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
