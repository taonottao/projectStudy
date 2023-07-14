package com.example.demo.common;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * 每次的 “编译+运行” 这个过程，就成为是一个 Task
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 13:48
 */
public class Task {
    // 约定一些常量来表示下面的文件
    // 这个表示所有临时文件所在的目录
    private static String WORK_DIR = "./tmp/";
    // 约定代码的类名
    private static String CLASS = null;
    // 约定要编译的代码文件名
    private static String CODE = null;
    // 约定存放编译错误信息的文件名
    private static String COMPILE_ERROR = null;
    // 约定存放运行时的标准输出文件名
    private static String STDOUT = null;
    // 约定存放运行时的标准错误文件名
    private static String STDERR = null;

    public Task(){
        // 在 Java 中使用 UUID 这个类就能生成一个 UUID 了
        WORK_DIR = "./tmp/" + UUID.randomUUID().toString() + "/";
        CLASS = "Solution";
        CODE = WORK_DIR + "Solution.java";
        COMPILE_ERROR = WORK_DIR + "compileError.txt";
        STDOUT = WORK_DIR + "stdout.txt";
        STDERR = WORK_DIR + "stderr.txt";
    }

    /**
     * 这个 Task 类提供的核心方法，就叫做 compileAndRun 编译+运行的意思
     * 参数：要编译运行的java 源代码
     * 返回值：表示编译运行的结果。编译出错/运行出错/运行正确....
     */
    public Answer compileAndRun(Question question) throws FileNotFoundException {
        // 0. 创建临时文件所在目录
        File workDir = new File(WORK_DIR);
        if(!workDir.exists()){
            // 创建多级目录
            workDir.mkdirs();
        }
        Answer answer = new Answer();

        // 1. 把 question 中的 code 写入到一个 Solution.java 文件中
        FileUtil.writeFile(CODE, question.getCode());
        // 2. 创建子进程，调用 javac 进行编译。注意！编译的时候需要有一个.java文件
        //      如果编译出错，javac 就会把错误信息写入到 stderr 里，就可以用一个专门的文件来保存 compileError.txt
        //    需要先把编译命令构造出来
        String compileCmd = String.format("javac -encoding utf8 %s -d %s", CODE, WORK_DIR);
        System.out.println("编译命令：" + compileCmd);
        CommandUtil.run(compileCmd, null, COMPILE_ERROR);
        // 如果编译出错，错误信息就会记录到 COMPILE_ERROR 这个文件中了，如果没有出错，这个文件为空文件
        String compileError = FileUtil.readFile(COMPILE_ERROR);
        if(!compileError.equals("")){
            // 编译出错
            System.out.println("编译出错！");
            // 注解返回 Answer，让 Answer 里面记录错误信息
            answer.setError(1);
            answer.setReason(compileError);
            return answer;
        }
        // 编译正确，继续往下执行运行的逻辑
        // 3. 创建子进程，调用 java 命令并执行
        //      运行程序的时候，也会把 java 的标准输出和标准错误获取到，stdout.txt   stderr.txt
        String runCmd = String.format("java -classpath %s %s", WORK_DIR, CLASS);
        System.out.println("运行命令：" + runCmd);
        CommandUtil.run(runCmd, STDOUT, STDERR);
        String runError = FileUtil.readFile(STDERR);
        if (!runError.equals("")) {
            System.out.println("运行出错!");
            answer.setError(2);
            answer.setReason(runError);
            return answer;
        }
        // 4. 父进程获取到刚才编译执行的结果，并打包成 Answer 对象
        //      编译执行的结果，就通过刚才约定的这几个文件来进行获取即可
        answer.setError(0);
        answer.setStdout(FileUtil.readFile(STDOUT));
        return answer;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Task task = new Task();
        Question question = new Question();
        question.setCode("public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"hello world\");\n" +
                "    }\n" +
                "}");
        Answer answer = task.compileAndRun(question);
        System.out.println(answer);
    }
}
