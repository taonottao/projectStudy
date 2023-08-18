package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.Answer;
import com.example.demo.common.Question;
import com.example.demo.common.Task;
import com.example.demo.entity.Problem;
import com.example.demo.service.ProblemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 19:18
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/insert")
    public AjaxResult add(Problem problem) {
        // 1. 非空校验
        if (problem == null || !StringUtils.hasLength(problem.getTitle())
                || !StringUtils.hasLength(problem.getDescription())
                || !StringUtils.hasLength(problem.getTestCode())
                || !StringUtils.hasLength(problem.getTemplateCode())) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 2. 数据库的添加操作
        return AjaxResult.success(problemService.insert(problem));
    }

    @RequestMapping("/delete")
    public AjaxResult delete(Integer id) {
        if (id == null || id < 0) {
            // 参数有误
            return AjaxResult.fail(-1, "参数异常");
        }

        return AjaxResult.success(problemService.delete(id));
    }

    @RequestMapping("/selectall")
    public AjaxResult selectAll() {
        List<Problem> list = problemService.selectAll();
        return AjaxResult.success(list);
    }

    @RequestMapping("/selectone")
    public AjaxResult selectOne(Integer id) {
        if (id == null || id < 0) {
            // 参数有误
            return AjaxResult.fail(-1, "参数异常");
        }
        Problem problem = problemService.selectOne(id);
        if(problem == null || !StringUtils.hasLength(problem.getTitle())){
            return AjaxResult.fail(-1, "参数异常");
        }

        return AjaxResult.success(problem);
    }


    static class CompileRequest{
        public int id;
        public String templateCode;
    }
    static class CompileResponse{
        public int error;
        public String reason;
        public String stdout;
    }
    @RequestMapping("/compile-run")
    public AjaxResult compileAndRun(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException, FileNotFoundException {
        response.setContentType("application/json;charset=utf8");
        // 1. 先读取请求的正文，并且按照 json 格式进行解析
        String body = readBody(request);
        if (body == null) {
            return AjaxResult.fail(-1, "请求正文为空!!");
        }
        CompileRequest compileRequest = objectMapper.readValue(body, CompileRequest.class);
        // 2. 根据 id 从数据库中查找到题目的详情 => 得到测试用例代码
        Problem problem = problemService.selectOne(compileRequest.id);
        if (problem == null) {
            return AjaxResult.fail(-1, "题目序号参数非法!!");
        }
        // testCode 是测试用例代码
        String testCode = problem.getTestCode();
        // requestCode 是用户提交的代码
        String requestCode = compileRequest.templateCode;
        if (requestCode == null) {
            return AjaxResult.fail(-1, "提交代码参数非法!!");
        }
        // 3. 把用户提交的代码和测试用例代码拼接成一个完整的代码
        String finalCode = mergeCode(requestCode, testCode);
        if (finalCode == null) {
            return AjaxResult.fail(-1, "提交代码参数非法!!");
        }
//        System.out.println(finalCode);
        // 4. 创建一个 Task 实例，调用里面的 compileAndRun 来编译运行
        Task task = new Task();
        Question question = new Question();
        question.setCode(finalCode);
        Answer answer = task.compileAndRun(question);
        // 5. 根据 Task 运行结果，包装成一个 HTTP 响应
        CompileResponse compileResponse = new CompileResponse();
        compileResponse.error = answer.getError();
        compileResponse.reason = answer.getReason();
        compileResponse.stdout = answer.getStdout();
        return AjaxResult.success(compileResponse);
    }


    private static String mergeCode(String requestCode, String testCode) {
        // 1. 查找最后一个 } 的位置
        int pos = requestCode.lastIndexOf("}");
        if (pos == -1) {
            // 说明提交的代码没有 } , 是非法代码
            return null;
        }
        String subStr = requestCode.substring(0, pos);
        return subStr + testCode + "\n}";
    }

    private static String readBody(HttpServletRequest request) throws UnsupportedEncodingException {
        // 1. 先根据 请求头 里面的 ContentLength 获取到 body 的长度（单位是字节）
        int contentLength = request.getContentLength();
        // 2. 按照这个长度准备好一个 byte[]
        byte[] buffer = new byte[contentLength];
        // 3. 通过 请求 里面的 getInputStream 方法，获取到 body 的流对象
        try(InputStream inputStream = request.getInputStream()) {
            // 4. 基于这个流对象，读取内容，放到 byte[] 中
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5. 把这个 byte[] 的内容构造成一个 String
        return new String(buffer, "utf8");
    }

    @RequestMapping("/praise")
    public AjaxResult praise(Integer id) {
        System.out.println(id);
        if (id == null || id < 0) {
            return AjaxResult.fail(-1, "参数非法！");
        }
        Problem problem = problemService.selectOne(id);
        if (problem == null) {
            return AjaxResult.fail(-1, "题目序号参数非法！");
        }
        problemService.praise(id);
        problem.setLikeCount(problem.getLikeCount() + 1);
        return AjaxResult.success(200, problem.getLikeCount());
    }
}
