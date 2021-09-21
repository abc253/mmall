package com.li;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.li.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class MMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMallApplication.class,args);
    }
}
