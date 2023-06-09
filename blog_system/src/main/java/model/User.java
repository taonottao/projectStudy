package model;

/**
 * 这个类表示数据库中的 user 表
 * 每个 User 实例就代表 user 表中的一个记录.
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/6/4 22:05
 */
public class User {
    private int userId;
    private String username;
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
