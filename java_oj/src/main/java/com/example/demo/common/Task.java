package com.example.demo.common;


import com.example.demo.controller.ProblemController;
import com.example.demo.entity.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

        // 进行安全性判定
        if(!checkCodeSafe(question.getCode())){
            System.out.println("用户提交了不安全的代码!");
            answer.setError(3);
            answer.setReason("您提交的代码不安全！");
            return answer;
        }
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

    private boolean checkCodeSafe(String code) {
        List<String> blackList = new ArrayList<>();
        // 反之提交的代码运行恶意的程序
        blackList.add("Runtime");
        blackList.add("exec");
        // 禁止提交的代码读写文件
        blackList.add("java.io");
        // 禁止提交的代码访问网络
        blackList.add("java.net");

        for (String target : blackList) {
            int pos = code.indexOf(target);
            if (pos >= 0) {
                // 找到任意的恶意代码，返回 false 表示不安全
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Task task = new Task();

        ProblemController problemController = new ProblemController();

        Problem problem = new Problem();
        problem.setTitle("两数之和");
        problem.setLevel("简单");
        problem.setDescription("给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。\n" +
                "\n" +
                "你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。\n" +
                "\n" +
                "你可以按任意顺序返回答案。\n" +
                "\n" +
                " \n" +
                "\n" +
                "示例 1：\n" +
                "\n" +
                "输入：nums = [2,7,11,15], target = 9\n" +
                "输出：[0,1]\n" +
                "解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。\n" +
                "示例 2：\n" +
                "\n" +
                "输入：nums = [3,2,4], target = 6\n" +
                "输出：[1,2]\n" +
                "示例 3：\n" +
                "\n" +
                "输入：nums = [3,3], target = 6\n" +
                "输出：[0,1]\n" +
                " \n" +
                "\n" +
                "提示：\n" +
                "\n" +
                "2 <= nums.length <= 104\n" +
                "-109 <= nums[i] <= 109\n" +
                "-109 <= target <= 109\n" +
                "只会存在一个有效答案\n" +
                " \n" +
                "\n" +
                "进阶：你可以想出一个时间复杂度小于 O(n2) 的算法吗？\n" +
                "\n" +
                "来源：力扣（LeetCode）\n" +
                "链接：https://leetcode.cn/problems/two-sum\n" +
                "著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。");
        problem.setTemplateCode("class Solution {\n" +
                "    public int[] twoSum(int[] nums, int target) {\n" +
                "        \n" +
                "    }\n" +
                "}");
        problem.setTestCode("public static void main(String[] args) {\n" +
                "        Solution solution = new Solution();\n" +
                "        // testcase1\n" +
                "        int[] nums = {2,7,11,15};\n" +
                "        int target = 9;\n" +
                "        int[] result = solution.twoSum(nums, target);\n" +
                "        if (result.length == 2 && result[0] == 0 && result[1] == 1) {\n" +
                "            System.out.println(\"testcase1 OK\");\n" +
                "        }else {\n" +
                "            System.out.println(\"testcase1 failed!\");\n" +
                "        }\n" +
                "\n" +
                "        // testcase2\n" +
                "        int[] nums2 = {3, 2, 4};\n" +
                "        int target2 = 6;\n" +
                "        int[] result2 = solution.twoSum(nums2, target2);\n" +
                "        if (result2.length == 2 && result2[0] == 1 && result2[1] == 2) {\n" +
                "            System.out.println(\"testcase2 OK\");\n" +
                "        }else {\n" +
                "            System.out.println(\"testcase2 failed!\");\n" +
                "        }\n" +
                "    }");

//        Question question = new Question();
//        question.setCode("public class Solution {\n" +
//                "    public static void main(String[] args) {\n" +
//                "        System.out.println(\"hello world\");\n" +
//                "    }\n" +
//                "}");
//        Answer answer = task.compileAndRun(question);
//        System.out.println(answer);
    }
}
