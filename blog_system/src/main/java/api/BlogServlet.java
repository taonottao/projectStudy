package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 通过这个类, 来实现一些后端提供的接口
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/6/5 12:43
 */
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogDao blogDao = new BlogDao();
        // 从 query string 中查询一下看是否有 blogId, 如果有就认为是查询指定博客, 如果没有就是查询所有博客
        String blogId = req.getParameter("blogId");
        if(blogId == null){
            List<Blog> blogs = blogDao.selectAll();
            String respString = objectMapper.writeValueAsString(blogs);
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write(respString);
        } else {
            Blog blog = blogDao.selectOne(Integer.parseInt(blogId));
            if(blog == null){
                System.out.println("当前 blogId = " + blogId + " 对应的博客不存在!");
            }
            String respString = objectMapper.writeValueAsString(blog);
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write(respString);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 发布博客
        // 读取请求, 构造 Blog 对象, 插入数据库中即可
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录, 无法发布博客!");
            return;
        }
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录, 无法发布博客!");
            return;
        }
        // 确保登陆之后, 就可以把作者给拿到了.

        // 获取博客正文和标题
        req.setCharacterEncoding("utf8");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        if(title == null || "".equals(title) || content == null || "".equals(content)){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前提交数据有误!! 标题或正文为空!!!");
            return;
        }

        // 构造 Blog 对象
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(user.getUserId());
        // 发布时间, 在 java / 数据库中生成都行
        blog.setPostTime(new Timestamp(System.currentTimeMillis()));
        // 插入数据库
        BlogDao blogDao = new BlogDao();
        blogDao.insert(blog);

        // 跳转到博客列表页
        resp.sendRedirect("blog_list.html");
    }
}
