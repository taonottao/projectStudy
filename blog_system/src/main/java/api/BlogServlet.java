package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
}
