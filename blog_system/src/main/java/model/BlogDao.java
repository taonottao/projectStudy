package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/6/4 22:19
 */
public class BlogDao {
    // 把一个 Blog 对象插入到 数据库中
    public void insert(Blog blog){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 建立连接
            connection = DBUtil.getConnection();
            // 2. 构造 sql
            String sql = "insert into blog values(null, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getContent());
            statement.setInt(3, blog.getUserId());
            // 如果数据库表里是 dateTime 类型, 插入数据的时候, 按照 TimeStamp 来插入或者按照格式化时间来插入都可以
            statement.setString(4, blog.getPostTime());
            // 3. 执行 sql
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(connection, statement, null);
        }
    }

    // 查询 blog 表中所有的博客数据
    public List<Blog> selectAll(){
        List<Blog> blogs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog order by postTime desc";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                String content = resultSet.getString("content");
                if(content.length() > 100){
                    content = content.substring(0, 100) + "...";
                }
                blog.setContent(content);
                blog.setUserId(resultSet.getInt("userId"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blogs.add(blog);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return blogs;
    }

    // 指定一个博客id, 来查询对应的博客
    public Blog selectOne(int blogId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet =  null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                blog.setUserId(resultSet.getInt("userId"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                return blog;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }

        return null;
    }

    // 指定一个博客id 来删除博客
    public void delete(int blogId){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "delete from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }
}
