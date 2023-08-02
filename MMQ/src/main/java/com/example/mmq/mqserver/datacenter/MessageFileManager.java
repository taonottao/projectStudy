package com.example.mmq.mqserver.datacenter;

/**
 * 通过这个类来针对硬盘上的消息进行管理
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/1 19:11
 */
public class MessageFileManager {

    // 定义一个内部类来表示该队列的统计信息
    static public class Stat {
        public int totalCount; // 总消息数量
        public int validCount; // 有效消息数量
    }

    // 预定消息文件所在的目录和文件名
    // 这个方法，用来获取到指定队列对应的消息文件所在的路径
    private String getQueueDir(String queueName) {
        return "./data/" + queueName;
    }

    // 这个方法用来获取该队列的消息数据文件
    // 注意，二进制文件，使用 txt 作为后缀不太合适，因为 txt 一般指文本文件，但是将就着吧
    private String getQueueDataPath(String queueName) {
        return getQueueDir(queueName) + "/queue_data.txt";
    }

    // 这个方法用来获取该队列的消息统计文件路径
    private String getQueueStatPath(String queueName) {
        return getQueueDir(queueName) + "/queue_stat.txt";
    }

}
