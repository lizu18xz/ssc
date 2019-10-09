package com.fayayo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dalizu on 2019/9/25.
 * @version v1.0
 * @desc
 */
@SpringBootApplication
@RestController
public class ClientStartUp {

    @Value("${questname}")
    String name;


    public static void main(String[] args) {

        SpringApplication.run(ClientStartUp.class,args);
    }

    @RequestMapping("/")
    public String sqyHello(){

        return "Hello,"+name;
    }


}
