package com.example.demo.common;


import java.io.File;
import java.util.UUID;

/**
 * 约定一个 “编译+运行” 为一个 Task
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/28 11:49
 */
public class Task {
    // 约定一组临时变量存放编译运行的信息
    private String WORK_DIR = null;
    private String CODE = null;
    private String CLASS = null;
    private String COMPILE_ERROR = null;
    private String STDOUT = null;
    private String STDERR = null;

    public Task(){
        // 为了是多进程之间互不影响，每个进程都有唯一得路径
        WORK_DIR = "./tmp/" + UUID.randomUUID().toString() + "/";
        CODE = WORK_DIR + "Solution.java";
        CLASS = "Solution";
        COMPILE_ERROR = WORK_DIR + "compileError.txt";
        STDOUT = WORK_DIR + "stdout.txt";
        STDERR = WORK_DIR + "stderr.txt";
    }

    public Answer CompileAndRun(Question question) {

        File workDir = new File(WORK_DIR);
        if (!workDir.exists()) {
            workDir.mkdirs();
        }

        Answer answer = new Answer();

        String code = question.getCode();

        FileUtil.writeFile(CODE, code);

        String compileCmd = String.format("javac -encoding utf8 %s -d %s", CODE, WORK_DIR);

        CommandUtil.run(compileCmd, null, COMPILE_ERROR);

        String compileError = FileUtil.readFile(COMPILE_ERROR);

        if (compileError != null || !"".equals(compileError)) {
            // 编译出错
            System.out.println("编译出错！！");
            answer.setError(1);
            answer.setReason(compileError);
            return answer;
        }

        // 编译没问题，继续运行
        String runCmd = String.format("java -classpath %s %s", WORK_DIR, CLASS);

        CommandUtil.run(runCmd, STDOUT, STDERR);

        String runError = FileUtil.readFile(STDERR);
        if(!runError.equals("")){
            System.out.println("运行出错！！");
            answer.setError(2);
            answer.setReason(runError);
        }

        // 运行正常
        String stdout = FileUtil.readFile(STDOUT);
        answer.setError(0);
        answer.setStdout(stdout);

        return answer;

    }

}
