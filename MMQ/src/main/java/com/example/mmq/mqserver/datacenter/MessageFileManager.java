package com.example.mmq.mqserver.datacenter;

import com.example.mmq.common.BinaryTool;
import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.core.Message;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

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

    private Stat readStat(String queueName) {
        // 由于当前的消息统计文件是文本文件，可以直接使用 Scanner 来读取文件内容
        Stat stat = new Stat();
        try (InputStream inputStream = new FileInputStream(getQueueStatPath(queueName))){
            Scanner scanner = new Scanner(inputStream);
            stat.totalCount = scanner.nextInt();
            stat.validCount = scanner.nextInt();
            return stat;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void writeStat(String queueName, Stat stat){
        // 使用 PrintWrite 来写文件
        // OutputStream 打开文件，默认情况下会直接把源文件清空，此时相当于新的文件覆盖了旧的。
        try (OutputStream outputStream = new FileOutputStream(getQueueStatPath(queueName))) {
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.write(stat.totalCount + "\t" + stat.validCount);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 创建队列对应的文件和目录
    public void createQueueFiles(String queueName) throws IOException {
        // 1. 先创建队列对应的消息目录
        File baseDir = new File(getQueueDir(queueName));
        if (!baseDir.exists()) {
            boolean isSuccess = baseDir.mkdirs();
            if (!isSuccess) {
                throw new IOException("创建目录失败！baseDir=" + baseDir.getAbsolutePath());
            }
        }
        // 2. 创建队列数据文件
        File queueDataFile = new File(getQueueDataPath(queueName));
        if (!queueDataFile.exists()) {
            boolean isSuccess = queueDataFile.mkdirs();
            if (!isSuccess) {
                throw new IOException("创建文件失败！queueDataFile="+queueDataFile.getAbsolutePath());
            }
        }
        // 3. 创建消息统计文件
        File queueStatFile = new File(getQueueStatPath(queueName));
        if (!queueStatFile.exists()) {
            boolean isSuccess = queueStatFile.mkdirs();
            if (!isSuccess) {
                throw new IOException("创建文件失败！queueStatFile="+queueStatFile.getAbsolutePath());
            }
        }
        // 4. 给消息统计文件设定初始值，0\t0
        Stat stat = new Stat();
        stat.validCount = 0;
        stat.totalCount = 0;
        writeStat(queueName, stat);
    }

    // 删除队列的目录和文件
    // 队列也是可以删除的，党队列删除以后，对应的文件自然也要删除
    public void destoryQueueFiles(String queueName) throws IOException {
        // 先删除里面的文件，再删除目录
        File queueDataFile = new File(getQueueDataPath(queueName));
        boolean succ1 = queueDataFile.delete();
        File queueStatFile = new File(getQueueStatPath(queueName));
        boolean succ2 = queueStatFile.delete();
        File baseDir = new File(getQueueDir(queueName));
        boolean succ3 = baseDir.delete();
        if (!succ1 || !succ2 || !succ3) {
            // 有任意一个删除失败，就算删除失败
            throw new IOException("删除队列目录和文件失败！baseDir" +baseDir.getAbsolutePath());
        }
    }

    public boolean checkFilesExits(String queueName) {
        // 判断队列的数据文件和统计文件是否都存在
        File queueDataFile = new File(getQueueDataPath(queueName));
        if (!queueDataFile.exists()) {
            return false;
        }
        File queueStatFile = new File(getQueueStatPath(queueName));
        if (!queueStatFile.exists()) {
            return false;
        }

        return true;
    }

    // 使用这个方法来把一个新的消息放到对应的文件中
    // queue 表示要把消息写入到的队列，message 则是要写的消息
    public void sendMessage(MSGQueue queue, Message message) throws MQException, IOException {
        // 1. 检查一下当前要写入的队列对应的文件是否存在
        if (!checkFilesExits(queue.getName())) {
            throw new MQException("[MessageFileManager] 队列对应的文件不存在！queueName=" + queue.getName());
        }
        // 2. 把 Message 对象进行序列化，转成二进制的字节数组
        byte[] messageBinary = BinaryTool.toBytes(message);
        synchronized (queue) {
            // 3. 先获取到当前队列数据文件的长度，用这个来计算出改 Messag 对象的 offseBeg 和 offsetEnd
            // 把新的 Message 数据，写入到队列数据文件末尾。此时 Message 对象的 offsetBeg 就是当前文件长度 + 4
            // offsetEnd 就是当前文件长度 + 4 + message 自身长度
            File queueDataFile = new File(getQueueDataPath(queue.getName()));
            // 通过 queueDataFile.length() 就能获取到文件的长度，单位字节
            message.setOffsetBeg(queueDataFile.length() + 4);
            message.setOffsetEnd(queueDataFile.length() + 4 + messageBinary.length);
            // 4. 写入消息到数据文件，注意，此处是追加写入到数据文件的末尾
            try (OutputStream outputStream = new FileOutputStream(queueDataFile, true)) {
                try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                    // 接下来要先写当前文件的长度，占据四个字节
                    dataOutputStream.writeInt(messageBinary.length);
                    // 写入消息本体
                    dataOutputStream.write(messageBinary);
                }
            }
            // 5. 更新消息统计文件
            Stat stat = readStat(queue.getName());
            stat.totalCount += 1;
            stat.validCount += 1;
            writeStat(queue.getName(), stat);
        }
    }

    /**
     * 这个是删除消息的方法
     * 这里的删除是逻辑删除，也就是把硬盘上存储的数据里的 isValid 属性设置为 0
     * 1. 先把文件中的这段数据读出来，还原成 Message 对象
     * 2. 把 isValid 改成 0
     * 3. 把上述数据重新写回到文件
     * 此处这个参数中的 message 对象，必须得包含有效的 offsetBeg 和 offsetEnd
     * @param queue
     * @param message
     */
    public void deleteMessage(MSGQueue queue, Message message) throws IOException, ClassNotFoundException {
        synchronized (queue) {
            try(RandomAccessFile randomAccessFile = new RandomAccessFile(getQueueDataPath(queue.getName()), "rw")){
                // 1.
                byte[] bufferSrc = new byte[(int) (message.getOffsetEnd() - message.getOffsetBeg())];
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.read(bufferSrc);
                Message diskMessage = (Message) BinaryTool.fromBytes(bufferSrc);
                // 2.
                diskMessage.setIsValue((byte) 0x0);
                // 3.
                byte[] bufferDest = BinaryTool.toBytes(diskMessage);
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.write(bufferDest);
            }

            // 更新统计文件
            Stat stat = new Stat();
            if (stat.validCount > 0) {
                stat.validCount -= 1;
            }
            writeStat(queue.getName(), stat);
        }
    }

    /**
     * 通过这个方法，从文件中读取出所有的消息内容，加载到内存中（具体来说是放到一个链表中）
     * 这个方法，准备在程序启动的时候调用
     * 这里使用 LinkedList 主要目的是为了后续的头删操作
     * @param queueName
     * @return
     */
    public LinkedList<Message> loadAllMessageFromQueue(String queueName) throws IOException, MQException {
        LinkedList<Message> messages = new LinkedList<>();
        try (InputStream inputStream = new FileInputStream(getQueueDataPath(queueName))) {
            try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
                while (true) {
                    // 1. 读取当前消息长度
                    int messageSize = dataInputStream.readInt();
                    // 2. 按照这个长度，读取消息内容
                    byte[] buffer = new byte[messageSize];
                    int actualSize = dataInputStream.read(buffer);
                    if (actualSize != messageSize) {
                        // 如果不匹配，说明文件有问题，格式错乱了
                        throw new MQException("[MessageFileManager] 文件格式错误！queueName" + queueName);
                    }
                }
            }
        }
    }

}
