package com.example.mmq;

import com.example.mmq.mqserver.BrokerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class MmqApplication {

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        context = SpringApplication.run(MmqApplication.class, args);

        BrokerServer brokerServer = new BrokerServer(9090);
        brokerServer.start();
    }

}
