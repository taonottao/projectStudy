package com.example.mtl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wt.mtl.dao")
public class MtlUsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtlUsApplication.class, args);
    }

}
