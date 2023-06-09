package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 这个类表示数据库中的 blog 表的内容
 * 每个 Blog 对象, 就对应 blog 表中的一条记录
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/6/4 22:05
 */
public class Blog {
    private int blogId;
    private String title;
    private String content;
    private int userId;
    private Timestamp postTime;

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public Timestamp getPostTime() {
//        return postTime;
//    }

    public String getPostTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(postTime);
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }
}
