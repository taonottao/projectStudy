package com.example.mmq.mqclient;

import lombok.Data;

import java.io.IOException;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/11 14:14
 */
@Data
public class ConnectionFactory {

    // broker server 的 ip地址
    private String host;
    // broker server 的端口号
    private int port;

    // 访问 broker server 的哪个虚拟主机
    // 下列几个属性暂时先不搞了。
//    private String virtualHostName;
//    private String username;
//    private String password;

    public Connection newConnection() throws IOException {
        Connection connection = new Connection(host, port);
        return connection;
    }

}
