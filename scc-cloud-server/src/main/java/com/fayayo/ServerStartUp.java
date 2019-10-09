package com.fayayo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author dalizu on 2019/9/25.
 * @version v1.0
 * @desc
 */
@SpringBootApplication
@EnableConfigServer
public class ServerStartUp {

    public static void main(String[] args) {

        SpringApplication.run(ServerStartUp.class,args);

    }

}
